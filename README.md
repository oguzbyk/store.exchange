
# Stock Exchange Management System

This is a Spring Boot-based application for managing stock exchanges and stocks. The application provides a RESTful API for creating, updating, deleting, and retrieving stock exchanges and stocks. It also includes user authentication and authorization features.

## Features

- Create, update, delete, and retrieve stock exchanges.
- Create, update, delete, and retrieve stocks.
- Manage associations between stocks and stock exchanges.
- User authentication and authorization.
- API documentation with Swagger.

## Technologies Used

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database
- Maven
- Docker
- MapStruct
- Lombok
- Mockito and JUnit 5 for testing

## Getting Started

### Prerequisites

- Java 17
- Maven 3.8+
- Docker (optional, for running the application with Docker)

### Installation

1. Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2. Navigate to the project directory:
    ```bash
    cd stock.exchange
    ```
3. Build the project using Maven:
    ```bash
    ./mvnw clean install
    ```

### Running the Application

You can run the application in different environments:

#### Running Locally

1. Use Maven to run the application:
    ```bash
    ./mvnw spring-boot:run
    ```
2. The application will start on `http://localhost:8080`.

#### Running with Docker

1. Build the Docker image:
    ```bash
    docker build -t stock-exchange-app .
    ```
2. Run the Docker container:
    ```bash
    docker-compose up
    ```
3. The application will be available on `http://localhost:8080`.

### API Documentation

Once the application is running, you can access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### Database

The application uses an H2 in-memory database for development and testing. The database console can be accessed at:
```
http://localhost:8080/h2-console
```
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** `password`

### Login Credentials

Use the following credentials to log in to the application:

- **Username:** `admin`
- **Password:** `admin123`

### Project Structure

- **`src/main/java/com/inghub/stock/exchange`**: Contains the main application code.
- **`src/main/resources`**: Contains the application properties and other resources.
- **`docker-compose.yml`**: Docker Compose file for running the application with Docker.
- **`pom.xml`**: Maven POM file with project dependencies and build configurations.

### Running Tests

To run the tests, use the following command:
```bash
./mvnw test
```

### Developer Notes

- This application is configured to use Spring Security for authentication and authorization.
- The application uses MapStruct for mapping between DTOs and entities.
- Mockito and JUnit 5 are used for writing unit tests.

## License

This project is licensed under the MIT License.
