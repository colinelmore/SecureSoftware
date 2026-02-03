# Baseball Card Tracker

## Overview
The Baseball Card Tracker is a Java application designed to help users manage and keep track of their baseball card collections. It provides functionalities to create, read, update, and delete information related to baseball cards, their styles, and search functionalities.

## Project Structure
The project is organized into the following main directories:

- **src/main/java/com/baseballcards**: Contains the main application code.
  - **dao**: Contains the Data Access Object (DAO) classes for handling database operations.
  - **models**: Contains the model classes representing the data structure of baseball cards.
  - **Main.java**: The entry point of the application.

- **src/main/resources**: Contains configuration files.
  - **application.properties**: Configuration for database connection.

- **src/test/java/com/baseballcards**: Contains test classes for the application.
  - **dao**: Contains tests for the DAO classes.

- **database**: Contains SQL scripts for database setup and test data.
  - **create_tables.sql**: SQL script to create the necessary tables.
  - **insert_test_data.sql**: SQL script to insert test data into the tables.
  - **schema.sql**: Schema definition for the database.

- **pom.xml**: Maven configuration file for managing project dependencies and build settings.

## Features
- **CRUD Operations**: The application supports Create, Read, Update, and Delete operations for baseball cards, card styles, and find card functionalities.
- **Database Integration**: Utilizes a relational database to store and manage data.
- **Test Coverage**: Includes unit tests to ensure the functionality of the DAO classes.

## Setup Instructions
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```
   cd baseball-card-tracker
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```
4. Configure the database connection in `src/main/resources/application.properties`.
5. Run the application:
   ```
   mvn exec:java -Dexec.mainClass="com.baseballcards.Main"
   ```

## Usage
- Add new baseball cards, styles, and search for cards using the provided functionalities in the application.
- Modify the SQL scripts in the `database` directory to customize the database schema and test data as needed.

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.