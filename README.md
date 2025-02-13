# Technical Assessment: Top Scorers Application

## Overview

This project is a solution to a technical assessment focused on parsing CSV data, determining top scorers from test results, integrating with a database, and providing a RESTful API for managing scores. The application was developed in Java with SQLite for local database management and uses Spark Java for the RESTful API.

## Project Structure

- **src/main/java/**: Contains the source code for the main application, database integration, and RESTful API.
  - `TopScorers.java`: Parses CSV and finds top scorers.
  - `DatabaseIntegration.java`: Handles integration with SQLite database.
  - `RESTfulAPI.java`: Implements the RESTful API using Spark Java.
- **TestData.csv**: Sample CSV file with test data.
- **testdata.db**: SQLite database file where CSV data is stored.
- **pom.xml** or **build.gradle**: Build configuration file if using Maven or Gradle respectively.

## Requirements Fulfilled

- CSV Parsing without standard libraries
- Output top scorers to STDOUT
- Database Integration (SQLite)
- RESTful API with endpoints for:
  - POST new scores
  - GET score of a specific person
  - GET top scorers
- Security Considerations (Discussed)
- Cloud Deployment Discussion (Discussed)

## How to Run

### Prerequisites

- Java JDK (version 8 or later)
- SQLite JDBC Driver (Download from [SQLite JDBC](https://github.com/xerial/sqlite-jdbc) and add to classpath)
- (Optional) Maven or Gradle for dependency management

### Running the Application

1. **Java Application (CSV Parsing & Top Scorers):**

   ```bash
   javac src/main/java/TopScorers.java
   java -cp ".:path/to/sqlite-jdbc.jar" TopScorers
