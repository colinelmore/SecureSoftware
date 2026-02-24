package com.baseballcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MAIN APPLICATION CLASS
 * 
 * This is the entry point for the Spring Boot application.
 * When you run this class, it starts the web server and makes
 * all your REST services available.
 * 
 * @SpringBootApplication tells Spring Boot to:
 * - Automatically configure the application
 * - Scan for components (like @RestController)
 * - Start the embedded web server
 */
@SpringBootApplication
public class Application {

    /**
     * MAIN METHOD
     * 
     * This is where the application starts.
     * It launches the Spring Boot application and starts the web server.
     */
    public static void main(String[] args) {
        // Start Spring Boot
        SpringApplication.run(Application.class, args);
        
        System.out.println("\n========================================");
        System.out.println("Baseball Card Service Started!");
        System.out.println("========================================");
        System.out.println("API Base URL: http://localhost:8080/api/cards");
        System.out.println("Health Check: http://localhost:8080/api/cards/health");
        System.out.println("========================================\n");
    }
}