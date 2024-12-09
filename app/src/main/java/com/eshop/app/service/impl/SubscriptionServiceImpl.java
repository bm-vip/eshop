package com.eshop.app.service.impl;

import com.eshop.app.entity.QSubscriptionEntity;
import com.eshop.app.entity.SubscriptionEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.entity.WalletEntity;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.enums.TransactionType;
import com.eshop.app.filter.SubscriptionFilter;
import com.eshop.app.mapping.SubscriptionMapper;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.repository.SubscriptionPackageRepository;
import com.eshop.app.repository.SubscriptionRepository;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.ParameterService;
import com.eshop.app.service.SubscriptionService;
import com.eshop.app.service.UserService;
import com.eshop.app.util.DateUtil;
import com.eshop.exception.common.BadRequestException;
import com.eshop.exception.common.NotFoundException;
import com.eshop.exception.common.PaymentRequiredException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.eshop.app.service.ParameterService.walletAddress;
import static com.eshop.app.util.MapperHelper.get;

@Service
public class SubscriptionServiceImpl extends BaseServiceImpl<SubscriptionFilter,SubscriptionModel, SubscriptionEntity, Long> implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPackageRepository subscriptionPackageRepository;
    private final UserService userService;
    private final WalletRepository walletRepository;
    private final ParameterService parameterService;

    public SubscriptionServiceImpl(SubscriptionRepository repository, SubscriptionMapper mapper, SubscriptionPackageRepository subscriptionPackageRepository, UserService userService, WalletRepository walletRepository, ParameterService parameterService) {
        super(repository, mapper);
        this.subscriptionRepository = repository;
        this.subscriptionPackageRepository = subscriptionPackageRepository;
        this.userService = userService;
        this.walletRepository = walletRepository;
        this.parameterService = parameterService;
    }
    @Override
    public JpaRepository<SubscriptionEntity,Long> getRepository() {
        return subscriptionRepository;
    }

    @Override
    public Predicate queryBuilder(SubscriptionFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QSubscriptionEntity path = QSubscriptionEntity.subscriptionEntity;

        if(!RoleType.hasRole(RoleType.ADMIN)) {
            builder.and(path.user.roles.any().role.ne(RoleType.ADMIN));
        }

        filter.getId().ifPresent(v -> builder.and(path.id.eq(v)));
        filter.getUserId().ifPresent(v -> builder.and(path.user.id.eq(v)));
        filter.getSubscriptionPackageId().ifPresent(v -> builder.and(path.subscriptionPackage.id.eq(v)));
        filter.getExpireDateFrom().ifPresent(v -> builder.and(path.expireDate.goe(DateUtil.toLocalDateTime(v))));
        filter.getExpireDateTo().ifPresent(v -> builder.and(path.expireDate.loe(DateUtil.toLocalDateTime(v))));
        filter.getDiscountPercentage().ifPresent(v -> builder.and(path.discountPercentage.eq(v)));
        filter.getFinalPriceFrom().ifPresent(v -> builder.and(path.finalPrice.goe(v)));
        filter.getFinalPriceTo().ifPresent(v -> builder.and(path.finalPrice.loe(v)));
        filter.getStatus().ifPresent(v -> builder.and(path.status.eq(v)));

        return builder;
    }

    @Override
    @Transactional
    public SubscriptionModel create(SubscriptionModel model){
        var entity = mapper.toEntity(model);
        var subscriptionPackage = subscriptionPackageRepository.findById(model.getSubscriptionPackage().getId()).orElseThrow(()-> new NotFoundException("No such subscriptionPackage with " + model.getSubscriptionPackage().getId()));

        entity.setStatus(EntityStatusType.Pending);
        if(subscriptionPackage.getDuration() <= 0)
            entity.setExpireDate(DateUtil.toLocalDateTime(4102444800000L));
        else {
            entity.setExpireDate(LocalDateTime.now().plusDays(subscriptionPackage.getDuration()));
        }
        entity.setFinalPrice(calculatePrice(subscriptionPackage.getPrice(), model.getDiscountPercentage()));
        if(model.getStatus().equals(EntityStatusType.Active)) {
            var balanceList = walletRepository.totalBalanceGroupedByCurrencyByUserId(entity.getUser().getId());
            var balance = balanceList.stream().filter(x->x.getCurrency().equals(subscriptionPackage.getCurrency())).findFirst();
            if(balance.isEmpty())
                throw new PaymentRequiredException();
            if(entity.getFinalPrice().compareTo(balance.get().getTotalAmount()) > 0)
                throw new PaymentRequiredException();
            deactivateOldActive(entity.getUser().getId());
            entity.setStatus(EntityStatusType.Active);
            addBonus(entity);
        }
        return mapper.toModel(subscriptionRepository.save(entity));
    }
    @Override
    @Transactional
    public SubscriptionModel update(SubscriptionModel model){
        var entity = subscriptionRepository.findById(model.getId()).orElseThrow(()-> new NotFoundException("No such subscription with " + model.getId()));
        if(entity.getStatus().equals(EntityStatusType.Active))
            throw new BadRequestException("subscription with Active status could not be update");

        if(model.getDiscountPercentage() != null)
            entity.setDiscountPercentage(model.getDiscountPercentage());
        if (get(()->model.getUser().getId()) != null)
            entity.setUser(new UserEntity().setUserId(model.getUser().getId()));

        if(!get(()-> model.getSubscriptionPackage().getId()).equals(entity.getSubscriptionPackage().getId())) {//subscription package changed
            var subscriptionPackage = subscriptionPackageRepository.findById(model.getSubscriptionPackage().getId()).orElseThrow(()-> new NotFoundException("No such subscriptionPackage with " + model.getSubscriptionPackage().getId()));
            entity.setSubscriptionPackage(subscriptionPackage);
            if(entity.getSubscriptionPackage().getDuration() <= 0)
                entity.setExpireDate(DateUtil.toLocalDateTime(4102444800000L));
            else {
                LocalDate.now().plusDays(entity.getSubscriptionPackage().getDuration());//The remaining days of previous subscription will be burned
            }
            BigDecimal finalPrice = calculatePrice(entity.getSubscriptionPackage().getPrice(), model.getDiscountPercentage());
            entity.setFinalPrice(finalPrice);
            entity.setStatus(EntityStatusType.Passive);
        }
        if(model.getStatus().equals(EntityStatusType.Active)) {
            var balanceList = walletRepository.totalBalanceGroupedByCurrencyByUserId(entity.getUser().getId());
            var balance = balanceList.stream().filter(x->x.getCurrency().equals(entity.getSubscriptionPackage().getCurrency())).findFirst();
            if(balance.isEmpty())
                throw new PaymentRequiredException();
            if(entity.getFinalPrice().compareTo(balance.get().getTotalAmount()) > 0)
                throw new PaymentRequiredException();
            deactivateOldActive(entity.getUser().getId());
            entity.setStatus(EntityStatusType.Active);
            addBonus(entity);
        }
        return mapper.toModel(subscriptionRepository.save(entity));
    }
    private BigDecimal calculatePrice(BigDecimal originalPrice, Integer discountPercentage) {
        if(discountPercentage == null)
            return originalPrice;
        var discountAmount = originalPrice.multiply(new BigDecimal(discountPercentage).divide(new BigDecimal("100")));
        return originalPrice.subtract(discountAmount);
    }
    private void deactivateOldActive(UUID userId) {
        var userModel = userService.findById(userId);
        var oldActive = subscriptionRepository.findByUserIdAndStatus(userModel.getId(), EntityStatusType.Active);
        if(oldActive != null) {
            oldActive.setStatus(EntityStatusType.Passive);
            subscriptionRepository.saveAndFlush(oldActive);
        }
    }

    @Override
    public SubscriptionModel findByUserAndActivePackage(UUID userId) {
        return mapper.toModel(subscriptionRepository.findByUserIdAndStatus(userId, EntityStatusType.Active));
    }

    private void addBonus(SubscriptionEntity entity) {
        var walletAddressValue = parameterService.findByCode(walletAddress).getValue();
//        var selfReferralBonus = entity.getSubscriptionPackage().getSelfReferralBonus();
//        WalletEntity selfWallet = new WalletEntity();
//        selfWallet.setActive(true);
//        selfWallet.setAmount(new BigDecimal(selfReferralBonus));
//        selfWallet.setUser(entity.getUser());
//        selfWallet.setCurrency(entity.getSubscriptionPackage().getCurrency());
//        selfWallet.setTransactionType(TransactionType.BONUS);
//        selfWallet.setAddress(walletAddressValue);
//        walletRepository.save(selfWallet);

        if(entity.getUser().getParent() != null) {
            var parentReferralBonus = entity.getSubscriptionPackage().getParentReferralBonus();
            WalletEntity parentWallet = new WalletEntity();
            parentWallet.setActive(true);
            parentWallet.setAmount(new BigDecimal(parentReferralBonus));
            parentWallet.setUser(entity.getUser().getParent());
            parentWallet.setCurrency(entity.getSubscriptionPackage().getCurrency());
            parentWallet.setTransactionType(TransactionType.BONUS);
            parentWallet.setAddress(walletAddressValue);
            walletRepository.save(parentWallet);
        }
    }
}
