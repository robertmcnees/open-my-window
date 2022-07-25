open-my-window seeks to provide the answer to a seemingly simple question.  "Should I open my window?"  At times the nights and mornings can be cool but temperatures will spike during the day.  open-my-window helps inform you if the air outside is worth bringing indoors for some free and all natural cooling.

## Overview

The project is largely intended to be a personal sandbox for exploration with various tech.  Things may get a bit over-engineered at times in the spirit of learning new tech.  I will start with what I want to learn and then try to find a place for it inside this project.

The entry point to the project is through the ``arbiter`` module, specifically through the endpoint ``/arbiter/window?postalCode=<postalCode>``.  ``arbiter`` will call ``geocode`` to get the coordinates for a given postal code (only US is supported at the moment).  Those coordinates are passed to ``forecast`` to obtain the weather data.  ``arbiter`` is responsible for parsing that data and making the final determination if your should open your window.  

Outside calls are made to the [OpenWeather API](https://openweathermap.org/api).  You will need your own API key if you wish to run the project yourself.  I am using the [One Call API](https://openweathermap.org/api/one-call-3).  

### Environment Variables

The ``geocode`` and ``forecast`` modules use the OpenWeather API.  The API key is injected into a Spring bean using the parameter ``SECRET_OPENWEATHER_API_KEY``.  When running locally I pass the value as a JVM argument using ``-DSECRET_OPENWEATHER_API_KEY=<your_key>``.  When running in Kubernetes, the value is injected into a pod as a secret shown in the following snippet from ``k8s_all_objects.yml``:
```yaml
  env:
    - name: SECRET_OPENWEATHER_API_KEY
      valueFrom:
        secretKeyRef:
          name: omw-openweather-apikey
          key: openweatherapikey
          optional: false
```
This assumes that the secret has already been created in the k8s environment.  More on that in a minute.

``arbiter`` currently finds services to communicate with by injecting the URL as a Spring ``@Value`` parameter.  When running locally I use JVM arguments to locate the services, specifically:
```
-Dforecastserviceurl=localhost:8082 -Dgeocodeserviceurl=localhost:8081
```

When running in Kubernetes, the URLs default to the services defined in ``ks_all_objects.yml``.  For example, 
```java
@Value("${forecastserviceurl:omw-forecast}")
```
Uses a default that matches
```yml
apiVersion: v1
kind: Service
metadata:
  name: omw-forecast
spec:
  ports:
    - port: 80
      targetPort: 8082
  selector:
    app: omw-forecast
```


## Running with Azure (AKS)

### Container Registry

I followed [this walkthrough from Microsoft](https://docs.microsoft.com/en-us/azure/container-registry/container-registry-java-quickstart) which I will quickly summarize my key points.  I first created an [Azure Container Registry](https://azure.microsoft.com/en-us/services/container-registry/) for uploaded images.  To push the image to the registry, I used the command:
``az acr login --name <your_repo> && mvn clean compile jib:build``
In order to use that command I had to make some changes to my ``pom.xml``.  This plugin was added:
```xml
<plugin>
    <artifactId>jib-maven-plugin</artifactId>
    <groupId>com.google.cloud.tools</groupId>
    <version>${jib-maven-plugin.version}</version>
    <configuration>
        <from>
            <image>mcr.microsoft.com/java/jdk:17-zulu-alpine</image>
        </from>
        <to>
            <image>${docker.image.prefix}/${project.artifactId}</image>
        </to>
    </configuration>
</plugin>
```
Where the property ``${docker.image.prefix}`` was the repository that I created.

### Secret Management

I used native secrets in kubernetes.  I used the command line from CloudShell in AKS and executed the command:

```kubectl create secret generic omw-openweather-apikey --from-literal=openweatherapikey=<your_api_key>```

This value is used when injecting an environment variable to a pod.  Specifically with the yml

```yml
valueFrom:
    secretKeyRef:
      name: omw-openweather-apikey
      key: openweatherapikey
      optional: false
```

### Kubernetes

[Azure Kubernetes Services](https://azure.microsoft.com/en-us/services/kubernetes-service/) (AKS) can be configured loading the configuration located in ``k8s_all_objects.yml``.  If the images are uploaded and the secret is created then the yml should need no additional changes.

The external IP for the application can be found by executing this command from Cloud Shell ``kubectl get service omw-arbiter``




