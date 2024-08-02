## PROJECT: jakarta-book

[![Jakarta_labb CI pipeline](https://github.com/ValentinaSukonina/jakarta_books/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/ValentinaSukonina/jakarta_books/actions/workflows/maven.yml)
[![SonarCloud](https://github.com/ValentinaSukonina/jakarta_books/actions/workflows/sonar.yml/badge.svg?branch=main)](https://github.com/ValentinaSukonina/jakarta_books/actions/workflows/sonar.yml)
![GitHub top language](https://img.shields.io/github/languages/top/ValentinaSukonina/jakarta_books?label=Java&color=red)
![GitHub language count](https://img.shields.io/github/languages/count/ValentinaSukonina/jakarta_books?color=yellow)


## DESCRIPTION
- This project involves developing a RESTful Web service using the JAX-RS API.
- The service will be deployed on a WildFly application server, utilizing the Jakarta EE platform.

## INSTRUCTIONS
- **To run application in Docker Desktop**
    - Download project.
    - Download and start Docker Desktop
    - Run command in terminal:
        - "mvn clean package"
        - "docker-compose up"
    - Application is now started in a docker-container.
    - Use Postman or Insomnia to run http methods GET(all), POST on URI:
        - http://localhost:8080/api/books
    - Run GET(for single book), PUT, DELETE on URI:
        - http://localhost:8080/api/books/ {id from GET http://localhost:8080/api/books }

- **To run IntegrationTests in testcontainers**
    - Download project
    - Download and start Docker Desktop
    - Run command in terminal:
        - "mvn clean package"
    - Run BookIT-class