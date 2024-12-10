package com.eshop.app.service.impl;

import com.eshop.app.config.MessageConfig;
import com.eshop.app.entity.QWalletEntity;
import com.eshop.app.entity.WalletEntity;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.enums.TransactionType;
import com.eshop.app.filter.WalletFilter;
import com.eshop.app.mapping.WalletMapper;
import com.eshop.app.model.*;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.*;
import com.eshop.app.util.DateUtil;
import com.eshop.app.util.SessionHolder;
import com.eshop.exception.common.NotAcceptableException;
import com.eshop.exception.common.NotFoundException;
import com.eshop.exception.common.PaymentRequiredException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.eshop.app.util.DateUtil.toLocalDate;
import static com.eshop.app.util.MapperHelper.get;
import static com.eshop.app.util.MapperHelper.getOrDefault;

@Service
public class WalletServiceImpl extends BaseServiceImpl<WalletFilter,WalletModel, WalletEntity, Long> implements WalletService {

    private final WalletRepository walletRepository;
    private final SubscriptionPackageService subscriptionPackageService;
    private final SubscriptionService subscriptionService;
    private final ParameterServiceImpl parameterService;
    private final UserService userService;
    private final MessageConfig messages;
    private final ResourceLoader resourceLoader;
    private final NotificationService notificationService;
    private final JPAQueryFactory queryFactory;
    private final SessionHolder sessionHolder;

    public WalletServiceImpl(WalletRepository repository, WalletMapper mapper, SubscriptionPackageService subscriptionPackageService, SubscriptionService subscriptionService, ParameterServiceImpl parameterService, UserService userService, MessageConfig messages, ResourceLoader resourceLoader, NotificationService notificationService, JPAQueryFactory queryFactory, SessionHolder sessionHolder) {
        super(repository, mapper);
        this.walletRepository = repository;
        this.subscriptionPackageService = subscriptionPackageService;
        this.subscriptionService = subscriptionService;
        this.parameterService = parameterService;
        this.userService = userService;
        this.messages = messages;
        this.resourceLoader = resourceLoader;
        this.notificationService = notificationService;
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
        filter.getTransactionHash().ifPresent(value -> builder.and(path.transactionHash.eq(value)));
        filter.getUserId().ifPresent(value -> builder.and(path.user.id.eq(value)));
        filter.getActive().ifPresent(value -> builder.and(path.active.eq(value)));
        filter.getAddress().ifPresent(value -> builder.and(path.address.eq(value)));

        return builder;
    }

    @Override
    @Transactional
    public WalletModel create(WalletModel model) {
        var balance = walletRepository.totalBalanceGroupedByCurrencyByUserId(model.getUser().getId());
        if(model.getTransactionType().equals(TransactionType.WITHDRAWAL)) {
            var balanceOptional = balance.stream().filter(x->x.getCurrency().equals(model.getCurrency())).findAny();
            if (balanceOptional.isPresent()) {
                var balanceModel = balanceOptional.get();
                if(model.getCurrency().equals(balanceModel.getCurrency())) {
                    if(balanceModel.getTotalAmount().compareTo(model.getAmount()) < 0)
                        throw new PaymentRequiredException();
                }
            }
        }
        var result =  super.create(model);
        if(model.isActive()) {
            balance = walletRepository.totalBalanceGroupedByCurrencyByUserId(model.getUser().getId());
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            var balanceOptional = balance.stream().filter(x->x.getCurrency().equals(model.getCurrency())).findAny();
            if (balanceOptional.isPresent()) {
                var balanceModel = balanceOptional.get();
                var nextSubscriptionPackage = subscriptionPackageService.findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(),balanceModel.getCurrency());
                if(nextSubscriptionPackage != null && (currentSubscription == null || !currentSubscription.getSubscriptionPackage().getId().equals(nextSubscriptionPackage.getId()))) {
                    currentSubscription = subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(nextSubscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
                }
                final var newSubscription = currentSubscription;
                if(model.getTransactionType().equals(TransactionType.WITHDRAWAL) && getOrDefault(()-> model.getAmount().compareTo(newSubscription.getSubscriptionPackage().getPrice()) >=0,false)){
                    if(newSubscription.getRemainingWithdrawalPerDay() > 0)
                        throw new NotAcceptableException(String.format("Withdrawal is allowed only after %d days", newSubscription.getRemainingWithdrawalPerDay()));
                }
                if(model.getTransactionType().equals(TransactionType.DEPOSIT) && !walletRepository.existsByUserIdAndTransactionTypeAndActiveTrue(model.getUser().getId(),TransactionType.DEPOSIT)) {
                    var user = userService.findById(model.getUser().getId());
                    if (get(() -> user.getParent()) != null) {
                        WalletModel bonus1 = new WalletModel();
                        bonus1.setActive(true);
                        bonus1.setUser(user.getParent());
                        bonus1.setAmount(referralDepositBonus(model.getAmount()));
//                        bonus1.setAddress(parameterService.findByCode("WALLET_ADDRESS").getValue());
                        bonus1.setCurrency(balanceModel.getCurrency());
                        bonus1.setTransactionType(TransactionType.BONUS);
                        create(bonus1);
                    }
                }
                notificationService.sendTransactionNotification(model);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public WalletModel update(WalletModel model) {
        var result =  super.update(model);
        if(model.isActive()) {
            var balance = walletRepository.totalBalanceGroupedByCurrencyByUserId(model.getUser().getId());
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            var balanceOptional = balance.stream().filter(x->x.getCurrency().equals(model.getCurrency())).findAny();
            if (balanceOptional.isPresent()) {
                var balanceModel = balanceOptional.get();
                var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(),balanceModel.getCurrency());
                if(subscriptionPackage != null && (currentSubscription == null || !currentSubscription.getSubscriptionPackage().getId().equals(subscriptionPackage.getId()))) {
                    currentSubscription = subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
                }
                final var newSubscription = currentSubscription;
                if(model.getTransactionType().equals(TransactionType.WITHDRAWAL) && getOrDefault(()-> model.getAmount().compareTo(newSubscription.getSubscriptionPackage().getPrice()) >=0,false)){
                    if(newSubscription.getRemainingWithdrawalPerDay() > 0)
                        throw new NotAcceptableException(String.format("Withdrawal is allowed only after %d days.", newSubscription.getRemainingWithdrawalPerDay()));
                }
                if(model.getTransactionType().equals(TransactionType.DEPOSIT) && !walletRepository.existsByUserIdAndTransactionTypeAndActiveTrue(model.getUser().getId(),TransactionType.DEPOSIT)) {
                    var user = userService.findById(model.getUser().getId());
                    if (get(() -> user.getParent()) != null) {
                        WalletModel bonus1 = new WalletModel();
                        bonus1.setActive(true);
                        bonus1.setUser(user.getParent());
                        bonus1.setAmount(referralDepositBonus(model.getAmount()));
//                        bonus1.setAddress(parameterService.findByCode("WALLET_ADDRESS").getValue());
                        bonus1.setCurrency(balanceModel.getCurrency());
                        bonus1.setTransactionType(TransactionType.BONUS);
                        create(bonus1);
                    }
                }
                notificationService.sendTransactionNotification(model);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        WalletEntity entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));

        var balance = walletRepository.totalBalanceGroupedByCurrencyByUserId(entity.getUser().getId());
        var subscriptionModel = subscriptionService.findByUserAndActivePackage(entity.getUser().getId());
        var balanceOptional = balance.stream().filter(x->x.getCurrency().equals(entity.getCurrency())).findAny();
        if (balanceOptional.isPresent()) {
            var balanceModel = balanceOptional.get();
            if(subscriptionModel.getSubscriptionPackage().getCurrency().equals(balanceModel.getCurrency()) && subscriptionModel.getSubscriptionPackage().getPrice().compareTo(balanceModel.getTotalAmount()) > 0) {
                subscriptionService.logicalDeleteById(subscriptionModel.getId());
            }
        }

        repository.delete(entity);
    }
    private BigDecimal referralDepositBonus(BigDecimal amount) {
        List<ParameterModel> parameters = parameterService.findAllByParameterGroupCode("REFERRAL_DEPOSIT_BONUS");
        for (ParameterModel parameter : parameters) {
            // Check if the amount falls within the range
            var split = parameter.getTitle().split("~");
            BigDecimal lowerLimit = new BigDecimal(split[0]);
            BigDecimal upperLimit = new BigDecimal(split[1]);
            if (amount.compareTo(lowerLimit) >= 0 && amount.compareTo(upperLimit) <= 0) {
                return new BigDecimal(parameter.getValue());
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<BalanceModel> totalBalanceGroupedByCurrency() {
        return walletRepository.totalBalanceGroupedByCurrency();
    }
    @Override
    public List<BalanceModel> totalDepositGroupedByCurrency() {
        return walletRepository.totalDepositGroupedByCurrency();
    }
    @Override
    public List<BalanceModel> totalWithdrawalGroupedByCurrency() {
        return walletRepository.totalWithdrawalGroupedByCurrency();
    }
    @Override
    public List<BalanceModel> totalBonusGroupedByCurrency() {
        return walletRepository.totalBonusGroupedByCurrency();
    }
    @Override
    public List<BalanceModel> totalRewardGroupedByCurrency() {
        return walletRepository.totalRewardGroupedByCurrency();
    }

//    @Override
//    public List<BalanceModel> totalProfitGroupedByCurrency(UUID userId) {
//        var result = walletRepository.totalProfitGroupedByCurrency(userId);
//        return result.stream()
//                .map(obj -> new BalanceModel(CurrencyType.valueOf((String) obj[0]),(BigDecimal) obj[1]))
//                .collect(Collectors.toList());
//    }

@Override
public Map<Long, BigDecimal> findAllWithinDateRange(long startDate, long endDate, TransactionType transactionType) {
    QWalletEntity path = QWalletEntity.walletEntity;
    DateTemplate<Date> truncatedDate = Expressions.dateTemplate(Date.class, "date_trunc('day', {0})", path.createdDate);
    var results = queryFactory.select(truncatedDate, path.amount.sum())
            .from(path)
            .where(truncatedDate.between(new Date(startDate),new Date(endDate)))
            .where(path.transactionType.eq(transactionType))
            .groupBy(truncatedDate)
            .orderBy(truncatedDate.asc())
            .fetch();
    Map<Long, BigDecimal> map = results.stream()
            .collect(Collectors.toMap(tuple -> tuple.get(truncatedDate).getTime(), tuple -> tuple.get(path.amount.sum())));

    var allDates = toLocalDate(startDate).datesUntil(toLocalDate(endDate).plusDays(1)).map(DateUtil::toEpoch);

    return allDates.collect(Collectors.toMap(epoch -> epoch, epoch -> map.getOrDefault(epoch, BigDecimal.ZERO)));
}
}
