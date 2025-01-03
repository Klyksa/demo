package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.service.WalletService;

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
}
