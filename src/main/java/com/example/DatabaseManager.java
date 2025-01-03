package com.example;

import com.example.config.DatasourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String query = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }
    }

    public void takeMoney(long userId, double amount) throws SQLException {
        String query = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }
    }
}
