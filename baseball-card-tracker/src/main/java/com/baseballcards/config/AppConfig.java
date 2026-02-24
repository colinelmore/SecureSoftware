package com.baseballcards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import com.baseballcards.business.CardBusinessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * CONFIGURATION CLASS
 * 
 * This class configures the application, specifically the database connection.
 * Spring Boot will automatically use this configuration when starting up.
 * 
 * @Configuration tells Spring this class contains configuration settings
 * @Bean tells Spring to create and manage these objects
 */
@Configuration
public class AppConfig {

    // These values come from application.properties file
    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${database.password}")
    private String databasePassword;

    /**
     * CREATE DATABASE CONNECTION
     * 
     * This method creates a connection to the PostgreSQL database.
     * Spring will call this once when the application starts.
     * 
     * @return Connection - Database connection object
     */
    @Bean
    public Connection databaseConnection() throws SQLException {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            
            // Create and return connection
            return DriverManager.getConnection(
                databaseUrl, 
                databaseUsername, 
                databasePassword
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }
    }

    /**
     * CREATE BUSINESS LAYER
     * 
     * This method creates the CardBusinessLayer object.
     * Spring will automatically inject the database connection.
     * 
     * @param connection - Database connection (Spring provides this automatically)
     * @return CardBusinessLayer - The business layer object
     */
    @Bean
    public CardBusinessLayer cardBusinessLayer(Connection connection) {
        return new CardBusinessLayer(connection);
    }
}