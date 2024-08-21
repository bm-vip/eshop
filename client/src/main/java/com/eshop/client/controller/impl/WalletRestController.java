package com.eshop.client.controller.impl;

import com.eshop.client.filter.WalletFilter;
import com.eshop.client.model.BalanceModel;
import com.eshop.client.model.WalletModel;
import com.eshop.client.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Wallet Rest Service v1")
@RequestMapping(value = "/api/v1/wallet")
public class WalletRestController extends BaseRestControllerImpl<WalletFilter, WalletModel, Long> {

    private WalletService walletService;

    public WalletRestController(WalletService service) {
        super(service, WalletFilter.class);
        this.walletService = service;
    }
    @GetMapping("/total-balance/{userId}")
    public ResponseEntity<List<BalanceModel>> findBalanceGroupedByCurrency(@PathVariable long userId){
        return ResponseEntity.ok(walletService.findBalanceGroupedByCurrency(userId));
    }
    @GetMapping("/total-deposit/{userId}")
    public ResponseEntity<List<BalanceModel>> findDepositGroupedByCurrency(@PathVariable long userId){
        return ResponseEntity.ok(walletService.findDepositGroupedByCurrency(userId));
    }
    @GetMapping("/total-withdrawal/{userId}")
    public ResponseEntity<List<BalanceModel>> findWithdrawalGroupedByCurrency(@PathVariable long userId){
        return ResponseEntity.ok(walletService.findWithdrawalGroupedByCurrency(userId));
    }
    @GetMapping("/total-bonus/{userId}")
    public ResponseEntity<List<BalanceModel>> findBonusGroupedByCurrency(@PathVariable long userId){
        return ResponseEntity.ok(walletService.findBonusGroupedByCurrency(userId));
    }

}
