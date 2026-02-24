package com.baseballcards.service;

// Spring annotations for REST API
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

// Our business layer and models
import com.baseballcards.business.CardBusinessLayer;
import com.baseballcards.models.Card;
import com.baseballcards.models.CardStyle;
import com.baseballcards.models.FindCard;

/**
 * SERVICE LAYER (REST API Controller)
 * 
 * This class creates web endpoints (URLs) that can be accessed over HTTP.
 * Each method here becomes a web service that calls the business layer.
 * 
 * BASE URL: http://localhost:8080/api/cards
 * 
 * ANNOTATIONS EXPLAINED:
 * - @RestController: Tells Spring this class handles web requests
 * - @RequestMapping: Sets the base URL path for all methods in this class
 * - @PostMapping: Handles POST requests (for creating data)
 * - @GetMapping: Handles GET requests (for reading data)
 * - @PutMapping: Handles PUT requests (for updating data)
 * - @DeleteMapping: Handles DELETE requests (for deleting data)
 */
@RestController
@RequestMapping("/api/cards")
public class CardServiceController {

    // This connects us to the business layer
    @Autowired
    private CardBusinessLayer businessLayer;

    // ========================================
    // CARD SERVICES
    // ========================================

    /**
     * SERVICE: ADD A NEW CARD
     * 
     * URL: POST http://localhost:8080/api/cards/add
     * 
     * HOW TO USE: Send a POST request with JSON data like:
     * {
     *   "firstName": "Mike",
     *   "lastName": "Trout",
     *   "height": 74,
     *   "weight": 235,
     *   "position": "OF",
     *   "team": "Angels",
     *   "battingAverage": 0.305
     * }
     * 
     * @param card - The Card object from the JSON request body
     * @return ResponseEntity - HTTP response with success/error message
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCard(@RequestBody Card card) {
        // Call the business layer to add the card
        String result = businessLayer.addCard(card);
        
        // If result starts with "Success", return HTTP 200 (OK)
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            // Otherwise, return HTTP 400 (Bad Request) with error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * SERVICE: GET A CARD BY NAME
     * 
     * URL: GET http://localhost:8080/api/cards/get?firstName=Mike&lastName=Trout
     * 
     * @param firstName - Player's first name (from URL parameter)
     * @param lastName - Player's last name (from URL parameter)
     * @return ResponseEntity - HTTP response with Card data or error
     */
    
    @GetMapping
public ResponseEntity<?> getAllCards() {
    List<Card> cards = businessLayer.getAllCards();
    
    if (cards.isEmpty()) {
        return ResponseEntity.ok("No cards found in database");
    } else {
        return ResponseEntity.ok(cards);
    }
}

    
    @GetMapping("/get")
    public ResponseEntity<?> getCard(
            @RequestParam String firstName, 
            @RequestParam String lastName) {
        
        // Call the business layer to get the card
        Card card = businessLayer.getCard(firstName, lastName);
        
        if (card != null) {
            // Return the card as JSON with HTTP 200
            return ResponseEntity.ok(card);
        } else {
            // Return HTTP 404 (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Card not found for " + firstName + " " + lastName);
        }
    }

    /**
     * SERVICE: UPDATE A CARD
     * 
     * URL: PUT http://localhost:8080/api/cards/update
     * 
     * HOW TO USE: Send a PUT request with JSON data of the card to update
     * 
     * @param card - The Card object with updated information
     * @return ResponseEntity - HTTP response with success/error message
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateCard(@RequestBody Card card) {
        // Call the business layer to update
        String result = businessLayer.updateCard(card);
        
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * SERVICE: DELETE A CARD
     * 
     * URL: DELETE http://localhost:8080/api/cards/delete?firstName=Mike&lastName=Trout
     * 
     * @param firstName - Player's first name
     * @param lastName - Player's last name
     * @return ResponseEntity - HTTP response with success/error message
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCard(
            @RequestParam String firstName, 
            @RequestParam String lastName) {
        
        // Call the business layer to delete
        String result = businessLayer.removeCard(firstName, lastName);
        
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    // ========================================
    // CARD STYLE SERVICES
    // ========================================

    /**
     * SERVICE: ADD A NEW CARD STYLE
     * 
     * URL: POST http://localhost:8080/api/cards/style/add
     * 
     * HOW TO USE: Send a POST request with JSON like:
     * {
     *   "manufacturer": "Topps",
     *   "year": 2023,
     *   "edition": "Series 1"
     * }
     */
    @PostMapping("/style/add")
    public ResponseEntity<String> addCardStyle(@RequestBody CardStyle cardStyle) {
        String result = businessLayer.addCardStyle(cardStyle);
        
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * SERVICE: GET A CARD STYLE BY ID
     * 
     * URL: GET http://localhost:8080/api/cards/style/get?id=1
     */
    @GetMapping("/style/get")
    public ResponseEntity<?> getCardStyle(@RequestParam int id) {
        CardStyle style = businessLayer.getCardStyle(id);
        
        if (style != null) {
            return ResponseEntity.ok(style);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Card style not found with ID " + id);
        }
    }

    /**
     * SERVICE: UPDATE A CARD STYLE
     * 
     * URL: PUT http://localhost:8080/api/cards/style/update
     */
    @PutMapping("/style/update")
    public ResponseEntity<String> updateCardStyle(@RequestBody CardStyle cardStyle) {
        String result = businessLayer.updateCardStyle(cardStyle);
        
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * SERVICE: DELETE A CARD STYLE
     * 
     * URL: DELETE http://localhost:8080/api/cards/style/delete?id=1
     */
    @DeleteMapping("/style/delete")
    public ResponseEntity<String> deleteCardStyle(@RequestParam int id) {
        String result = businessLayer.removeCardStyle(id);
        
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    // ========================================
    // FIND CARD SERVICES
    // ========================================

    /**
     * SERVICE: ADD A FIND CARD ENTRY
     * 
     * URL: POST http://localhost:8080/api/cards/find/add
     * 
     * HOW TO USE: Send a POST request with JSON like:
     * {
     *   "firstName": "Mike",
     *   "lastName": "Trout",
     *   "teamName": "Angels"
     * }
     */
    @PostMapping("/find/add")
    public ResponseEntity<String> addFindCard(@RequestBody FindCard findCard) {
        String result = businessLayer.addFindCard(findCard);
        
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * SERVICE: GET A FIND CARD ENTRY
     * 
     * URL: GET http://localhost:8080/api/cards/find/get?firstName=Mike&lastName=Trout
     */
    @GetMapping("/find/get")
    public ResponseEntity<?> getFindCard(
            @RequestParam String firstName, 
            @RequestParam String lastName) {
        
        FindCard findCard = businessLayer.getFindCard(firstName, lastName);
        
        if (findCard != null) {
            return ResponseEntity.ok(findCard);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Find card entry not found");
        }
    }

    /**
     * SERVICE: UPDATE A FIND CARD ENTRY
     * 
     * URL: PUT http://localhost:8080/api/cards/find/update
     */
    @PutMapping("/find/update")
    public ResponseEntity<String> updateFindCard(@RequestBody FindCard findCard) {
        String result = businessLayer.updateFindCard(findCard);
        
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * SERVICE: DELETE A FIND CARD ENTRY
     * 
     * URL: DELETE http://localhost:8080/api/cards/find/delete?firstName=Mike&lastName=Trout
     */
    @DeleteMapping("/find/delete")
    public ResponseEntity<String> deleteFindCard(
            @RequestParam String firstName, 
            @RequestParam String lastName) {
        
        String result = businessLayer.removeFindCard(firstName, lastName);
        
        if (result.startsWith("Success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    // ========================================
    // HEALTH CHECK SERVICE
    // ========================================

    /**
     * SERVICE: CHECK IF THE API IS RUNNING
     * 
     * URL: GET http://localhost:8080/api/cards/health
     * 
     * This is a simple test endpoint to verify the service is running
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Baseball Card Service is running!");
    }
}