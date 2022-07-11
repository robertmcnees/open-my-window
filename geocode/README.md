# Overview

The purpose of geocode is to take a postal code and return lat long coordinates that can be used to look up weather data.  A basic cache exists to persist postal code data that has previously been geocoded.  The cache will be used first when available to minimize external API calls.

# Tech

## Local Secret Management

The application requires an API key to contact the OpenWeather API.

### Command Line

The API key can be specified as an argument when running with maven
```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DSECRET_OPENWEATHER_API_KEY=<your_api_key>"
```

### Environment Variable

The API key can also be stored in an environment variable.  On a Mac:
```
export SECRET_OPENWEATHER_API_KEY=<your_api_key>>    
```
