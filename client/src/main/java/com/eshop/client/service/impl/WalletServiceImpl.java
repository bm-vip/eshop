package com.eshop.client.service.impl;

import com.eshop.client.entity.QWalletEntity;
import com.eshop.client.entity.WalletEntity;
import com.eshop.client.enums.EntityStatusType;
import com.eshop.client.enums.RoleType;
import com.eshop.client.enums.TransactionType;
import com.eshop.client.filter.WalletFilter;
import com.eshop.client.mapping.WalletMapper;
import com.eshop.client.model.SubscriptionPackageModel;
import com.eshop.client.model.WalletModel;
import com.eshop.client.repository.WalletRepository;
import com.eshop.client.service.*;
import com.eshop.client.util.DateUtil;
import com.eshop.client.util.SessionHolder;
import com.eshop.exception.common.NotAcceptableException;
import com.eshop.exception.common.NotFoundException;
import com.eshop.exception.common.PaymentRequiredException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.eshop.client.util.DateUtil.toLocalDate;
import static com.eshop.client.util.StringUtils.generateIdKey;

@Service
public class WalletServiceImpl extends BaseServiceImpl<WalletFilter,WalletModel, WalletEntity, Long> implements WalletService {

    private final WalletRepository walletRepository;
    private final SubscriptionService subscriptionService;
    private final JPAQueryFactory queryFactory;
    private final SessionHolder sessionHolder;
    private final String minWithdrawAmount;
    private final UserService userService;
    private final MailService mailService;

    public WalletServiceImpl(WalletRepository repository, WalletMapper mapper, SubscriptionService subscriptionService, JPAQueryFactory queryFactory, SessionHolder sessionHolder, ParameterService parameterService, UserService userService, MailService mailService) {
        super(repository, mapper);
        this.walletRepository = repository;
        this.subscriptionService = subscriptionService;
        this.queryFactory = queryFactory;
        this.sessionHolder = sessionHolder;
        this.minWithdrawAmount = parameterService.findByCode("MIN_WITHDRAW").getValue();
        this.userService = userService;
        this.mailService = mailService;
    }

    @Override
    public Predicate queryBuilder(WalletFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QWalletEntity path = QWalletEntity.walletEntity;

        if(!RoleType.hasRole(RoleType.ADMIN)) {
            builder.and(path.user.roles.any().role.ne(RoleType.ADMIN));
        }

        filter.getId().ifPresent(value -> builder.and(path.id.eq(value)));
        filter.getAmount().ifPresent(value -> builder.and(path.amount.eq(value)));
        filter.getAmountFrom().ifPresent(value -> builder.and(path.amount.goe(value)));
        filter.getAmountTo().ifPresent(value -> builder.and(path.amount.loe(value)));
        filter.getActualAmount().ifPresent(value -> builder.and(path.actualAmount.eq(value)));
        filter.getActualAmountFrom().ifPresent(value -> builder.and(path.actualAmount.goe(value)));
        filter.getActualAmountTo().ifPresent(value -> builder.and(path.actualAmount.loe(value)));
        filter.getCurrency().ifPresent(value -> builder.and(path.currency.eq(value)));
        filter.getNetwork().ifPresent(value -> builder.and(path.network.eq(value)));
        filter.getTransactionType().ifPresent(value -> builder.and(path.transactionType.eq(value)));
        filter.getTransactionTypes().ifPresent(value -> builder.and(path.transactionType.in(value)));
        filter.getTransactionHash().ifPresent(value -> builder.and(path.transactionHash.eq(value)));
        filter.getUserId().ifPresent(value -> builder.and(path.user.id.eq(value)));
        filter.getStatus().ifPresent(value -> builder.and(path.status.eq(value)));
        filter.getAddress().ifPresent(value -> builder.and(path.address.eq(value)));

        return builder;
    }

    @Override
    @Transactional
    public WalletModel create(WalletModel model, String allKey) {
        var user = userService.findById(model.getUser().getId(), generateIdKey("User",model.getUser().getId()));
//        if(!user.isEmailVerified()) {
//            mailService.sendVerification(user.getEmail(),"Email verification link");
//            throw new ExpectationException("Please verify your email before make this transaction.");
//        }
        var totalBalance = totalBalanceByUserId(model.getUser().getId());
        if(model.getTransactionType().equals(TransactionType.WITHDRAWAL)) {
            if(totalBalance.compareTo(model.getAmount()) < 0)
                throw new PaymentRequiredException();

            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            if(currentSubscription == null)
                throw new PaymentRequiredException();

            SubscriptionPackageModel currentSubscriptionPackage = currentSubscription.getSubscriptionPackage();
            if(!model.getCurrency().equals(currentSubscriptionPackage.getCurrency()))
                throw new NotAcceptableException("The currency type does not match your currently active subscription's currency.");

            var totalProfit = walletRepository.totalProfit(model.getUser().getId());
            if(model.getAmount().compareTo(currentSubscription.getFinalPrice()) >= 0 || model.getAmount().compareTo(totalProfit) > 0) {
                if(currentSubscription.getRemainingWithdrawalPerDay() > 0L)
                    throw new NotAcceptableException(String.format("You can withdraw your funds after %d days.",currentSubscription.getRemainingWithdrawalPerDay()));
                if(user.getChildCount() < currentSubscriptionPackage.getOrderCount()) {
                    throw new NotAcceptableException(String.format("To withdraw your funds you need to have at least %d referrals.", currentSubscriptionPackage.getOrderCount()));
                }
                model.setTransactionType(TransactionType.WITHDRAWAL);
            } else {
                // withdrawal profit
                model.setTransactionType(TransactionType.WITHDRAWAL_PROFIT);
                if (model.getAmount().compareTo(new BigDecimal(minWithdrawAmount)) < 0)
                    throw new NotAcceptableException(String.format("You need to request more than %s %s.", minWithdrawAmount, currentSubscriptionPackage.getCurrency().getTitle()));
                if (model.getAmount().compareTo(totalProfit) > 0)
                    throw new NotAcceptableException(String.format("Insufficient balance, You are not allowed to withdraw more than %s of the total profit.", totalProfit));
            }

        } else if(model.getTransactionType().equals(TransactionType.DEPOSIT)) {
            //please deposit more than the subscription amount
        }
        model.setStatus(EntityStatusType.Pending);
        model.setRole(user.getRole());
        var result =  super.create(model, allKey);
//        if(model.isActive()) {
//            balance = walletRepository.findBalance(model.getUser().getId());
//            for (BalanceModel balanceModel : balance) {
//                var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
//                var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(),balanceModel.getCurrency());
//                if(currentSubscription == null || (subscriptionPackage != null && !currentSubscription.getSubscriptionPackage().getId().equals(subscriptionPackage.getId()))) {
//                    subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
//                }
//            }
//        }
        return result;
    }

    @Override
    @Transactional
    public WalletModel update(WalletModel model, String key, String allKey) {
        var user = userService.findById(model.getUser().getId(), generateIdKey("User",model.getUser().getId()));
//        if(!user.isEmailVerified()) {
//            mailService.sendVerification(user.getEmail(),"Email verification link");
//            throw new ExpectationException("Please verify your email before make this transaction.");
//        }
        model.setStatus(EntityStatusType.Pending);
        var result =  super.update(model, key, allKey);
//        if(model.isActive()) {
//            var balance = walletRepository.findBalance(model.getUser().getId());
//            var subscriptionModel = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
//            for (BalanceModel balanceModel : balance) {
//                var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(),balanceModel.getCurrency());
//                if(subscriptionPackage != null && !subscriptionModel.getSubscriptionPackage().getId().equals(subscriptionPackage.getId())) {
//                    subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
//                }
//            }
//        }
        return result;
    }

    @Override
    @Transactional
    public void deleteById(Long id, String allKey) {
        WalletEntity entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));
        repository.delete(entity);

        var balance = totalBalanceByUserId(entity.getUser().getId());
        var subscriptionModel = subscriptionService.findByUserAndActivePackage(entity.getUser().getId());

        if(subscriptionModel.getSubscriptionPackage().getPrice().compareTo(balance) > 0) {
            subscriptionService.logicalDeleteById(subscriptionModel.getId());
        }
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalBalanceByUserId'")
    public BigDecimal totalBalanceByUserId(UUID userId) {
        return walletRepository.calculateUserBalance(userId);
    }
    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalDeposit'")
    public BigDecimal totalDeposit(UUID userId) {
        return walletRepository.totalDeposit(userId);
    }
    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalWithdrawal'")
    public BigDecimal totalWithdrawal(UUID userId) {
        return walletRepository.totalWithdrawal(userId);
    }
    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalBonus'")
    public BigDecimal totalBonus(UUID userId) {
        return walletRepository.totalBonus(userId);
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalReward'")
    public BigDecimal totalReward(UUID userId) {
        return walletRepository.totalReward(userId);
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalProfit'")
    public BigDecimal totalProfit(UUID userId) {
        return walletRepository.totalProfit(userId);
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':dailyProfit'")
    public BigDecimal dailyProfit(UUID userId) {
        QWalletEntity path = QWalletEntity.walletEntity;
        DateTemplate<Date> truncatedDate = Expressions.dateTemplate(Date.class, "date_trunc('day', {0})", path.createdDate);
        var rewardBonusSum =
                new CaseBuilder()
                        .when(path.transactionType.eq(TransactionType.REWARD)
                                .or(path.transactionType.eq(TransactionType.BONUS)))
                        .then(path.amount)
                        .otherwise(BigDecimal.ZERO)
                        .sum();

        var withdrawalProfitSum =
                new CaseBuilder()
                        .when(path.transactionType.eq(TransactionType.WITHDRAWAL_PROFIT))
                        .then(path.amount)
                        .otherwise(BigDecimal.ZERO)
                        .sum();


        return queryFactory.select(rewardBonusSum.subtract(withdrawalProfitSum))
                .from(path)
                .where(path.user.id.eq(userId))
                .where(path.status.eq(EntityStatusType.Active))
                .where(truncatedDate.eq(DateUtil.truncate(new Date())))
                .fetchOne();
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:findAllWithinDateRange:startDate:' + #startDate + ':endDate:' + #endDate + ':transactionType:' + #transactionType")
    public Map<Long, BigDecimal> findAllWithinDateRange(long startDate, long endDate, TransactionType transactionType) {
        QWalletEntity path = QWalletEntity.walletEntity;
        DateTemplate<Date> truncatedDate = Expressions.dateTemplate(Date.class, "date_trunc('day', {0})", path.createdDate);
        var results = queryFactory.select(truncatedDate, path.amount.sum())
                .from(path)
                .where(truncatedDate.between(new Date(startDate),new Date(endDate)))
                .where(path.transactionType.eq(transactionType))
                .where(path.user.id.eq(sessionHolder.getCurrentUser().getId()))
                .groupBy(truncatedDate)
                .orderBy(truncatedDate.asc())
                .fetch();
        Map<Long, BigDecimal> map = results.stream()
                .collect(Collectors.toMap(tuple -> tuple.get(truncatedDate).getTime(),tuple -> tuple.get(path.amount.sum())));

        var allDates = toLocalDate(startDate).datesUntil(toLocalDate(endDate).plusDays(1)).map(DateUtil::toEpoch);

        return allDates.collect(Collectors.toMap(epoch -> epoch, epoch -> map.getOrDefault(epoch, BigDecimal.ZERO)));
    }
}
