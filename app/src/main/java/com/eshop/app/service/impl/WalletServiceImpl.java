package com.eshop.app.service.impl;

import com.eshop.app.entity.QWalletEntity;
import com.eshop.app.entity.WalletEntity;
import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.enums.TransactionType;
import com.eshop.app.filter.WalletFilter;
import com.eshop.app.mapping.WalletMapper;
import com.eshop.app.model.*;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.SubscriptionPackageService;
import com.eshop.app.service.SubscriptionService;
import com.eshop.app.service.UserService;
import com.eshop.app.service.WalletService;
import com.eshop.app.util.DateUtil;
import com.eshop.exception.common.NotAcceptableException;
import com.eshop.exception.common.NotFoundException;
import com.eshop.exception.common.PaymentRequiredException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.eshop.app.util.MapperHelper.get;
import static com.eshop.app.util.MapperHelper.getOrDefault;

@Service
public class WalletServiceImpl extends BaseServiceImpl<WalletFilter,WalletModel, WalletEntity, Long> implements WalletService {

    private final WalletRepository walletRepository;
    private final SubscriptionPackageService subscriptionPackageService;
    private final SubscriptionService subscriptionService;
    private final ParameterServiceImpl parameterService;
    private final UserService userService;

    public WalletServiceImpl(WalletRepository repository, WalletMapper mapper, SubscriptionPackageService subscriptionPackageService, SubscriptionService subscriptionService, ParameterServiceImpl parameterService, UserService userService) {
        super(repository, mapper);
        this.walletRepository = repository;
        this.subscriptionPackageService = subscriptionPackageService;
        this.subscriptionService = subscriptionService;
        this.parameterService = parameterService;
        this.userService = userService;
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
        var balance = walletRepository.totalBalanceGroupedByCurrency(model.getUser().getId());
        if(model.getTransactionType().equals(TransactionType.WITHDRAWAL)) {
            for (BalanceModel balanceModel : balance) {
                if(model.getCurrency().equals(balanceModel.getCurrency())) {
                    if(balanceModel.getTotalAmount().compareTo(model.getAmount()) < 0)
                        throw new PaymentRequiredException();
                }
            }
        }
        var result =  super.create(model);
        if(model.isActive()) {
            balance = walletRepository.totalBalanceGroupedByCurrency(model.getUser().getId());
            for (BalanceModel balanceModel : balance) {
                var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
                var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(),balanceModel.getCurrency());
                if(currentSubscription == null || (subscriptionPackage != null && !currentSubscription.getSubscriptionPackage().getId().equals(subscriptionPackage.getId()))) {
                    subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
                }

                if(model.getTransactionType().equals(TransactionType.WITHDRAWAL) && getOrDefault(()-> model.getAmount().compareTo(currentSubscription.getSubscriptionPackage().getPrice()) >=0,false)){
                    var subscriptionPackageModel = currentSubscription.getSubscriptionPackage();
                    var withdrawalDate = subscriptionPackageModel.getCreatedDate().plusDays(subscriptionPackageModel.getWithdrawalDurationPerDay());
                    if(withdrawalDate.isAfter(LocalDateTime.now()))
                        throw new NotAcceptableException("Withdrawal is allowed only after " + withdrawalDate.toString());
                }
                if(model.getTransactionType().equals(TransactionType.DEPOSIT) && !walletRepository.existsByUserIdAndTransactionTypeAndCurrencyAndActiveTrue(model.getUser().getId(),TransactionType.DEPOSIT,balanceModel.getCurrency())) {
                    BigDecimal referralDepositBonus = referralDepositBonus(model.getAmount());
                    String walletAddress = parameterService.findByCode("WALLET_ADDRESS").getValue();
                    var user = userService.findById(model.getUser().getId());
                    if (get(() -> user.getParent()) != null) {
                        WalletModel bonus1 = new WalletModel();
                        bonus1.setActive(true);
                        bonus1.setUser(user.getParent());
                        bonus1.setAmount(referralDepositBonus(model.getAmount()));
                        bonus1.setAddress(walletAddress);
                        bonus1.setCurrency(balanceModel.getCurrency());
                        bonus1.setTransactionType(TransactionType.BONUS);
                        create(bonus1);
                    }
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public WalletModel update(WalletModel model) {
        var result =  super.update(model);
        if(model.isActive()) {
            var balance = walletRepository.totalBalanceGroupedByCurrency(model.getUser().getId());
            var subscriptionModel = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            for (BalanceModel balanceModel : balance) {
                var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(),balanceModel.getCurrency());
                if(subscriptionPackage != null) {
                    if(subscriptionModel != null && subscriptionModel.getSubscriptionPackage().getId().equals(subscriptionPackage.getId()))
                        return result;
                    subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
                }
                if(model.getTransactionType().equals(TransactionType.WITHDRAWAL) && getOrDefault(()-> model.getAmount().compareTo(subscriptionModel.getSubscriptionPackage().getPrice()) >=0,false)){
                    var subscriptionPackageModel = subscriptionModel.getSubscriptionPackage();
                    var withdrawalDate = subscriptionPackageModel.getCreatedDate().plusDays(subscriptionPackageModel.getWithdrawalDurationPerDay());
                    if(withdrawalDate.isAfter(LocalDateTime.now()))
                        throw new NotAcceptableException("Withdrawal is allowed only after " + withdrawalDate.toString());
                }
                if(model.getTransactionType().equals(TransactionType.DEPOSIT) && !walletRepository.existsByUserIdAndTransactionTypeAndCurrencyAndActiveTrue(model.getUser().getId(),TransactionType.DEPOSIT,balanceModel.getCurrency())) {
                    BigDecimal referralDepositBonus = referralDepositBonus(model.getAmount());
                    String walletAddress = parameterService.findByCode("WALLET_ADDRESS").getValue();
                    var user = userService.findById(model.getUser().getId());
                    if (get(() -> user.getParent()) != null) {
                        WalletModel bonus1 = new WalletModel();
                        bonus1.setActive(true);
                        bonus1.setUser(user.getParent());
                        bonus1.setAmount(referralDepositBonus(model.getAmount()));
                        bonus1.setAddress(walletAddress);
                        bonus1.setCurrency(balanceModel.getCurrency());
                        bonus1.setTransactionType(TransactionType.BONUS);
                        create(bonus1);
                    }
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
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
    public List<BalanceModel> totalBalanceGroupedByCurrency(long userId) {
        return walletRepository.totalBalanceGroupedByCurrency(userId);
    }
    @Override
    public List<BalanceModel> totalDepositGroupedByCurrency(long userId) {
        return walletRepository.totalDepositGroupedByCurrency(userId);
    }
    @Override
    public List<BalanceModel> totalWithdrawalGroupedByCurrency(long userId) {
        return walletRepository.totalWithdrawalGroupedByCurrency(userId);
    }
    @Override
    public List<BalanceModel> totalBonusGroupedByCurrency(long userId) {
        return walletRepository.totalBonusGroupedByCurrency(userId);
    }
    @Override
    public List<BalanceModel> totalRewardGroupedByCurrency(long userId) {
        return walletRepository.totalRewardGroupedByCurrency(userId);
    }

    @Override
    public List<BalanceModel> totalProfitGroupedByCurrency(long userId) {
        var result = walletRepository.totalProfitGroupedByCurrency(userId);
        return result.stream()
                .map(obj -> new BalanceModel(CurrencyType.valueOf((String) obj[0]),(BigDecimal) obj[1]))
                .collect(Collectors.toList());
    }
}
