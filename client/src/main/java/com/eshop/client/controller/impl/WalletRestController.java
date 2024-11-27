package com.eshop.client.controller.impl;

import com.eshop.client.enums.TransactionType;
import com.eshop.client.filter.WalletFilter;
import com.eshop.client.model.BalanceModel;
import com.eshop.client.model.WalletModel;
import com.eshop.client.service.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Tag(name = "Wallet Rest Service v1")
@RequestMapping(value = "/api/v1/wallet")
public class WalletRestController extends BaseRestControllerImpl<WalletFilter, WalletModel, Long> {

    private WalletService walletService;

    public WalletRestController(WalletService service) {
        super(service);
        this.walletService = service;
    }
    @GetMapping("/total-balance/{userId}")
    public ResponseEntity<List<BalanceModel>> totalBalanceGroupedByCurrency(@PathVariable UUID userId){
        return ResponseEntity.ok(walletService.totalBalanceGroupedByCurrency(userId));
    }
    @GetMapping("/total-deposit/{userId}")
    public ResponseEntity<List<BalanceModel>> totalDepositGroupedByCurrency(@PathVariable UUID userId){
        return ResponseEntity.ok(walletService.totalDepositGroupedByCurrency(userId));
    }
    @GetMapping("/total-withdrawal/{userId}")
    public ResponseEntity<List<BalanceModel>> totalWithdrawalGroupedByCurrency(@PathVariable UUID userId){
        return ResponseEntity.ok(walletService.totalWithdrawalGroupedByCurrency(userId));
    }
    @GetMapping("/total-bonus/{userId}")
    public ResponseEntity<List<BalanceModel>> totalBonusGroupedByCurrency(@PathVariable UUID userId){
        return ResponseEntity.ok(walletService.totalBonusGroupedByCurrency(userId));
    }
    @GetMapping("/total-reward/{userId}")
    public ResponseEntity<List<BalanceModel>> totalRewardGroupedByCurrency(@PathVariable UUID userId){
        return ResponseEntity.ok(walletService.totalRewardGroupedByCurrency(userId));
    }
    @GetMapping("/total-profit/{userId}")
    public ResponseEntity<List<BalanceModel>> totalProfitGroupedByCurrency(@PathVariable UUID userId){
        return ResponseEntity.ok(walletService.totalProfitGroupedByCurrency(userId));
    }
    @GetMapping("/daily-profit/{userId}")
    public ResponseEntity<List<BalanceModel>> dailyProfitGroupedByCurrency(@PathVariable UUID userId){
        return ResponseEntity.ok(walletService.dailyProfitGroupedByCurrency(userId));
    }
    @GetMapping("/get-date-range/{startDate}/{endDate}/{transactionType}")
    public ResponseEntity<Map<Long, BigDecimal>> findAllWithinDateRange(@PathVariable long startDate, @PathVariable long endDate, @PathVariable TransactionType transactionType){
        return ResponseEntity.ok(walletService.findAllWithinDateRange(startDate,endDate,transactionType));
    }

}
