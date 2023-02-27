open-my-window seeks to provide the answer to a seemingly simple question.  "Should I open my window?"  At times the nights and mornings can be cool but temperatures will spike during the day.  open-my-window helps inform you if the air outside is worth bringing indoors for some free and all natural cooling.

# Overview

The project is largely intended to be a personal sandbox for exploration with various tech.  Things may get a bit over-engineered at times in the spirit of learning new things.  I will start with what I want to learn and then try to find a place for it inside this project.

The entry point to the project is through the ``arbiter`` module, specifically through the endpoint ``/window/{country_code}/{postal_code}``.  ``arbiter`` will call ``geocode`` to get the coordinates for the postal code and then use those coordinates to call ``forecast`` to obtain the weather data.  ``arbiter`` is responsible for parsing that data and making the final determination if your should open your window.  

Outside calls are made to the [OpenWeather API](https://openweathermap.org/api).  You will need your own API key if you wish to run the project yourself.  I am using the [One Call API](https://openweathermap.org/api/one-call-3).  

# Running

This application is designed to run either locally or on [Azure Spring Apps](https://azure.microsoft.com/en-us/services/spring-apps/).  The project contains 2 applications, `configserver` and `discoveryserver` that are only required when running locally and should not be deployed to ASA.  Details are below.

# Service Communication


As stated by [Azure Documentation](https://docs.microsoft.com/en-us/azure/spring-cloud/how-to-service-registration?pivots=programming-language-java) regarding service registration and discovery, Azure Spring Apps provides 2 methods to allow your services to find each other.  For demonstration purposes, open-my-window uses both.  arbiter -> geocode will use the kubernetes service and arbiter -> forecast will use Spring Cloud service discovery.

## Azure Spring Apps - Native Kubernetes Service
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
```java
	public GeocodeCoordinates getGeocodeCoordinates(String postalCode, String countryCode) {
		log.info("Geocode URL" + geocodeServiceUrl);
		return restTemplate.getForObject("http://" + geocodeServiceUrl + "/geocode/coordinates?postalCode=" + postalCode + "&countryCode=" + countryCode,
				GeocodeCoordinates.class);
	}
```

## Local - Inject URL as Value

When running locally and not in ASA, we need to specify the `geocodeserviceurl` as our local machine will not know how to decipher the URL `http://geocode/...`.  One way to inject this value is by using a command line parameter when running locally, `-Dgeocodeserviceurl=localhost:8081`

## Azure Spring Apps - Spring Cloud Service Registration

2. Use Spring Cloud Service Registration

The `ForecastService` class in `arbiter` will use the instance of `forecast` registered with the service registration.  A `DiscoveryClient` is injected into the constructor of this class to find the instance.

```java
	public ForecastService(RestTemplateBuilder restTemplateBuilder, DiscoveryClient discoveryClient) {
		this.restTemplate = restTemplateBuilder.build();
		this.discoveryClient = discoveryClient;
	}

	public ForecastRecord getCurrentWeather(Double lat, Double lon) {
		ServiceInstance serviceInstance = discoveryClient.getInstances("forecast").get(0);
		return restTemplate.getForObject("http://" + serviceInstance.getUri().getHost() + ":" + serviceInstance.getUri().getPort()
				+ "/forecast/forecastWeather?lat="+lat+"&lon="+lon, ForecastRecord.class);
	}
```

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

## Azure Spring Apps
Azure Spring Apps [allows you to specify the details](https://learn.microsoft.com/en-us/azure/spring-apps/how-to-config-server#attach-your-config-server-repository-to-azure-spring-apps) for the implicit configuration server.

## Local

When running locally we need to provide our own config server.  The application `configserver` is provided for this purpose.  To run `arbiter` locally and use the local `configserver`, use the 'local' profile when running `arbiter`.  This will load the `application-local.properties` file that has the location of the local configserver.

# Deploying to Azure Spring Apps

Microsoft has [good documentation around Azure Spring Apps](https://learn.microsoft.com/en-us/azure/spring-apps/).  Instead of trying to recreate that I will give my take on the process.

## Manual Deploy

In my opinion the simplest way to deploy an application is through using the [Azure CLI](https://learn.microsoft.com/en-us/cli/azure/service-page/azure%20spring%20apps?view=azure-cli-latest) with Azure Spring Apps

When deploying to ASA, you may need to log in first using
```shell
az login
```

After logging in, you need to create the application in ASA that you want to deploy.  This is a 1 time step and does not need to be repeated when performing subsequent version updates.  You will use the [az spring app create](https://learn.microsoft.com/en-us/cli/azure/spring/app?view=azure-cli-latest#az-spring-app-create) command.  Here is a sample based on this project:

```shell
az spring app create --name forecast --instance-count 1 --memory 2Gi --runtime-version Java_17 
```

After creating the application on ASA you can deploy by specifying the name of the applicatio you just created path the the Spring Boot .jar.  You will use [az spring app deploy](https://learn.microsoft.com/en-us/cli/azure/spring/app?view=azure-cli-latest#az-spring-app-deploy).  This is another sample based on the app create command above.
```
az spring app deploy --name forecast --artifact-path ./target/forecast-0.0.1-SNAPSHOT.jar --jvm-options="-Xms2048m -Xmx2048m"
```
## GitHub Actions

The Azure Spring Apps deployment can be automated by a GitHub Action.  [Here is an example](https://github.com/robertmcnees/open-my-window/actions/workflows/deploy-arbiter.yml) of deploying the `arbiter` application to ASA.  Documentation can be found on [GitHub](https://github.com/marketplace/actions/azure-spring-apps) or [Microsoft](https://learn.microsoft.com/en-us/azure/spring-apps/how-to-github-actions?pivots=programming-language-java).

# Secret Management

The API key for OpenWeatherAPI is stored in an Azure key vault.  Spring Cloud Azure is used to pull this information from the key vault.
