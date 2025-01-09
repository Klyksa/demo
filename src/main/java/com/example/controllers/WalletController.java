package com.example.controllers;

import com.example.models.Operation;
import com.example.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance/{userId}")
    public double getBalance(@PathVariable Long userId) {
        return walletService.getBalance(userId);
    }

    @PostMapping("/deposit")
    public String depositMoney(@RequestParam Long userId, @RequestParam double amount) {
        walletService.putMoney(userId, amount);
        return "Deposit successful!";
    }

    @PostMapping("/withdraw")
    public String withdrawMoney(@RequestParam Long userId, @RequestParam double amount) {
        walletService.takeMoney(userId, amount);
        return "Withdrawal successful!";
    }

    @PostMapping("/transfer")
    public String transferMoney(@RequestParam Long fromUserId, @RequestParam Long toUserId, @RequestParam double amount) {
        walletService.transferMoney(fromUserId, toUserId, amount);
        return "Transfer successful!";
    }

    @GetMapping("/operations")
    public List<Operation> getOperationList(@RequestParam Long userId, @RequestParam(required = false) Date startDate, @RequestParam(required = false) Date endDate) {
        return walletService.getOperationList(userId, startDate, endDate);
    }
}
