package com.example.service;

import com.example.DatabaseManager;
import com.example.models.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private DatabaseManager databaseManager;

    public double getBalance(long userId) {
        try {
            return databaseManager.getBalance(userId);
        } catch (SQLException e) {
            System.err.println("Ошибка при получении баланса для пользователя с ID: " + userId);
            e.printStackTrace();
            return 0.0;
        }
    }

    public void putMoney(long userId, double amount) {
        try {
            databaseManager.putMoney(userId, amount);
        } catch (SQLException e) {
            System.err.println("Ошибка при внесении денег: " + amount + " для пользователя с ID: " + userId);
            e.printStackTrace();
        }
    }

    public void takeMoney(long userId, double amount) {
        try {
            databaseManager.takeMoney(userId, amount);
        } catch (SQLException e) {
            System.err.println("Ошибка при снятии денег: " + amount + " для пользователя с ID: " + userId);
            e.printStackTrace();
        }
    }

    public List<Operation> getOperationList(long userId, Date startDate, Date endDate) {
        try {
            return databaseManager.getOperationList(userId, startDate, endDate);
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка операций для пользователя с ID: " + userId);
            e.printStackTrace();
            return null;
        }
    }
    public void transferMoney(long fromUserId, long toUserId, double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        try {
            databaseManager.transferMoney(fromUserId, toUserId, amount);
        } catch (SQLException e) {
            throw new Exception("Error occurred during the money transfer: " + e.getMessage(), e);
        }
    }
}
