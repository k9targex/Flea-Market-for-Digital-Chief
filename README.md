[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=k9targex_FleaMarket&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=k9targex_FleaMarket)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=k9targex_FleaMarket&metric=bugs)](https://sonarcloud.io/summary/new_code?id=k9targex_FleaMarket)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=k9targex_FleaMarket&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=k9targex_FleaMarket)

# FleaMarket Application for DigitalChief

## Overview

FleaMarket Application is a RESTful web service built with Spring Boot for managing sellers and their products in a marketplace scenario. It leverages Spring Data JPA to interact with a PostgreSQL database, providing endpoints to perform CRUD operations on sellers and their associated products.

## Features

### SellerService

#### Endpoints

- **GET /api/sellers**:
  - Retrieves all sellers from the database.
  
- **DELETE /api/sellers/{username}**:
  - Deletes a seller by their username.
  
- **POST /api/sellers**:
  - Creates a new seller with the provided username.
  
- **PUT /api/sellers/{oldUsername}**:
  - Changes the username of an existing seller.

- **GET /api/sellers/{seller}/products**:
  - Retrieves all products associated with a seller.

- **POST /api/sellers/{seller}/products**:
  - Adds a new product to a seller.
  
- **DELETE /api/sellers/{seller}/products/{productName}**:
  - Deletes a product from a seller's inventory.
  
- **PUT /api/sellers/{seller}/products/{oldProductName}**:
  - Changes the name of a product associated with a seller.

#### Exceptions

- `SellerNotFoundException`: Thrown when a seller with the given username is not found.
- `SellerTakenException`: Thrown when attempting to create a seller with a username that is already taken.
- `ProductNotFoundException`: Thrown when a product with the given name or ID does not exist.
- `ProductTakenException`: Thrown when attempting to add a product that already exists for a seller.

### ProductService

#### Endpoints

- **GET /api/products/{productId}/seller**:
  - Retrieves the seller associated with a product given its ID.

#### Exceptions

- `ProductNotFoundException`: Thrown when the product with the given ID is not found.

## Setup

### Prerequisites

- Java 17
- Maven 3.9.6 and higher
- PostgreSQL
### Clone the Repository

1. Clone the repository to your local machine:

```sh
git clone https://github.com/k9targex/FleaMarket.git
```
2.  Navigate into the cloned repository:
```sh
cd fleamarket
```

### Build and Run
#### Build and Run Script

To simplify the build and run process, execute the following steps:

1. Navigate to the root directory of your project.
2. Run the `fleamarket_start.bat` script located in the main project directory. This script performs the following tasks:
- Creates the PostgreSQL database `FleaMarket`.
- Fills the `username` and `password` fields in `application.properties`.
- Builds the project using Maven.
- Starts the application.
```sh
fleamarket_start.bat
```


#### Manual Build and Run

If you prefer to build and run manually:
1. Configure the database connection in `src/main/resources/application.properties`:
```sh
spring.datasource.username={username}
spring.datasource.password={password}
```
2. Create the PostgreSQL database FleaMarket.
3. Open a terminal or command prompt.
4. Navigate to the root directory of your project.


#### Build the Application

3. Build the project using Maven:

```sh
mvn clean install
```

This command compiles the code, executes tests, and packages the application into a JAR file.

#### Run the Application
4. Make sure that database FleaMarket is created.

5. Start the application using Maven:directory:
```sh
mvn spring-boot:run
```
6. Alternatively, you can run the JAR file generated in the target/ directory:
```sh
java -jar target/fleamarket-0.0.1-SNAPSHOT.jar
```


### Troubleshooting
1. Check the console output for any error messages if the application fails to start.
2. Verify the correctness of database connection settings in application.properties.
3. Ensure Java 17 and Maven are correctly installed and configured on your system.


### API Documentation and Testing
#### Swagger
Swagger is integrated into this application to provide interactive API documentation. To access Swagger UI:
1. Start the application.
2. Open your web browser and navigate to
```sh
 http://localhost:8080/swagger-ui/index.html#/
 ```

This interface allows you to explore the API endpoints, view request and response formats, and execute requests directly from the browser.

#### Unit Tests
The application includes comprehensive unit tests covering 100% of the business logic. Run the tests using:
```sh
mvn test
```
![Unit Tests](Coverage.png)



### Postman
A Postman collection is provided to facilitate testing the API endpoints. Follow these steps to use it:

1. Import the Postman collection:
- Open Postman.
- Click on the Import button.
- Select the provided Postman collection file `FleaMarket.postman_collection.json`
2. Use the imported collection to send requests to your running application. The collection includes predefined requests for all major functionalities of the application.

    This setup allows you to quickly test and interact with the API, ensuring that all endpoints work as expected.




  ## License
  This project is licensed under the MIT License. See the LICENSE file for more information.
## Author
This application was developed by Mozheiko Dmitry.
