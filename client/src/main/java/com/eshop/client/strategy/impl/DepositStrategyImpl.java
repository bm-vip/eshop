package com.eshop.client.strategy.impl;

import com.eshop.client.service.SubscriptionPackageService;
import com.eshop.client.enums.EntityStatusType;
import com.eshop.client.model.WalletModel;
import com.eshop.client.strategy.TransactionStrategy;
import com.eshop.exception.common.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositStrategyImpl implements TransactionStrategy {
    private final SubscriptionPackageService subscriptionPackageService;

    @Override
    public void execute(WalletModel model) {
        var sp = subscriptionPackageService.findMatchedPackageByAmount(model.getAmount());
        if (sp == null)
            throw new BadRequestException("Please deposit at least the amount of the first subscription.");
        model.setStatus(EntityStatusType.Pending);
    }
}
