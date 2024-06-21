# Back_End

This section contains the backend implementation of the project using Spring Boot.

## Project Overview

This project leverages Spring Boot as the backend framework to automate the extraction of crucial information from warehouse floorplan images. Utilizing contour detection, the system identifies and counts aisles and racks within these layouts.

## File Structure

The backend files are organized as follows under `src/main/java`:

- **Controller**: Handles incoming HTTP requests and delegates to service methods.
- **Domain**: Contains domain models or DTOs used throughout the application.
- **Entity**: Represents entities that are persisted in the database.
- **Repository**: Implements data access operations and queries.
- **Service**: Implements business logic and coordinates tasks.
- **WarehouseVisionLayoutParserApplication.java**: Entry point for the Spring Boot application.

## Running the Application

To run the backend application, follow these steps:

1. Clone this repository to your local machine.
2. Open the project in IntelliJ, Eclipse, or any other IDE of your choice.
3. Navigate to the `WarehouseVisionLayoutParserApplication.java` file.
4. Run the `main` method in `WarehouseVisionLayoutParserApplication.java` to start the Spring Boot application.

## Dependencies

The backend project utilizes dependencies managed by Maven, specified in the `pom.xml` file.

## Screenshots

Aisle_Table

