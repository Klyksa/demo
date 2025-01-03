package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.DatabaseManager;

import java.sql.SQLException;

@Service
public class WalletService {

    @Autowired
    private DatabaseManager databaseManager;

    public double getBalance(long userId) {
        try {
            return databaseManager.getBalance(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void putMoney(long userId, double amount) {
        try {
            databaseManager.putMoney(userId, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void takeMoney(long userId, double amount) {
        try {
            databaseManager.takeMoney(userId, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
