package com.baseballcards.business;

// Import the data layer and models
import com.baseballcards.dao.CardDAO;
import com.baseballcards.models.Card;
import com.baseballcards.models.CardStyle;
import com.baseballcards.models.FindCard;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BUSINESS LAYER
 * 
 * This class contains business logic for managing baseball cards.
 * It sits between the Service Layer (which we'll create later) and the Data Layer (CardDAO).
 * 
 * PURPOSE: Add validation, business rules, and error handling before calling the database.
 */
public class CardBusinessLayer {

    // This is our connection to the Data Layer
    private CardDAO cardDAO;

    /**
     * CONSTRUCTOR
     * 
     * When we create a CardBusinessLayer object, we pass in a database connection.
     * This connection is then used to create a CardDAO object.
     * 
     * @param connection - The database connection
     */
    public CardBusinessLayer(Connection connection) {
        this.cardDAO = new CardDAO(connection);
    }

    // ========================================
    // BUSINESS METHODS FOR CARD
    // ========================================

    /**
     * ADD A NEW BASEBALL CARD
     * 
     * This method adds a new baseball card to the database.
     * Before adding, it checks if the card data is valid (business logic).
     * 
     * @param card - The Card object to add
     * @return String - Success message or error message
     */
    public String addCard(Card card) {
        try {
            // BUSINESS LOGIC: Validate the card before adding
            if (card.getFirstName() == null || card.getFirstName().trim().isEmpty()) {
                return "Error: First name cannot be empty";
            }
            if (card.getLastName() == null || card.getLastName().trim().isEmpty()) {
                return "Error: Last name cannot be empty";
            }
            if (card.getBattingAverage() < 0 || card.getBattingAverage() > 1.0) {
                return "Error: Batting average must be between 0 and 1.0";
            }

            // If validation passes, call the Data Layer to add the card
            cardDAO.createCard(card);
            return "Success: Card added for " + card.getFirstName() + " " + card.getLastName();

        } catch (SQLException e) {
            // If something goes wrong with the database, return an error message
            return "Error: Could not add card - " + e.getMessage();
        }
    }

    /**
     * GET A BASEBALL CARD
     * 
     * This method retrieves a baseball card from the database by name.
     * 
     * @param firstName - Player's first name
     * @param lastName - Player's last name
     * @return Card - The Card object, or null if not found
     */
    public Card getCard(String firstName, String lastName) {
        try {
            // BUSINESS LOGIC: Check if names are provided
            if (firstName == null || firstName.trim().isEmpty()) {
                System.out.println("Error: First name cannot be empty");
                return null;
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                System.out.println("Error: Last name cannot be empty");
                return null;
            }

            // Call the Data Layer to get the card
            Card card = cardDAO.readCard(firstName, lastName);
            
            if (card == null) {
                System.out.println("No card found for " + firstName + " " + lastName);
            }
            
            return card;

        } catch (SQLException e) {
            System.out.println("Error: Could not retrieve card - " + e.getMessage());
            return null;
        }
    }

    /**
     * UPDATE A BASEBALL CARD
     * 
     * This method updates an existing baseball card in the database.
     * 
     * @param card - The Card object with updated information
     * @return String - Success message or error message
     */
    public String updateCard(Card card) {
        try {
            // BUSINESS LOGIC: Validate before updating
            if (card.getFirstName() == null || card.getFirstName().trim().isEmpty()) {
                return "Error: First name cannot be empty";
            }
            if (card.getLastName() == null || card.getLastName().trim().isEmpty()) {
                return "Error: Last name cannot be empty";
            }
            if (card.getBattingAverage() < 0 || card.getBattingAverage() > 1.0) {
                return "Error: Batting average must be between 0 and 1.0";
            }

            // Check if card exists before updating
            Card existingCard = cardDAO.readCard(card.getFirstName(), card.getLastName());
            if (existingCard == null) {
                return "Error: Card not found for " + card.getFirstName() + " " + card.getLastName();
            }

            // Call the Data Layer to update
            cardDAO.updateCard(card);
            return "Success: Card updated for " + card.getFirstName() + " " + card.getLastName();

        } catch (SQLException e) {
            return "Error: Could not update card - " + e.getMessage();
        }
    }

    /**
     * REMOVE A BASEBALL CARD
     * 
     * This method deletes a baseball card from the database.
     * 
     * @param firstName - Player's first name
     * @param lastName - Player's last name
     * @return String - Success message or error message
     */
    public String removeCard(String firstName, String lastName) {
        try {
            // BUSINESS LOGIC: Validate names
            if (firstName == null || firstName.trim().isEmpty()) {
                return "Error: First name cannot be empty";
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                return "Error: Last name cannot be empty";
            }

            // Check if card exists before deleting
            Card existingCard = cardDAO.readCard(firstName, lastName);
            if (existingCard == null) {
                return "Error: Card not found for " + firstName + " " + lastName;
            }

            // Call the Data Layer to delete
            cardDAO.deleteCard(firstName, lastName);
            return "Success: Card removed for " + firstName + " " + lastName;

        } catch (SQLException e) {
            return "Error: Could not delete card - " + e.getMessage();
        }
    }

    /**
 * GET ALL BASEBALL CARDS
 * 
 * This method retrieves all baseball cards from the database.
 * 
 * @return List<Card> - List of all cards, or empty list if none found
 */
public List<Card> getAllCards() {
    try {
        // Call the Data Layer to get all cards
        List<Card> cards = cardDAO.readAllCards();
        
        if (cards.isEmpty()) {
            System.out.println("No cards found in database");
        }
        
        return cards;

    } catch (SQLException e) {
        System.out.println("Error: Could not retrieve cards - " + e.getMessage());
        return new ArrayList<>(); // Return empty list on error
    }
}
    
    // ========================================
    // BUSINESS METHODS FOR CARD STYLE
    // ========================================

    /**
     * ADD A NEW CARD STYLE
     * 
     * @param cardStyle - The CardStyle object to add
     * @return String - Success message or error message
     */
    public String addCardStyle(CardStyle cardStyle) {
        try {
            // BUSINESS LOGIC: Validate card style
            if (cardStyle.getManufacturer() == null || cardStyle.getManufacturer().trim().isEmpty()) {
                return "Error: Manufacturer cannot be empty";
            }
            if (cardStyle.getYear() < 1800 || cardStyle.getYear() > 2100) {
                return "Error: Year must be between 1800 and 2100";
            }

            // Call the Data Layer
            cardDAO.createCardStyle(cardStyle);
            return "Success: Card style added for " + cardStyle.getManufacturer();

        } catch (SQLException e) {
            return "Error: Could not add card style - " + e.getMessage();
        }
    }

    /**
     * GET A CARD STYLE
     * 
     * @param id - The card style ID
     * @return CardStyle - The CardStyle object, or null if not found
     */
    public CardStyle getCardStyle(int id) {
        try {
            // BUSINESS LOGIC: Validate ID
            if (id <= 0) {
                System.out.println("Error: ID must be greater than 0");
                return null;
            }

            // Call the Data Layer
            CardStyle style = cardDAO.readCardStyle(id);
            
            if (style == null) {
                System.out.println("No card style found with ID " + id);
            }
            
            return style;

        } catch (SQLException e) {
            System.out.println("Error: Could not retrieve card style - " + e.getMessage());
            return null;
        }
    }

    /**
     * UPDATE A CARD STYLE
     * 
     * @param cardStyle - The CardStyle object with updated information
     * @return String - Success message or error message
     */
    public String updateCardStyle(CardStyle cardStyle) {
        try {
            // BUSINESS LOGIC: Validate
            if (cardStyle.getManufacturer() == null || cardStyle.getManufacturer().trim().isEmpty()) {
                return "Error: Manufacturer cannot be empty";
            }
            if (cardStyle.getYear() < 1800 || cardStyle.getYear() > 2100) {
                return "Error: Year must be between 1800 and 2100";
            }

            // Call the Data Layer
            cardDAO.updateCardStyle(cardStyle);
            return "Success: Card style updated";

        } catch (SQLException e) {
            return "Error: Could not update card style - " + e.getMessage();
        }
    }

    /**
     * REMOVE A CARD STYLE
     * 
     * @param id - The card style ID
     * @return String - Success message or error message
     */
    public String removeCardStyle(int id) {
        try {
            // BUSINESS LOGIC: Validate ID
            if (id <= 0) {
                return "Error: ID must be greater than 0";
            }

            // Call the Data Layer
            cardDAO.deleteCardStyle(id);
            return "Success: Card style removed";

        } catch (SQLException e) {
            return "Error: Could not delete card style - " + e.getMessage();
        }
    }

    // ========================================
    // BUSINESS METHODS FOR FIND CARD
    // ========================================

    /**
     * ADD A FIND CARD ENTRY
     * 
     * @param findCard - The FindCard object to add
     * @return String - Success message or error message
     */
    public String addFindCard(FindCard findCard) {
        try {
            // BUSINESS LOGIC: Validate
            if (findCard.getFirstName() == null || findCard.getFirstName().trim().isEmpty()) {
                return "Error: First name cannot be empty";
            }
            if (findCard.getLastName() == null || findCard.getLastName().trim().isEmpty()) {
                return "Error: Last name cannot be empty";
            }
            if (findCard.getTeamName() == null || findCard.getTeamName().trim().isEmpty()) {
                return "Error: Team name cannot be empty";
            }

            // Call the Data Layer
            cardDAO.createFindCard(findCard);
            return "Success: Find card entry added for " + findCard.getFirstName() + " " + findCard.getLastName();

        } catch (SQLException e) {
            return "Error: Could not add find card entry - " + e.getMessage();
        }
    }

    /**
     * GET A FIND CARD ENTRY
     * 
     * @param firstName - Player's first name
     * @param lastName - Player's last name
     * @return FindCard - The FindCard object, or null if not found
     */
    public FindCard getFindCard(String firstName, String lastName) {
        try {
            // BUSINESS LOGIC: Validate names
            if (firstName == null || firstName.trim().isEmpty()) {
                System.out.println("Error: First name cannot be empty");
                return null;
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                System.out.println("Error: Last name cannot be empty");
                return null;
            }

            // Call the Data Layer
            FindCard findCard = cardDAO.readFindCard(firstName, lastName);
            
            if (findCard == null) {
                System.out.println("No find card entry for " + firstName + " " + lastName);
            }
            
            return findCard;

        } catch (SQLException e) {
            System.out.println("Error: Could not retrieve find card - " + e.getMessage());
            return null;
        }
    }

    /**
     * UPDATE A FIND CARD ENTRY
     * 
     * @param findCard - The FindCard object with updated information
     * @return String - Success message or error message
     */
    public String updateFindCard(FindCard findCard) {
        try {
            // BUSINESS LOGIC: Validate
            if (findCard.getFirstName() == null || findCard.getFirstName().trim().isEmpty()) {
                return "Error: First name cannot be empty";
            }
            if (findCard.getLastName() == null || findCard.getLastName().trim().isEmpty()) {
                return "Error: Last name cannot be empty";
            }
            if (findCard.getTeamName() == null || findCard.getTeamName().trim().isEmpty()) {
                return "Error: Team name cannot be empty";
            }

            // Check if exists
            FindCard existing = cardDAO.readFindCard(findCard.getFirstName(), findCard.getLastName());
            if (existing == null) {
                return "Error: Find card entry not found";
            }

            // Call the Data Layer
            cardDAO.updateFindCard(findCard);
            return "Success: Find card entry updated";

        } catch (SQLException e) {
            return "Error: Could not update find card - " + e.getMessage();
        }
    }

    /**
     * REMOVE A FIND CARD ENTRY
     * 
     * @param firstName - Player's first name
     * @param lastName - Player's last name
     * @return String - Success message or error message
     */
    public String removeFindCard(String firstName, String lastName) {
        try {
            // BUSINESS LOGIC: Validate
            if (firstName == null || firstName.trim().isEmpty()) {
                return "Error: First name cannot be empty";
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                return "Error: Last name cannot be empty";
            }

            // Check if exists
            FindCard existing = cardDAO.readFindCard(firstName, lastName);
            if (existing == null) {
                return "Error: Find card entry not found";
            }

            // Call the Data Layer
            cardDAO.deleteFindCard(firstName, lastName);
            return "Success: Find card entry removed";

        } catch (SQLException e) {
            return "Error: Could not delete find card - " + e.getMessage();
        }
    }
}