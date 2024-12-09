package com.eshop.app.controller.impl;

import com.eshop.app.enums.TransactionType;
import com.eshop.app.filter.WalletFilter;
import com.eshop.app.model.BalanceModel;
import com.eshop.app.model.WalletModel;
import com.eshop.app.service.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @GetMapping("/total-balance")
    public ResponseEntity<List<BalanceModel>> totalBalanceGroupedByCurrency(){
        return ResponseEntity.ok(walletService.totalBalanceGroupedByCurrency());
    }
    @GetMapping("/total-deposit")
    public ResponseEntity<List<BalanceModel>> totalDepositGroupedByCurrency(){
        return ResponseEntity.ok(walletService.totalDepositGroupedByCurrency());
    }
    @GetMapping("/total-withdrawal")
    public ResponseEntity<List<BalanceModel>> totalWithdrawalGroupedByCurrency(){
        return ResponseEntity.ok(walletService.totalWithdrawalGroupedByCurrency());
    }
    @GetMapping("/total-bonus")
    public ResponseEntity<List<BalanceModel>> totalBonusGroupedByCurrency(){
        return ResponseEntity.ok(walletService.totalBonusGroupedByCurrency());
    }
    @GetMapping("/total-reward")
    public ResponseEntity<List<BalanceModel>> totalRewardGroupedByCurrency(){
        return ResponseEntity.ok(walletService.totalRewardGroupedByCurrency());
    }
    @GetMapping("/get-date-range/{startDate}/{endDate}/{transactionType}")
    public ResponseEntity<Map<Long, BigDecimal>> findAllWithinDateRange(@PathVariable long startDate, @PathVariable long endDate, @PathVariable TransactionType transactionType){
        return ResponseEntity.ok(walletService.findAllWithinDateRange(startDate,endDate,transactionType));
    }
}
