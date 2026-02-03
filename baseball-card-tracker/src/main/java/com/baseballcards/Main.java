package com.baseballcards;

import com.baseballcards.dao.CardDAO;
import com.baseballcards.models.Card;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        // Database connection setup for PostgreSQL
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"; // Replace with your database name
        String dbUser = "postgres"; // Replace with your database username
        String dbPassword = "NewStrongPassword"; // Replace with your database password


        try {
            //Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            CardDAO cardDAO = new CardDAO(connection);

                        // Create a new Card
            Card newCard = new Card("Mike", "brown", 6.3f, 220f, "First Baseman", "St. Louis Cardinals", 0.305);
            

            cardDAO.createCard(newCard);
            System.out.println("Card created successfully!");

        

        
            // Update a Card
            
            System.out.print("Enter Card ID to update: ");
            System.out.print("Enter new first name: ");
            newCard.setFirstName("Carson");
            System.out.print("Enter new last name: ");
            newCard.setLastName("Matthews");
            System.out.print("Enter new height: ");
            newCard.setHeight(6);
            System.out.print("Enter new weight: ");
            newCard.setWeight(190);
            System.out.print("Enter new position: ");
            newCard.setPosition("Pitcher");
            //System.out.print("Enter new team: ");
            newCard.setTeam("Atlanta Braves");
            System.out.print("Enter new batting average: ");
            newCard.setBattingAverage(.326);

            cardDAO.updateCard(newCard);
            System.out.println("Card updated successfully!");
                        

                        
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}