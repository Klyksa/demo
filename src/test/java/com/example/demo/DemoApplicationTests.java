package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "13421342")) {
			Statement statement = connection.createStatement();

			// Очистка данных перед тестом
			statement.executeUpdate("DELETE FROM operations");
			statement.executeUpdate("DELETE FROM users");

			// Добавление тестовых пользователей
			statement.executeUpdate("INSERT INTO users (user_id, balance) VALUES (1, 100.0)");
			statement.executeUpdate("INSERT INTO users (user_id, balance) VALUES (2, 50.0)");
		}
	}
}
	/*@Test
	public void testTransferMoneyAPI() throws Exception {
		mockMvc.perform(post("/api/wallet/transfer")
						.param("fromUserId", "1")
						.param("toUserId", "2")
						.param("amount", "50.0"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Transfer successful")));
	}*\

