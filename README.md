open-my-window seeks to provide the answer to a seemingly simple question.  "Should I open my window?"  At times the nights and mornings can be cool but temperatures will spike during the day.  open-my-window helps inform you if the air outside is worth bringing indoors for some free and all natural cooling.

## Overview

The project is largely intended to be a personal sandbox for exploration with various tech.  Things may get a bit over-engineered at times in the spirit of learning new tech.  I will start with what I want to learn and then try to find a place for it inside this project.

The entry point to the project is through the ``arbiter`` module, specifically through the endpoint ``/arbiter/window?postalCode=<postalCode>``.  ``arbiter`` will call ``geocode`` to get the coordinates for a given postal code.  Those coordinates are passed to ``forecast`` to obtain the weather data.  ``arbiter`` is responsible for parsing that data and making the final determination if your should open your window.  

Outside calls are made to the [OpenWeather API](https://openweathermap.org/api).  You will need your own API key if you wish to run the project yourself.  I am using the [One Call API](https://openweathermap.org/api/one-call-3).  

# Azure Spring Apps

This application is designed to be seamless run either locally or on [Azure Spring Apps](https://azure.microsoft.com/en-us/services/spring-apps/).

### Service Discovery

As stated by [Azure Documentation](https://docs.microsoft.com/en-us/azure/spring-cloud/how-to-service-registration?pivots=programming-language-java) regarding service registration and discovery, Azure Spring Apps provides 2 methods to allow your services to find each other.  For demonstration purposes, open-my-window uses both.  arbiter -> geocode will use the native service and arbiter -> forecast will use Spring Cloud service discovery.
1. Kubernetes service names are created automatically

``
Azure Spring Apps creates a corresponding kubernetes service for every app running in it using app name as the kubernetes service name. So you can invoke calls in one app to another app by using app name in a http/https request like http(s)://{app name}/path.
``

The `GeocodeService` class in `arbiter` uses this approach.  Since it is using a native k8s service under the hood, it will look very similar to a native k8s solution.

In the example below when running in Azure Spring Apps no variable `geocodeserviceurl` will be provided so the URL will default to `geocode` which is the same as the application name that we wish to call.
```java
	@Value("${geocodeserviceurl:geocode}")
	private String geocodeServiceUrl;
```

2. Use Spring Cloud Service Registration

The `ForecastService` class in `arbiter` will use the instance of `forecast` registered with the service registration.  A `DiscoveryClient` is injected into the constructor of this class to find the instance.  The `ForecastApplication` is annotated with `@EnableEurekaClient` to register itself.

### External Configuration

`aribter` pulls configuration from a [GitHub repository](https://github.com/robertmcnees/open-my-window-config) specifically designed to hold externalized configuration.  This is accomplished by including the config starter in the `pom.xml`.
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

Azure Spring Apps [allows you to specify the details](https://learn.microsoft.com/en-us/azure/spring-apps/how-to-config-server#attach-your-config-server-repository-to-azure-spring-apps) for the implicit configuration server.


## Deploying to Azure Spring Apps

To deploy an app to Azure Spring Apps, you first need to create the application:
```
az spring app create --name forecast --instance-count 1 --memory 2Gi --runtime-version Java_17 
```
After creating the app you can deploy by specifying the path the the Spring Boot .jar
```
az spring app deploy --name forecast --artifact-path ./target/forecast-0.0.1-SNAPSHOT.jar --jvm-options="-Xms2048m -Xmx2048m"
```

## Secret Management

At this time I manually enter the API key for OpenWeather via the Azure console after creating the application.

## Running Locally

Azure Spring Apps provides 2 pieces of functionality that we need to replicate in a local environment.  Mainly how services communicate with one another and how external configuration is loaded

### Service Communication

The `GeocodeService` in `arbiter` uses a k8s service to find the geocode service.  If running locally, you will need to specify a variable `geocodeserviceurl` as a build argument.

Spring Cloud Service Discovery is also used so that the `arbiter` service can call the `forecast` service.  As such, a service registraton server must be running in the environment that you are deploying to.  When you are running in Azure Spring Apps, the registration server is provided for you.  When running locally, you will need to run the module `discoveryserver` in this application.

The 2 methods were used as a way to demonstrate multiple ways services can communicate in Azure Spring Apps.

### External Configuration

Azure Spring Apps provides a configuration service for us out of the box.  That is not true in our local environments, so we need to supply our own configuration server.  This config server will be the intermediary between an application that needs external configuration (i.e. `arbiter`) and the configuration (i.e. the GitHub repository).

The `configserver` module provides this intermediary functionality.'  This deployment is only required when running locally.

Similarly, we must tell the `arbiter` application to look for the config server locally.  To do this set the active profile to local by using the command line argument `-Dspring.profiles.active=local` when running the application.  This will use the `application-local.properties` file that will tell `arbiter` where to find the `configserver`.

