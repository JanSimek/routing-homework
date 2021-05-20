[![Build Actions Status](https://github.com/JanSimek/routing-homework/actions/workflows/build.yaml/badge.svg)](https://github.com/JanSimek/routing-homework/actions) [![codebeat badge](https://codebeat.co/badges/9c45fb2e-ddd6-49e8-a43d-339a80831fe8)](https://codebeat.co/projects/github-com-jansimek-routing-homework-master)

# Routing Homework

### Getting started
To run automated tests execute the following command:

```mvn test```

To start the application run:

```mvn spring-boot:run```

Or use the provided wrapper script.

### Documentation

The service provides a single endpoint `/routing/AAA/BBB` where AAA and BBB
are three-letter country codes defined in ISO 3166-1 for which you can calculate a possible land route.

For example, calling `curl -X GET http://localhost:8080/routing/CZE/RUS` will return the following route:

```json
{
    "route": ["CZE","POL","RUS"]
}
```

#### Api documentation
Automatically generated Swagger docs are available at:

* **Swagger UI:** 
  * http://localhost:8080/swagger-ui.html
* **JSON schema:** 
  * http://localhost:8080/v3/api-docs 
