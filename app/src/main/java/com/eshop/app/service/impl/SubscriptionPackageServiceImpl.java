package com.eshop.app.service.impl;

import com.eshop.app.entity.QSubscriptionPackageEntity;
import com.eshop.app.entity.SubscriptionPackageEntity;
import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.filter.SubscriptionFilter;
import com.eshop.app.filter.SubscriptionPackageFilter;
import com.eshop.app.mapping.SubscriptionPackageMapper;
import com.eshop.app.mapping.UserMapper;
import com.eshop.app.model.BalanceModel;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.model.SubscriptionPackageModel;
import com.eshop.app.model.UserModel;
import com.eshop.app.repository.SubscriptionPackageRepository;
import com.eshop.app.repository.UserRepository;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.SubscriptionPackageService;
import com.eshop.exception.common.BadRequestException;
import com.eshop.exception.common.NotAcceptableException;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SubscriptionPackageServiceImpl extends BaseServiceImpl<SubscriptionPackageFilter,SubscriptionPackageModel, SubscriptionPackageEntity, Long> implements SubscriptionPackageService {

    private final SubscriptionPackageRepository subscriptionPackageRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final SubscriptionServiceImpl subscriptionService;
    private final UserMapper userMapper;

    public SubscriptionPackageServiceImpl(SubscriptionPackageRepository repository, SubscriptionPackageMapper mapper, WalletRepository walletRepository, UserRepository userRepository, SubscriptionServiceImpl subscriptionService, UserMapper userMapper) {
        super(repository, mapper);
        this.subscriptionPackageRepository = repository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
        this.userMapper = userMapper;
    }

    @Override
    public SubscriptionPackageModel create(SubscriptionPackageModel model) {
        var result = super.create(model);
        userRepository.findAll().forEach(user -> {
            var balance = walletRepository.totalBalanceGroupedByCurrency(user.getId());
            var balanceOptional = balance.stream().filter(x->x.getCurrency().equals(model.getCurrency())).findAny();
            if (balanceOptional.isPresent()) {
                var balanceModel = balanceOptional.get();
                var currentSubscription = subscriptionService.findByUserAndActivePackage(user.getId());
                var subscriptionPackage = findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(), balanceModel.getCurrency());
                if (currentSubscription == null || (subscriptionPackage != null && !currentSubscription.getSubscriptionPackage().getId().equals(subscriptionPackage.getId()))) {
                    subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(userMapper.toModel(user)).setStatus(EntityStatusType.Active));
                }
            }
        });
        return result;
    }

    @Override
    public SubscriptionPackageModel update(SubscriptionPackageModel model) {
        var entity = repository.findById(model.getId()).orElseThrow(() -> new NotFoundException(String.format("%s not found by id %d", model.getClass().getName(), model.getId().toString())));

        if(model.getPrice().compareTo(entity.getPrice())!=0 || model.getMaxPrice().compareTo(entity.getMaxPrice())!=0) {
            SubscriptionFilter subscriptionFilter = new SubscriptionFilter();
            subscriptionFilter.setSubscriptionPackageId(model.getId());
            if (subscriptionService.exists(subscriptionFilter))
                throw new NotAcceptableException("Subscription package already taken by another user, you can't update it");
        }
        return mapper.toModel(repository.save(mapper.updateEntity(model, entity)));
    }

    @Override
    public Predicate queryBuilder(SubscriptionPackageFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QSubscriptionPackageEntity path = QSubscriptionPackageEntity.subscriptionPackageEntity;

        filter.getId().ifPresent(v -> builder.and(path.id.eq(v)));
        filter.getName().ifPresent(v -> builder.and(path.name.toLowerCase().contains(v.toLowerCase())));
        filter.getDuration().ifPresent(v -> builder.and(path.duration.eq(v)));
        filter.getOrderCount().ifPresent(v -> builder.and(path.orderCount.eq(v)));
        filter.getPrice().ifPresent(v -> builder.and(path.price.eq(v)));
        filter.getPriceFrom().ifPresent(v -> builder.and(path.price.goe(v)));
        filter.getPriceTo().ifPresent(v -> builder.and(path.price.loe(v)));
        filter.getMaxPrice().ifPresent(v -> builder.and(path.maxPrice.eq(v)));
        filter.getMaxPriceFrom().ifPresent(v -> builder.and(path.maxPrice.goe(v)));
        filter.getMaxPriceTo().ifPresent(v -> builder.and(path.maxPrice.loe(v)));
        filter.getCurrency().ifPresent(v -> builder.and(path.currency.eq(v)));
        filter.getStatus().ifPresent(v -> builder.and(path.status.eq(v)));
        filter.getDescription().ifPresent(v -> builder.and(path.description.toLowerCase().contains(v.toLowerCase())));
        filter.getMinTradingReward().ifPresent(v -> builder.and(path.minTradingReward.eq(v)));
        filter.getMinTradingRewardFrom().ifPresent(v -> builder.and(path.minTradingReward.goe(v)));
        filter.getMinTradingRewardTo().ifPresent(v -> builder.and(path.minTradingReward.loe(v)));
        filter.getMaxTradingReward().ifPresent(v -> builder.and(path.maxTradingReward.eq(v)));
        filter.getMaxTradingRewardFrom().ifPresent(v -> builder.and(path.maxTradingReward.goe(v)));
        filter.getMaxTradingRewardTo().ifPresent(v -> builder.and(path.maxTradingReward.loe(v)));
        filter.getParentReferralBonus().ifPresent(v -> builder.and(path.parentReferralBonus.eq(v)));
        filter.getParentReferralBonusFrom().ifPresent(v -> builder.and(path.parentReferralBonus.goe(v)));
        filter.getParentReferralBonusTo().ifPresent(v -> builder.and(path.parentReferralBonus.loe(v)));
        filter.getWithdrawalDurationPerDay().ifPresent(v -> builder.and(path.withdrawalDurationPerDay.eq(v)));
        filter.getUserProfitPercentage().ifPresent(v -> builder.and(path.userProfitPercentage.eq(v)));
        filter.getSiteProfitPercentage().ifPresent(v -> builder.and(path.siteProfitPercentage.eq(v)));

        return builder;
    }

    @Override
    public SubscriptionPackageModel findMatchedPackageByAmountAndCurrency(BigDecimal amount, CurrencyType currency) {
        var result = subscriptionPackageRepository.findMatchedPackageByAmountAndCurrency(amount, currency);
        if(result.isEmpty()) {
            var lastPackage = subscriptionPackageRepository.findTopByOrderByMaxPriceDesc();
            if(amount.compareTo(lastPackage.getMaxPrice()) >= 0)
                return mapper.toModel(lastPackage);
            return null;
        }
        return mapper.toModel(result.get());
    }
}
