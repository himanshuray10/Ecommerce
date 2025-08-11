# Loan Account API

Spring Boot application that integrates with an external Loan Account API, persists responses to an H2 database via JPA, and exposes a GET endpoint.

## Requirements
- Java 17+
- Maven 3.9+

## Run locally
```bash
mvn clean spring-boot:run
```

Alternatively, build a jar:
```bash
mvn -DskipTests package
java -jar target/loan-api-0.0.1-SNAPSHOT.jar
```

## API
- GET `/api/loanaccounts/{loanAccountNumber}`
  - Calls upstream `https://demo9993930.mockable.io/loanaccount/{loanAccountNumber}`
  - Persists response into H2
  - Returns JSON:
    ```json
    {
      "loanAccountNumber": "123456",
      "dueDate": "2023-06-15",
      "emiAmount": 2500
    }
    ```

## Config
- H2 console: `/h2-console` (JDBC URL `jdbc:h2:file:./data/loan-db`)
- Override upstream base URL via `application.yml` property `integration.loan.base-url`

## Project structure
- `controller` — REST API
- `service` — business logic and persistence orchestration
- `integration` — external API client
- `model` — JPA entity
- `repository` — Spring Data JPA repository
- `dto` — API DTOs

## Notes
- Logging added at controller, service, and integration layers.
- JPA auto DDL is enabled for quick setup. For production, use migrations.