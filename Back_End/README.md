# Back_End

This section contains the backend implementation of the project using Spring Boot.

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

Aisles_Table
---
![Table for Aisles](https://github.com/RincisM/Warehouse_Vision_Layout_Parser/blob/ed1a5643e5894304d01295ceef3d74d13ed669fd/Screenshots/aisles_table.png)

Images_Table
---
![Table for Images](https://github.com/RincisM/Warehouse_Vision_Layout_Parser/blob/58f3c6930508a926982efb2069d4a9af3697f15c/Screenshots/image_table.png)

Postman_Query
---
![Sample Query for the Application using Postman](https://github.com/RincisM/Warehouse_Vision_Layout_Parser/blob/58f3c6930508a926982efb2069d4a9af3697f15c/Screenshots/postman_query.png)

JSON Result
---
![Sample JSON Result](https://github.com/RincisM/Warehouse_Vision_Layout_Parser/blob/58f3c6930508a926982efb2069d4a9af3697f15c/Screenshots/Sample_JSON_result.png)

