package com.baseballcards.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * SERVICE TEST HARNESS
 * 
 * This is a console-based application that tests all the REST services.
 * It performs CRUD operations (Create, Read, Update, Delete) to verify
 * that all services are working correctly.
 * 
 * HOW TO USE:
 * 1. Make sure your Spring Boot service is running (mvn spring-boot:run)
 * 2. Run this test harness: mvn exec:java -Dexec.mainClass="com.baseballcards.test.ServiceTestHarness"
 * 3. Follow the menu options to test different services
 */
public class ServiceTestHarness {

    // Base URL for all API calls
    private static final String BASE_URL = "http://localhost:8080/api/cards";
    
    // Scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("BASEBALL CARD SERVICE TEST HARNESS");
        System.out.println("========================================\n");

        // First, check if the service is running
        if (!checkServiceHealth()) {
            System.out.println("ERROR: Service is not running!");
            System.out.println("Please start the service first: mvn spring-boot:run");
            return;
        }

        System.out.println("Service is running! ✓\n");

        // Show menu
        while (true) {
            showMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    runFullCRUDTest();
                    break;
                case 2:
                    testAddCard();
                    break;
                case 3:
                    testGetCard();
                    break;
                case 4:
                    testGetAllCards();
                    break;
                case 5:
                    testUpdateCard();
                    break;
                case 6:
                    testDeleteCard();
                    break;
                case 7:
                    testCardStyle();
                    break;
                case 8:
                    testFindCard();
                    break;
                case 0:
                    System.out.println("\nExiting Test Harness. Goodbye!");
                    return;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }

    /**
     * SHOW MENU
     * Displays all available test options
     */
    private static void showMenu() {
        System.out.println("\n========================================");
        System.out.println("TEST MENU");
        System.out.println("========================================");
        System.out.println("1. Run Full CRUD Test (Recommended)");
        System.out.println("2. Test: Add Card");
        System.out.println("3. Test: Get Card by Name");
        System.out.println("4. Test: Get All Cards");
        System.out.println("5. Test: Update Card");
        System.out.println("6. Test: Delete Card");
        System.out.println("7. Test: Card Style Operations");
        System.out.println("8. Test: Find Card Operations");
        System.out.println("0. Exit");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
    }

    /**
     * GET USER CHOICE
     */
    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * CHECK SERVICE HEALTH
     * Tests if the service is running
     */
    private static boolean checkServiceHealth() {
        try {
            String response = sendGetRequest(BASE_URL + "/health");
            return response.contains("running");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * RUN FULL CRUD TEST
     * Tests Create, Read, Update, Delete in sequence
     */
    private static void runFullCRUDTest() {
        System.out.println("\n========================================");
        System.out.println("RUNNING FULL CRUD TEST");
        System.out.println("========================================\n");

        String testFirstName = "TestPlayer";
        String testLastName = "CRUD" + System.currentTimeMillis(); // Unique name

        // STEP 1: CREATE
        System.out.println("STEP 1: CREATE - Adding new card...");
        String createJson = String.format(
            "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"height\":72.0,\"weight\":200.0,\"position\":\"SS\",\"team\":\"TestTeam\",\"battingAverage\":0.300}",
            testFirstName, testLastName
        );
        String createResponse = sendPostRequest(BASE_URL + "/add", createJson);
        System.out.println("Response: " + createResponse);
        
        if (createResponse.contains("Success")) {
            System.out.println("✓ CREATE test PASSED\n");
        } else {
            System.out.println("✗ CREATE test FAILED\n");
            return;
        }

        pause(1);

        // STEP 2: READ
        System.out.println("STEP 2: READ - Retrieving the card...");
        String readResponse = sendGetRequest(
            BASE_URL + "/get?firstName=" + testFirstName + "&lastName=" + testLastName
        );
        System.out.println("Response: " + readResponse);
        
        if (readResponse.contains(testFirstName) && readResponse.contains(testLastName)) {
            System.out.println("✓ READ test PASSED\n");
        } else {
            System.out.println("✗ READ test FAILED\n");
            return;
        }

        pause(1);

        // STEP 3: UPDATE
        System.out.println("STEP 3: UPDATE - Updating the card...");
        String updateJson = String.format(
            "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"height\":73.0,\"weight\":210.0,\"position\":\"SS\",\"team\":\"UpdatedTeam\",\"battingAverage\":0.350}",
            testFirstName, testLastName
        );
        String updateResponse = sendPutRequest(BASE_URL + "/update", updateJson);
        System.out.println("Response: " + updateResponse);
        
        if (updateResponse.contains("Success")) {
            System.out.println("✓ UPDATE test PASSED\n");
        } else {
            System.out.println("✗ UPDATE test FAILED\n");
            return;
        }

        pause(1);

        // STEP 4: READ AGAIN (verify update)
        System.out.println("STEP 4: READ AGAIN - Verifying update...");
        String readResponse2 = sendGetRequest(
            BASE_URL + "/get?firstName=" + testFirstName + "&lastName=" + testLastName
        );
        System.out.println("Response: " + readResponse2);
        
        if (readResponse2.contains("UpdatedTeam") && readResponse2.contains("0.35")) {
            System.out.println("✓ UPDATE VERIFICATION test PASSED\n");
        } else {
            System.out.println("✗ UPDATE VERIFICATION test FAILED\n");
        }

        pause(1);

        // STEP 5: DELETE
        System.out.println("STEP 5: DELETE - Deleting the card...");
        String deleteResponse = sendDeleteRequest(
            BASE_URL + "/delete?firstName=" + testFirstName + "&lastName=" + testLastName
        );
        System.out.println("Response: " + deleteResponse);
        
        if (deleteResponse.contains("Success")) {
            System.out.println("✓ DELETE test PASSED\n");
        } else {
            System.out.println("✗ DELETE test FAILED\n");
            return;
        }

        pause(1);

        // STEP 6: READ AGAIN (verify deletion)
        System.out.println("STEP 6: READ AGAIN - Verifying deletion...");
        String readResponse3 = sendGetRequest(
            BASE_URL + "/get?firstName=" + testFirstName + "&lastName=" + testLastName
        );
        System.out.println("Response: " + readResponse3);
        
        if (readResponse3.contains("not found")) {
            System.out.println("✓ DELETE VERIFICATION test PASSED\n");
        } else {
            System.out.println("✗ DELETE VERIFICATION test FAILED\n");
        }

        System.out.println("\n========================================");
        System.out.println("FULL CRUD TEST COMPLETED!");
        System.out.println("========================================");
    }

    /**
     * TEST ADD CARD
     */
    private static void testAddCard() {
        System.out.println("\n--- TEST: ADD CARD ---");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter height: ");
        double height = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter weight: ");
        double weight = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter position: ");
        String position = scanner.nextLine();
        System.out.print("Enter team: ");
        String team = scanner.nextLine();
        System.out.print("Enter batting average: ");
        double battingAverage = Double.parseDouble(scanner.nextLine());

        String json = String.format(
            "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"height\":%.1f,\"weight\":%.1f,\"position\":\"%s\",\"team\":\"%s\",\"battingAverage\":%.3f}",
            firstName, lastName, height, weight, position, team, battingAverage
        );

        String response = sendPostRequest(BASE_URL + "/add", json);
        System.out.println("\nResponse: " + response);
    }

    /**
     * TEST GET CARD
     */
    private static void testGetCard() {
        System.out.println("\n--- TEST: GET CARD ---");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        String response = sendGetRequest(
            BASE_URL + "/get?firstName=" + firstName + "&lastName=" + lastName
        );
        System.out.println("\nResponse: " + response);
    }

    /**
     * TEST GET ALL CARDS
     */
    private static void testGetAllCards() {
        System.out.println("\n--- TEST: GET ALL CARDS ---");
        String response = sendGetRequest(BASE_URL);
        System.out.println("\nResponse: " + response);
    }

    /**
     * TEST UPDATE CARD
     */
    private static void testUpdateCard() {
        System.out.println("\n--- TEST: UPDATE CARD ---");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter new height: ");
        double height = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter new weight: ");
        double weight = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter new position: ");
        String position = scanner.nextLine();
        System.out.print("Enter new team: ");
        String team = scanner.nextLine();
        System.out.print("Enter new batting average: ");
        double battingAverage = Double.parseDouble(scanner.nextLine());

        String json = String.format(
            "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"height\":%.1f,\"weight\":%.1f,\"position\":\"%s\",\"team\":\"%s\",\"battingAverage\":%.3f}",
            firstName, lastName, height, weight, position, team, battingAverage
        );

        String response = sendPutRequest(BASE_URL + "/update", json);
        System.out.println("\nResponse: " + response);
    }

    /**
     * TEST DELETE CARD
     */
    private static void testDeleteCard() {
        System.out.println("\n--- TEST: DELETE CARD ---");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        String response = sendDeleteRequest(
            BASE_URL + "/delete?firstName=" + firstName + "&lastName=" + lastName
        );
        System.out.println("\nResponse: " + response);
    }

    /**
     * TEST CARD STYLE
     */
    private static void testCardStyle() {
        System.out.println("\n--- TEST: CARD STYLE ---");
        System.out.println("1. Add Card Style");
        System.out.println("2. Get Card Style");
        System.out.print("Choose: ");
        int choice = getUserChoice();

        if (choice == 1) {
            System.out.print("Enter manufacturer: ");
            String manufacturer = scanner.nextLine();
            System.out.print("Enter year: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter edition: ");
            String edition = scanner.nextLine();

            String json = String.format(
                "{\"manufacturer\":\"%s\",\"year\":%d,\"edition\":\"%s\"}",
                manufacturer, year, edition
            );

            String response = sendPostRequest(BASE_URL + "/style/add", json);
            System.out.println("\nResponse: " + response);
        } else if (choice == 2) {
            System.out.print("Enter style ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            String response = sendGetRequest(BASE_URL + "/style/get?id=" + id);
            System.out.println("\nResponse: " + response);
        }
    }

    /**
     * TEST FIND CARD
     */
    private static void testFindCard() {
        System.out.println("\n--- TEST: FIND CARD ---");
        System.out.println("1. Add Find Card Entry");
        System.out.println("2. Get Find Card Entry");
        System.out.print("Choose: ");
        int choice = getUserChoice();

        if (choice == 1) {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter team name: ");
            String teamName = scanner.nextLine();

            String json = String.format(
                "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"teamName\":\"%s\"}",
                firstName, lastName, teamName
            );

            String response = sendPostRequest(BASE_URL + "/find/add", json);
            System.out.println("\nResponse: " + response);
        } else if (choice == 2) {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            String response = sendGetRequest(
                BASE_URL + "/find/get?firstName=" + firstName + "&lastName=" + lastName
            );
            System.out.println("\nResponse: " + response);
        }
    }

    // ========================================
    // HTTP REQUEST HELPER METHODS
    // ========================================

    /**
     * SEND GET REQUEST
     */
    private static String sendGetRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    responseCode < 400 ? conn.getInputStream() : conn.getErrorStream()
                )
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * SEND POST REQUEST
     */
    private static String sendPostRequest(String urlString, String jsonBody) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    responseCode < 400 ? conn.getInputStream() : conn.getErrorStream()
                )
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * SEND PUT REQUEST
     */
    private static String sendPutRequest(String urlString, String jsonBody) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    responseCode < 400 ? conn.getInputStream() : conn.getErrorStream()
                )
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * SEND DELETE REQUEST
     */
    private static String sendDeleteRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    responseCode < 400 ? conn.getInputStream() : conn.getErrorStream()
                )
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * PAUSE (for better readability during tests)
     */
    private static void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}