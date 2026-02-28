package com.baseballcards.dao;

import com.baseballcards.models.Card;
import com.baseballcards.models.CardStyle;
import com.baseballcards.models.FindCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {

    private Connection connection;

    public CardDAO(Connection connection) {
        this.connection = connection;
    }

    // CRUD operations for Card
    public void createCard(Card card) throws SQLException {
        String sql = "INSERT INTO Card (first_Name, last_Name, height, weight, position, team, batting_Average) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, card.getFirstName());
            pstmt.setString(2, card.getLastName());
            pstmt.setDouble(3, card.getHeight());
            pstmt.setDouble(4, card.getWeight());
            pstmt.setString(5, card.getPosition());
            pstmt.setString(6, card.getTeam());
            pstmt.setDouble(7, card.getBattingAverage());
            pstmt.executeUpdate();
        }
    }

    public Card readCard(String firstName, String lastName) throws SQLException {
        String sql = "SELECT * FROM Card WHERE first_Name = ? AND last_Name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Card(
                    rs.getString("first_Name"),
                    rs.getString("last_Name"),
                    rs.getDouble("height"),
                    rs.getDouble("weight"),
                    rs.getString("position"),
                    rs.getString("team"),
                    rs.getDouble("batting_Average")
                );
            }
        }
        return null;
    }

    public void updateCard(Card card) throws SQLException {
        String sql = "UPDATE Card SET height = ?, weight = ?, position = ?, team = ?, batting_Average = ? WHERE first_Name = ? AND last_Name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, card.getHeight());
            pstmt.setDouble(2, card.getWeight());
            pstmt.setString(3, card.getPosition());
            pstmt.setString(4, card.getTeam());
            pstmt.setDouble(5, card.getBattingAverage());
            pstmt.setString(6, card.getFirstName());
            pstmt.setString(7, card.getLastName());
            pstmt.executeUpdate();
        }
    }

    public void deleteCard(String firstName, String lastName) throws SQLException {
        String sql = "DELETE FROM Card WHERE first_Name = ? AND last_Name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.executeUpdate();
        }
    }

    // Get ALL cards from database
public List<Card> readAllCards() throws SQLException {
    String sql = "SELECT * FROM Card";
    List<Card> cards = new ArrayList<>();
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Card card = new Card(
                rs.getString("first_Name"),
                rs.getString("last_Name"),
                rs.getDouble("height"),
                rs.getDouble("weight"),
                rs.getString("position"),
                rs.getString("team"),
                rs.getDouble("batting_Average")
            );
            cards.add(card);
        }
    }
    return cards;
}
    

    // CRUD operations for CardStyle
    public void createCardStyle(CardStyle cardStyle) throws SQLException {
        String sql = "INSERT INTO CardStyle (manufacturer, year, edition) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cardStyle.getManufacturer());
            pstmt.setInt(2, cardStyle.getYear());
            pstmt.setString(3, cardStyle.getEdition());
            pstmt.executeUpdate();
        }
    }

    public CardStyle readCardStyle(int id) throws SQLException {
        String sql = "SELECT * FROM CardStyle WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new CardStyle(
                    rs.getString("manufacturer"),
                    rs.getInt("year"),
                    rs.getString("edition")
                );
            }
        }
        return null;
    }

    public void updateCardStyle(CardStyle cardStyle) throws SQLException {
        String sql = "UPDATE CardStyle SET manufacturer = ?, year = ?, edition = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cardStyle.getManufacturer());
            pstmt.setInt(2, cardStyle.getYear());
            pstmt.setString(3, cardStyle.getEdition());
            pstmt.executeUpdate();
        }
    }

    public void deleteCardStyle(int id) throws SQLException {
        String sql = "DELETE FROM CardStyle WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // CRUD operations for FindCard
    public void createFindCard(FindCard findCard) throws SQLException {
        String sql = "INSERT INTO FindCard (first_Name, last_Name, team_Name) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, findCard.getFirstName());
            pstmt.setString(2, findCard.getLastName());
            pstmt.setString(3, findCard.getTeamName());
            pstmt.executeUpdate();
        }
    }

    public FindCard readFindCard(String firstName, String lastName) throws SQLException {
        String sql = "SELECT * FROM FindCard WHERE first_Name = ? AND last_Name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new FindCard(
                    rs.getString("first_Name"),
                    rs.getString("last_Name"),
                    rs.getString("team_Name")
                );
            }
        }
        return null;
    }

    public void updateFindCard(FindCard findCard) throws SQLException {
        String sql = "UPDATE FindCard SET team_Name = ? WHERE first_Name = ? AND last_Name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, findCard.getTeamName());
            pstmt.setString(2, findCard.getFirstName());
            pstmt.setString(3, findCard.getLastName());
            pstmt.executeUpdate();
        }
    }

    public void deleteFindCard(String firstName, String lastName) throws SQLException {
        String sql = "DELETE FROM FindCard WHERE first_Name = ? AND last_Name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.executeUpdate();
        }
    }
    
}