package com.eshop.client.service.impl;

import com.eshop.client.entity.QArbitrageEntity;
import com.eshop.client.entity.QWalletEntity;
import com.eshop.client.entity.WalletEntity;
import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.RoleType;
import com.eshop.client.enums.TransactionType;
import com.eshop.client.filter.WalletFilter;
import com.eshop.client.mapping.WalletMapper;
import com.eshop.client.model.BalanceModel;
import com.eshop.client.model.WalletModel;
import com.eshop.client.repository.WalletRepository;
import com.eshop.client.service.SubscriptionPackageService;
import com.eshop.client.service.SubscriptionService;
import com.eshop.client.service.WalletService;
import com.eshop.client.util.DateUtil;
import com.eshop.exception.common.NotFoundException;
import com.eshop.exception.common.PaymentRequiredException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.eshop.client.enums.TransactionType.BONUS;
import static com.eshop.client.util.DateUtil.toLocalDate;

@Service
public class WalletServiceImpl extends BaseServiceImpl<WalletFilter,WalletModel, WalletEntity, Long> implements WalletService {

    private final WalletRepository walletRepository;
    private final SubscriptionService subscriptionService;
    private final JPAQueryFactory queryFactory;

    public WalletServiceImpl(WalletRepository repository, WalletMapper mapper, SubscriptionService subscriptionService, JPAQueryFactory queryFactory) {
        super(repository, mapper);
        this.walletRepository = repository;
        this.subscriptionService = subscriptionService;
        this.queryFactory = queryFactory;
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
        model.setActive(false);
        var result =  super.create(model);
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
    public WalletModel update(WalletModel model) {
        model.setActive(false);
        var result =  super.update(model);
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
    public List<BalanceModel> totalProfitGroupedByCurrency(long userId) {
        var result = walletRepository.totalProfitGroupedByCurrency(userId);
        return result.stream()
                .map(obj -> new BalanceModel(CurrencyType.valueOf((String) obj[0]),(BigDecimal) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, BigDecimal> findAllWithinDateRange(long startDate, long endDate, TransactionType transactionType) {
        QWalletEntity path = QWalletEntity.walletEntity;
        DateTemplate<Date> truncatedDate = Expressions.dateTemplate(Date.class, "date_trunc('day', {0})", path.createdDate);
        var results = queryFactory.select(truncatedDate, path.amount.sum())
                .from(path)
                .where(path.createdDate.between(new Date(startDate),new Date(endDate)))
                .where(path.transactionType.eq(transactionType))
                .groupBy(truncatedDate)
                .fetch();
        Map<Long, BigDecimal> map = results.stream()
                .collect(Collectors.toMap(tuple -> tuple.get(truncatedDate).getTime(),tuple -> tuple.get(path.amount.sum())));

        var allDates = toLocalDate(startDate).datesUntil(toLocalDate(endDate).plusDays(1)).map(DateUtil::toEpoch);

        return allDates.collect(Collectors.toMap(epoch -> epoch, epoch -> map.getOrDefault(epoch, BigDecimal.ZERO)));
    }
}
