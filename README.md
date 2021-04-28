# Mancala Game with Spring Boot

This is a mancala game that has a basic UI and full backend code. Technologies:

* Java 11
* Spring Boot
* Spring MVC
* JQuery
* HTML, CSS, Bootstrap
* Thymeleaf
* JUnit 5
* H2
* Swagger 3

## Build & Run

This is a maven project. So just use maven lifecycle. (*mvn clean compile* to compile)
Since this is a spring boot application, just run MancalaApplication which is annotated with SpringBootApplication.
\
\
The application will run on 8080 port. So to open the home page follow *http://localhost:8080*.
\
\
You will see a button written *Click to Play* on it. Please click it, this will redirect you inside the game, and
you will see the game board. Now you're ready to play, just enjoy!

## Endpoints

There are two REST endpoints in the project:

```
[POST] /mancala/new-game
[POST] /mancala/play
```

*/new-game* endpoint is not being used currently inside UI but if another application integrates to our backend, then it
must call */new-game* endpoint to create an initial board.

Swagger was implemented to test the endpoints easily. You will see the endpoint details on Swagger UI. To open the
Swagger UI, please follow *http://localhost:8080/swagger-ui/*.

## Logging

slf4j library was used for logging.

## Session Management

The session is stored both on Spring, and H2 database. Since H2 is in-memory storage inside the application, after
restarting, the session is removed. So it's not fully functional.

## Unit Tests

Code coverage:\
Class: %95\
Method: %88\
Line: %89


