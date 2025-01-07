package com.example;

import com.example.config.DatasourceProperties;
import com.example.models.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseManager {
    private Connection connection;

    @Autowired
    public DatabaseManager(DatasourceProperties datasourceProperties) {
        try {
            connection = DriverManager.getConnection(
                    datasourceProperties.getUrl(),
                    datasourceProperties.getUsername(),
                    datasourceProperties.getPassword()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(long userId) throws SQLException {
        String query = "SELECT balance FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        }
        return 0.0;
    }

    public void putMoney(long userId, double amount) throws SQLException {
        String updateBalanceQuery = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
        String insertOperationQuery = "INSERT INTO operations (user_id, operation_type, amount, created_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceQuery);
             PreparedStatement insertOperationStmt = connection.prepareStatement(insertOperationQuery)) {

            connection.setAutoCommit(false);

            updateBalanceStmt.setDouble(1, amount);
            updateBalanceStmt.setLong(2, userId);
            updateBalanceStmt.executeUpdate();

            insertOperationStmt.setLong(1, userId);
            insertOperationStmt.setInt(2, 1); // 1 = Deposit
            insertOperationStmt.setDouble(3, amount);
            insertOperationStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            insertOperationStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void takeMoney(long userId, double amount) throws SQLException {
        String updateBalanceQuery = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
        String insertOperationQuery = "INSERT INTO operations (user_id, operation_type, amount, created_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceQuery);
             PreparedStatement insertOperationStmt = connection.prepareStatement(insertOperationQuery)) {

            connection.setAutoCommit(false);

            updateBalanceStmt.setDouble(1, amount);
            updateBalanceStmt.setLong(2, userId);
            updateBalanceStmt.executeUpdate();

            insertOperationStmt.setLong(1, userId);
            insertOperationStmt.setInt(2, 2); // 2 = Withdrawal
            insertOperationStmt.setDouble(3, amount);
            insertOperationStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            insertOperationStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void transferMoney(long fromUserId, long toUserId, double amount) throws SQLException {
        String checkBalanceQuery = "SELECT balance FROM users WHERE user_id = ?";
        String updateBalanceQuery = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
        String insertOperationQuery = "INSERT INTO operations (user_id, target_user_id, operation_type, amount, created_at) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement checkBalanceStmt = connection.prepareStatement(checkBalanceQuery);
             PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceQuery);
             PreparedStatement insertOperationStmt = connection.prepareStatement(insertOperationQuery)) {

            connection.setAutoCommit(false);

            // Check balance of sender
            checkBalanceStmt.setLong(1, fromUserId);
            ResultSet resultSet = checkBalanceStmt.executeQuery();
            if (resultSet.next() && resultSet.getDouble("balance") < amount) {
                throw new SQLException("Insufficient funds for user ID: " + fromUserId);
            }

            // Deduct money from sender
            updateBalanceStmt.setDouble(1, -amount);
            updateBalanceStmt.setLong(2, fromUserId);
            updateBalanceStmt.executeUpdate();

            // Add money to recipient
            updateBalanceStmt.setDouble(1, amount);
            updateBalanceStmt.setLong(2, toUserId);
            updateBalanceStmt.executeUpdate();

            // Log operation for sender
            insertOperationStmt.setLong(1, fromUserId);
            insertOperationStmt.setLong(2, toUserId);
            insertOperationStmt.setInt(3, 3); // 3 = Transfer
            insertOperationStmt.setDouble(4, -amount);
            insertOperationStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            insertOperationStmt.executeUpdate();

            // Log operation for recipient
            insertOperationStmt.setLong(1, toUserId);
            insertOperationStmt.setLong(2, fromUserId);
            insertOperationStmt.setInt(3, 3); // 3 = Transfer
            insertOperationStmt.setDouble(4, amount);
            insertOperationStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            insertOperationStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Operation> getOperationList(long userId, Date startDate, Date endDate) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM operations WHERE user_id = ?");
        List<Operation> operations = new ArrayList<>();

        if (startDate != null) {
            query.append(" AND created_at >= ?");
        }
        if (endDate != null) {
            query.append(" AND created_at <= ?");
        }

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            statement.setLong(1, userId);
            int index = 2;
            if (startDate != null) {
                statement.setDate(index++, startDate);
            }
            if (endDate != null) {
                statement.setDate(index, endDate);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Operation operation = new Operation();
                operation.setId(resultSet.getLong("id"));
                operation.setUserId(resultSet.getLong("user_id"));
                operation.setOperationType(resultSet.getInt("operation_type"));
                operation.setAmount(resultSet.getDouble("amount"));
                operation.setCreatedAt(resultSet.getTimestamp("created_at"));
                operations.add(operation);
            }
        }

        return operations;
    }
}
