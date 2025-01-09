package com.example.service;

import com.example.models.Operation;
import com.example.models.User;
import com.example.repositories.OperationRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationRepository operationRepository;

    public double getBalance(long userId) {
        return userRepository.findById(userId).map(User::getBalance).orElse(0.0);
    }

    @Transactional
    public void putMoney(long userId, double amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Operation operation = new Operation();
        operation.setUserId(userId);
        operation.setOperationType(1); // 1 = Deposit
        operation.setAmount(amount);
        operation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        operationRepository.save(operation);
    }

    @Transactional
    public void takeMoney(long userId, double amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        Operation operation = new Operation();
        operation.setUserId(userId);
        operation.setOperationType(2); // 2 = Withdrawal
        operation.setAmount(amount);
        operation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        operationRepository.save(operation);
    }

    @Transactional
    public void transferMoney(long fromUserId, long toUserId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        User sender = userRepository.findById(fromUserId).orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User recipient = userRepository.findById(toUserId).orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);
        userRepository.save(sender);
        userRepository.save(recipient);

        Operation senderOperation = new Operation();
        senderOperation.setUserId(fromUserId);
        senderOperation.setOperationType(3); // 3 = Transfer
        senderOperation.setAmount(-amount);
        senderOperation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        operationRepository.save(senderOperation);

        Operation recipientOperation = new Operation();
        recipientOperation.setUserId(toUserId);
        recipientOperation.setOperationType(3); // 3 = Transfer
        recipientOperation.setAmount(amount);
        recipientOperation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        operationRepository.save(recipientOperation);
    }

    public List<Operation> getOperationList(long userId, Date startDate, Date endDate) {
        Timestamp startTimestamp = startDate != null ? new Timestamp(startDate.getTime()) : new Timestamp(0);
        Timestamp endTimestamp = endDate != null ? new Timestamp(endDate.getTime()) : new Timestamp(System.currentTimeMillis());
        return operationRepository.findByUserIdAndCreatedAtBetween(userId, startTimestamp, endTimestamp);
    }
}