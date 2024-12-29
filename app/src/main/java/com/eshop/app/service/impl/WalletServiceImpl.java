package com.eshop.app.service.impl;

import com.eshop.app.client.NetworkStrategyFactory;
import com.eshop.app.entity.QWalletEntity;
import com.eshop.app.entity.WalletEntity;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.enums.TransactionType;
import com.eshop.app.filter.WalletFilter;
import com.eshop.app.mapping.WalletMapper;
import com.eshop.app.model.ParameterModel;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.model.WalletModel;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.*;
import com.eshop.app.util.DateUtil;
import com.eshop.exception.common.NotAcceptableException;
import com.eshop.exception.common.NotFoundException;
import com.eshop.exception.common.PaymentRequiredException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.eshop.app.util.DateUtil.toLocalDate;
import static com.eshop.app.util.MapperHelper.get;
import static com.eshop.app.util.MapperHelper.getOrDefault;

@Service
@Slf4j
public class WalletServiceImpl extends BaseServiceImpl<WalletFilter,WalletModel, WalletEntity, Long> implements WalletService {

    private final WalletRepository walletRepository;
    private final SubscriptionPackageService subscriptionPackageService;
    private final SubscriptionService subscriptionService;
    private final ParameterServiceImpl parameterService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final NetworkStrategyFactory networkStrategyFactory;
    private final JPAQueryFactory queryFactory;
    private final RoleService roleService;

    public WalletServiceImpl(WalletRepository repository, WalletMapper mapper, SubscriptionPackageService subscriptionPackageService, SubscriptionService subscriptionService, ParameterServiceImpl parameterService, UserService userService, NotificationService notificationService, NetworkStrategyFactory networkStrategyFactory, JPAQueryFactory queryFactory, RoleService roleService) {
        super(repository, mapper);
        this.walletRepository = repository;
        this.subscriptionPackageService = subscriptionPackageService;
        this.subscriptionService = subscriptionService;
        this.parameterService = parameterService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.networkStrategyFactory = networkStrategyFactory;
        this.queryFactory = queryFactory;
        this.roleService = roleService;
    }

    @Override
    public Predicate queryBuilder(WalletFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QWalletEntity path = QWalletEntity.walletEntity;

        if(!RoleType.hasRole(RoleType.ADMIN)) {
            builder.and(path.user.roles.any().role.ne(RoleType.ADMIN));
            builder.and(path.role.eq(RoleType.firstRole()));
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
        filter.getTransactionHash().ifPresent(value -> builder.and(path.transactionHash.eq(value)));
        filter.getUserId().ifPresent(value -> builder.and(path.user.id.eq(value)));
        filter.getStatus().ifPresent(value -> builder.and(path.status.eq(value)));
        filter.getAddress().ifPresent(value -> builder.and(path.address.eq(value)));
        filter.getRoleId().ifPresent(v->{
            var role = roleService.findById(v);
            builder.and(path.role.eq(role.getRole()));
        });

        return builder;
    }
    @Override
    @Transactional
    public WalletModel createFromAdmin(WalletModel model) {
        var balance = walletRepository.totalBalance(model.getRole());
        if(model.getTransactionType().equals(TransactionType.WITHDRAWAL)) {
            if(balance.compareTo(model.getAmount()) < 0)
                throw new PaymentRequiredException();
        }
        model.setStatus(EntityStatusType.Active);
        if(model.getRoleId()!=null && !StringUtils.hasLength(model.getRole())) {
            var role = roleService.findById(model.getRoleId());
            model.setRole(role.getRole());
        }
        return super.create(model);
    }

    @Override
    @Transactional
    public WalletModel create(WalletModel model) {
        var user = userService.findById(model.getUser().getId());
        var balance = walletRepository.calculateUserBalance(model.getUser().getId());
        if(model.getTransactionType().equals(TransactionType.WITHDRAWAL)) {
            if(balance.compareTo(model.getAmount()) < 0)
                throw new PaymentRequiredException();
        }
        model.setRole(user.getRole());
        var result =  super.create(model);
        boolean transactionIsValid = validateTransaction(model);
        if(model.getStatus().equals(EntityStatusType.Active) && (RoleType.hasRole(RoleType.ADMIN) || transactionIsValid)) {
            balance = walletRepository.calculateUserBalance(model.getUser().getId());
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());

            var nextSubscriptionPackage = subscriptionPackageService.findMatchedPackageByAmount(balance);
            if(nextSubscriptionPackage != null && (currentSubscription == null || !currentSubscription.getSubscriptionPackage().getId().equals(nextSubscriptionPackage.getId()))) {
                currentSubscription = subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(nextSubscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
            }
            final var newSubscription = currentSubscription;
            if(model.getTransactionType().equals(TransactionType.WITHDRAWAL) && getOrDefault(()-> model.getAmount().compareTo(newSubscription.getSubscriptionPackage().getPrice()) >=0,false)){
                if(RoleType.hasRole(RoleType.USER) && newSubscription.getRemainingWithdrawalPerDay() > 0)
                    throw new NotAcceptableException(String.format("Withdrawal is allowed only after %d days", newSubscription.getRemainingWithdrawalPerDay()));
            }
            if(model.getTransactionType().equals(TransactionType.DEPOSIT) && walletRepository.countByUserIdAndTransactionTypeAndStatus(model.getUser().getId(),TransactionType.DEPOSIT,EntityStatusType.Active) == 1) {
                if (get(() -> user.getParent()) != null) {
                    WalletModel bonus1 = new WalletModel();
                    bonus1.setStatus(EntityStatusType.Active);
                    bonus1.setUser(user.getParent());
                    bonus1.setAmount(referralDepositBonus(model.getAmount()));
                    bonus1.setActualAmount(referralDepositBonus(model.getAmount()));
                    bonus1.setTransactionType(TransactionType.BONUS);
                    bonus1.setRole(user.getRole());
                    create(bonus1);
                }
            }
            notificationService.sendTransactionNotification(model);

        }
        return result;
    }

    @Override
    @Transactional
    public WalletModel update(WalletModel model) {
        var entity = repository.findById(model.getId()).orElseThrow(() -> new NotFoundException(String.format("%s not found by id %d", model.getClass().getName(), model.getId().toString())));
        if(get(()->model.getUser().getId())!=null)
            entity.setUser(entityManager.getReference(entity.getUser().getClass(), model.getUser().getId()));
        var result = mapper.toModel(repository.save(mapper.updateEntity(model, entity)));
        if(model.getStatus().equals(EntityStatusType.Active) && (RoleType.hasRole(RoleType.ADMIN) || validateTransaction(model))) {
            var balance = walletRepository.calculateUserBalance(model.getUser().getId());
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());

            var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmount(balance);
            if(subscriptionPackage != null && (currentSubscription == null || !currentSubscription.getSubscriptionPackage().getId().equals(subscriptionPackage.getId()))) {
                currentSubscription = subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
            }
            final var newSubscription = currentSubscription;
            if(model.getTransactionType().equals(TransactionType.WITHDRAWAL) && getOrDefault(()-> model.getAmount().compareTo(newSubscription.getSubscriptionPackage().getPrice()) >=0,false)){
                if(RoleType.hasRole(RoleType.USER) && newSubscription.getRemainingWithdrawalPerDay() > 0)
                    throw new NotAcceptableException(String.format("Withdrawal is allowed only after %d days.", newSubscription.getRemainingWithdrawalPerDay()));
                if(userService.countAllActiveChild(model.getUser().getId()) < newSubscription.getSubscriptionPackage().getOrderCount())
                    throw new NotAcceptableException(String.format("To withdraw your funds you need to have at least %d referrals.", newSubscription.getSubscriptionPackage().getOrderCount()));
            }
            if(model.getTransactionType().equals(TransactionType.DEPOSIT) && walletRepository.countByUserIdAndTransactionTypeAndStatus(model.getUser().getId(),TransactionType.DEPOSIT,EntityStatusType.Active) == 1) {
                var user = userService.findById(model.getUser().getId());
                if (get(() -> user.getParent()) != null) {
                    WalletModel bonus1 = new WalletModel();
                    bonus1.setStatus(EntityStatusType.Active);
                    bonus1.setUser(user.getParent());
                    bonus1.setAmount(referralDepositBonus(model.getAmount()));
                    bonus1.setActualAmount(referralDepositBonus(model.getAmount()));
                    bonus1.setTransactionType(TransactionType.BONUS);
                    bonus1.setRole(user.getRole());
                    create(bonus1);
                }
            }
            notificationService.sendTransactionNotification(model);

        }
        return result;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        WalletEntity entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));

        var balance = walletRepository.calculateUserBalance(entity.getUser().getId());
        var subscriptionModel = subscriptionService.findByUserAndActivePackage(entity.getUser().getId());
        if(subscriptionModel.getSubscriptionPackage().getPrice().compareTo(balance) > 0) {
            subscriptionService.logicalDeleteById(subscriptionModel.getId());
        }
        repository.delete(entity);
    }
    @Override
    public BigDecimal referralDepositBonus(BigDecimal amount) {
//        List<ParameterModel> parameters = parameterService.findAllByParameterGroupCode("REFERRAL_DEPOSIT_BONUS");
//        for (ParameterModel parameter : parameters) {
//            // Check if the amount falls within the range
//            var split = parameter.getTitle().split("~");
//            BigDecimal lowerLimit = new BigDecimal(split[0]);
//            BigDecimal upperLimit = new BigDecimal(split[1]);
//            if (amount.compareTo(lowerLimit) >= 0 && amount.compareTo(upperLimit) <= 0) {
//                return new BigDecimal(parameter.getValue());
//            }
//        }
        return BigDecimal.ONE;
    }

    @Override
    public BigDecimal totalBalance() {
        String role = null;
        if(!RoleType.hasRole(RoleType.ADMIN))
            role = RoleType.firstRole();
        return walletRepository.totalBalance(role);
    }
    @Override
    public BigDecimal totalDeposit() {
        String role = null;
        if(!RoleType.hasRole(RoleType.ADMIN))
            role = RoleType.firstRole();
        return walletRepository.totalDeposit(role);
    }
    @Override
    public BigDecimal totalWithdrawal() {
        String role = null;
        if(!RoleType.hasRole(RoleType.ADMIN))
            role = RoleType.firstRole();
        return walletRepository.totalWithdrawal(role);
    }
    @Override
    public BigDecimal totalBonus() {
        String role = null;
        if(!RoleType.hasRole(RoleType.ADMIN))
            role = RoleType.firstRole();
        return walletRepository.totalBonus(role);
    }
    @Override
    public BigDecimal totalReward() {
        String role = null;
        if(!RoleType.hasRole(RoleType.ADMIN))
            role = RoleType.firstRole();
        return walletRepository.totalReward(role);
    }

//    @Override
//    public BalanceModel totalProfit(UUID userId) {
//        var result = walletRepository.totalProfit(userId);
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
                .where(path.user.roles.any().id.eq(2L))
                .groupBy(truncatedDate)
                .orderBy(truncatedDate.asc())
                .fetch();
        Map<Long, BigDecimal> map = results.stream()
                .collect(Collectors.toMap(tuple -> tuple.get(truncatedDate).getTime(), tuple -> tuple.get(path.amount.sum())));

        var allDates = toLocalDate(startDate).datesUntil(toLocalDate(endDate).plusDays(1)).map(DateUtil::toEpoch);

        return allDates.collect(Collectors.toMap(epoch -> epoch, epoch -> map.getOrDefault(epoch, BigDecimal.ZERO)));
    }
    @Override
    public boolean validateTransaction(WalletModel model) {
        try {
            var network = networkStrategyFactory.get(model.getNetwork());
            var transaction = network.getTransactionInfo(model.getTransactionHash());
            if (transaction == null)
                return false;
            if (!transaction.isSuccess())
                return false;
            if (transaction.getAmount().subtract(model.getActualAmount().setScale(4, RoundingMode.HALF_UP)).abs().compareTo(new BigDecimal("0.0001")) != 0)
                return false;
            if (!transaction.getToAddress().equals(model.getAddress()))
                return false;
            return true;
        } catch (Exception e){
            log.error("Transaction not valid. {}", e.getMessage());
            return false;
        }
    }
}
