#Project: jakarta-book

[![SonarCloud](
https://sonarcloud.io/api/project_badges/measure?project=ValentinaSukonina_jakarta_books&metric=alert_status)

## Description
This project involves developing a RESTful Web service using the JAX-RS API, with a specific focus on movie-related data. The service will be deployed on a WildFly application server, utilizing the Jakarta EE platform.
## Docker

To build the docker image:

```
mvn clean package
docker build --tag=jakarta-labb .