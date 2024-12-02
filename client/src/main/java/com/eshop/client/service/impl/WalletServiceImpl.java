package com.eshop.client.service.impl;

import com.eshop.client.entity.QWalletEntity;
import com.eshop.client.entity.WalletEntity;
import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.RoleType;
import com.eshop.client.enums.TransactionType;
import com.eshop.client.filter.WalletFilter;
import com.eshop.client.mapping.WalletMapper;
import com.eshop.client.model.BalanceModel;
import com.eshop.client.model.SubscriptionPackageModel;
import com.eshop.client.model.WalletModel;
import com.eshop.client.repository.WalletRepository;
import com.eshop.client.service.SubscriptionService;
import com.eshop.client.service.WalletService;
import com.eshop.client.util.DateUtil;
import com.eshop.client.util.SessionHolder;
import com.eshop.exception.common.BadRequestException;
import com.eshop.exception.common.NotAcceptableException;
import com.eshop.exception.common.NotFoundException;
import com.eshop.exception.common.PaymentRequiredException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.eshop.client.util.DateUtil.toLocalDate;

@Service
public class WalletServiceImpl extends BaseServiceImpl<WalletFilter,WalletModel, WalletEntity, Long> implements WalletService {

    private final WalletRepository walletRepository;
    private final SubscriptionService subscriptionService;
    private final JPAQueryFactory queryFactory;
    private final SessionHolder sessionHolder;

    public WalletServiceImpl(WalletRepository repository, WalletMapper mapper, SubscriptionService subscriptionService, JPAQueryFactory queryFactory, SessionHolder sessionHolder) {
        super(repository, mapper);
        this.walletRepository = repository;
        this.subscriptionService = subscriptionService;
        this.queryFactory = queryFactory;
        this.sessionHolder = sessionHolder;
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
        filter.getCurrency().ifPresent(value -> builder.and(path.currency.eq(value)));
        filter.getTransactionType().ifPresent(value -> builder.and(path.transactionType.eq(value)));
        filter.getTransactionTypes().ifPresent(value -> builder.and(path.transactionType.in(value)));
        filter.getTransactionHash().ifPresent(value -> builder.and(path.transactionHash.eq(value)));
        filter.getUserId().ifPresent(value -> builder.and(path.user.id.eq(value)));
        filter.getActive().ifPresent(value -> builder.and(path.active.eq(value)));
        filter.getAddress().ifPresent(value -> builder.and(path.address.eq(value)));

        return builder;
    }

    @Override
    @Transactional
    public WalletModel create(WalletModel model, String allKey) {
        var totalBalance = walletRepository.totalBalanceGroupedByCurrency(model.getUser().getId());
        if(model.getTransactionType().equals(TransactionType.WITHDRAWAL)) {
            for (BalanceModel balanceModel : totalBalance) {
                if(model.getCurrency().equals(balanceModel.getCurrency())) {
                    if(balanceModel.getTotalAmount().compareTo(model.getAmount()) < 0)
                        throw new PaymentRequiredException();
                }
            }
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            if(currentSubscription == null)
                throw new PaymentRequiredException();

            SubscriptionPackageModel currentSubscriptionPackage = currentSubscription.getSubscriptionPackage();
            if(!model.getCurrency().equals(currentSubscriptionPackage.getCurrency()))
                throw new NotAcceptableException("The currency type does not match your currently active subscription's currency.");

            var totalProfit = walletRepository.totalProfitGroupedByCurrency(model.getUser().getId()).stream().filter(f->f.getCurrency().equals(currentSubscriptionPackage.getCurrency())).findAny().orElse(new BalanceModel(currentSubscriptionPackage.getCurrency(),BigDecimal.ZERO));
            if(model.getAmount().compareTo(currentSubscription.getFinalPrice()) >= 0 || model.getAmount().compareTo(totalProfit.getTotalAmount()) > 0) {
                if(currentSubscription.getRemainingWithdrawalPerDay() > 0L)
                    throw new NotAcceptableException(String.format("You can withdraw your funds after %d days.",currentSubscription.getRemainingWithdrawalPerDay()));
                model.setTransactionType(TransactionType.WITHDRAWAL);
            } else {
                // withdrawal profit
                model.setTransactionType(TransactionType.WITHDRAWAL_PROFIT);
                if (model.getAmount().compareTo(new BigDecimal(15)) < 0)
                    throw new NotAcceptableException(String.format("You need to request more than 15 %s.", currentSubscriptionPackage.getCurrency().getTitle()));
                if (model.getAmount().compareTo(totalProfit.getTotalAmount()) > 0)
                    throw new NotAcceptableException(String.format("Insufficient balance, You are not allowed to withdraw more than %s of the total profit.", totalProfit.getTotalAmount()));
            }

        } else if(model.getTransactionType().equals(TransactionType.DEPOSIT)) {
            //please deposit more than the subscription amount
        }
        model.setActive(false);
        var result =  super.create(model, allKey);
//        if(model.isActive()) {
//            balance = walletRepository.findBalanceGroupedByCurrency(model.getUser().getId());
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
        model.setActive(false);
        var result =  super.update(model, key, allKey);
//        if(model.isActive()) {
//            var balance = walletRepository.findBalanceGroupedByCurrency(model.getUser().getId());
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

        var balance = walletRepository.totalBalanceGroupedByCurrency(entity.getUser().getId());
        var subscriptionModel = subscriptionService.findByUserAndActivePackage(entity.getUser().getId());
        for (BalanceModel balanceModel : balance) {
            if(subscriptionModel.getSubscriptionPackage().getCurrency().equals(balanceModel.getCurrency()) && subscriptionModel.getSubscriptionPackage().getPrice().compareTo(balanceModel.getTotalAmount()) > 0) {
                subscriptionService.logicalDeleteById(subscriptionModel.getId());
            }
        }

        repository.delete(entity);
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalBalanceGroupedByCurrency'")
    public List<BalanceModel> totalBalanceGroupedByCurrency(UUID userId) {
        return walletRepository.totalBalanceGroupedByCurrency(userId);
    }
    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalDepositGroupedByCurrency'")
    public List<BalanceModel> totalDepositGroupedByCurrency(UUID userId) {
        return walletRepository.totalDepositGroupedByCurrency(userId);
    }
    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalWithdrawalGroupedByCurrency'")
    public List<BalanceModel> totalWithdrawalGroupedByCurrency(UUID userId) {
        return walletRepository.totalWithdrawalGroupedByCurrency(userId);
    }
    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalBonusGroupedByCurrency'")
    public List<BalanceModel> totalBonusGroupedByCurrency(UUID userId) {
        return walletRepository.totalBonusGroupedByCurrency(userId);
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalRewardGroupedByCurrency'")
    public List<BalanceModel> totalRewardGroupedByCurrency(UUID userId) {
        return walletRepository.totalRewardGroupedByCurrency(userId);
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':totalProfitGroupedByCurrency'")
    public List<BalanceModel> totalProfitGroupedByCurrency(UUID userId) {
        return walletRepository.totalProfitGroupedByCurrency(userId);
    }

    @Override
    @Cacheable(cacheNames = "client", key = "'Wallet:' + #userId.toString() + ':dailyProfitGroupedByCurrency'")
    public List<BalanceModel> dailyProfitGroupedByCurrency(UUID userId) {
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


        return queryFactory.select(Projections.constructor(BalanceModel.class, path.currency, rewardBonusSum.subtract(withdrawalProfitSum)))
                .from(path)
                .where(path.user.id.eq(userId))
                .where(truncatedDate.eq(DateUtil.truncate(new Date())))
                .groupBy(path.currency)
                .fetch();
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
