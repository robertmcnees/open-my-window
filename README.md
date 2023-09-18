open-my-window seeks to provide the answer to a seemingly simple question.  "Should I open my window?"  At times the nights and mornings can be cool but temperatures will spike during the day.  open-my-window helps inform you if the air outside is worth bringing indoors for some free and all natural cooling.

# Overview

The project is largely intended to be a personal sandbox for exploration with various tech.  Things may get a bit over-engineered at times in the spirit of learning new things.  I will start with what I want to learn and then try to find a place for it inside this project.

The entry point to the project is through the ``arbiter`` module, specifically through the endpoint ``/window/{country_code}/{postal_code}``.  ``arbiter`` will call ``geocode`` to get the coordinates for the postal code and then use those coordinates to call ``forecast`` to obtain the weather data.  ``arbiter`` is responsible for parsing that data and making the final determination if your should open your window.  

Outside calls are made to the [OpenWeather API](https://openweathermap.org/api).  You will need your own API key if you wish to run the project yourself.  I am using the [One Call API](https://openweathermap.org/api/one-call-3).  

# Running

This application is designed to run either locally or on [Azure Spring Apps](https://azure.microsoft.com/en-us/services/spring-apps/).  The project contains 2 applications, `configserver` and `discoveryserver` that are only required when running locally and should not be deployed to ASA.  Details are below.

# Service Communication

## Local - Inject URL as Value

When running locally and not in ASA, we need to specify the `geocodeserviceurl` as our local machine will not know how to decipher the URL `http://geocode/...`.  One way to inject this value is by using a command line parameter when running locally, `-Dgeocodeserviceurl=localhost:8081`

## Local - Run a local Discovery Server
When running the project in ASA, the service discovery server will be provided automatically and we can assume that it is available.  When running locally, we need to stand up our own discover server.  For this purpose the `discoveryserver` application is provided in the repository.  Note this deployment is only needed locally and is not required in ASA. 

# External Configuration

`aribter` pulls configuration from a [GitHub repository](https://github.com/robertmcnees/open-my-window-config) specifically designed to hold externalized configuration.  This is accomplished by including the config starter in the `pom.xml`.
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

## Local

When running locally we need to provide our own config server.  The application `configserver` is provided for this purpose.  To run `arbiter` locally and use the local `configserver`, use the 'local' profile when running `arbiter`.  This will load the `application-local.properties` file that has the location of the local configserver.

# Deploying to Azure Spring Apps


After creating the application on ASA you can deploy by specifying the name of the applicatio you just created path the the Spring Boot .jar.  You will use [az spring app deploy](https://learn.microsoft.com/en-us/cli/azure/spring/app?view=azure-cli-latest#az-spring-app-deploy).  This is another sample based on the app create command above.
```
az spring app deploy --name forecast --artifact-path ./target/forecast-0.0.1-SNAPSHOT.jar --jvm-options="-Xms2048m -Xmx2048m"
```
## GitHub Actions

The Azure Spring Apps deployment can be automated by a GitHub Action.  [Here is an example](https://github.com/robertmcnees/open-my-window/actions/workflows/deploy-arbiter.yml) of deploying the `arbiter` application to ASA.  Documentation can be found on [GitHub](https://github.com/marketplace/actions/azure-spring-apps) or [Microsoft](https://learn.microsoft.com/en-us/azure/spring-apps/how-to-github-actions?pivots=programming-language-java).

# Secret Management

The API key for OpenWeatherAPI is stored in an Azure key vault.  Spring Cloud Azure is used to pull this information from the key vault.
