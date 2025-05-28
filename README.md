# Credit Module Challenge

This is a demo Spring Boot application for a case study.

## Tech Stack

- Java 17
- Spring Boot 3.3.5
- RESTful API for loan management
- In-memory H2 Database
- Spring Data JPA for data access
- Lombok, MapStruct, Liquibase to manage database schema

## Project Structure

```
src/main/java/com/eminyagiz/creditmodule/
├── common/
│   └── exception/
│   └── mapper/
│   └── validation/
├── controller/
│   ├── AuthenticationController.java
│   ├── LoanController.java
│   └── UserController.java
├── model/
│   └── dto/
│   └── entity/
├── repository/
│   └── InstallmentRepository.java
│   └── LoanRepository.java
│   └── UserRepository.java
├── security/
│   └── JWTFilter.java
│   └── JWTUtil.java
│   └── SecurityConfiguration.java
├── service/
│   └── impl/
│   └── AuthenticationService.java
│   └── InstallmentService.java
│   └── LoanService.java
│   └── UserService.java
└── CreditModuleApplication.java
```

## Prerequisite

1. Java 17
2. Gradle

## Running the Project

The application will start on port 8082 by default. To start application from the command line:

```bash
./gradlew bootRun
```

- To package as jar and run the jar

```bash
./gradlew bootJar && java -jar build/libs/creditModule-0.0.1-SNAPSHOT.jar
```

- To running tests

```bash
./gradlew clean test
```

## H2 Database Console

The H2 console is enabled and you may accessed at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./creditmodule`
- Username: `sa`
- Password: `password`

## cURL for Postman

### 1. Register
```
curl --location 'localhost:8082/auth/register' \
--data '{
    "username":"user1",
    "customerRole": "CUSTOMER",
    "password": "1234",
    "name": "myname",
    "surname": "mysurname"
}'
```

### 2. Login
```
curl --location 'localhost:8082/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "user1",
    "password": "1234"
}'
```

### 3. Add Credit Limit
```
curl --location --request PUT 'localhost:8082/customer/credit-limit' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <jwt-token>' \
--data '{
"customerId": 1,
"additionalCreditLimit": 100000
}'
```

### 4. Create Loan
```
curl --location 'localhost:8082/loan' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <jwt-token>' \
--data '{
    "numberOfInstallments": 6,
    "customerId": 1,
    "amount": 10000,
    "interestRate": 0.5
}'
```

### 5. Get Loan Installments
```
curl --location 'localhost:8082/loan/1/installment' \
--header 'Authorization: Bearer <jwt-token>'
```

### 6. Get Loan Installments
```
curl --location 'localhost:8082/loan?customerId=1' \
--header 'Authorization: Bearer <jwt-token>'
```

### 7. Pay Loan Installments
```
curl --location --request PUT 'localhost:8082/loan' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <jwt-token>' \
--data '{
"loanId": 1,
"customerId": 8,
"amount": 2500
}'
```