### Create an image and push it to the Azure image repository
az acr login && mvn clean compile jib:build 

### Create the secrets that are referenced in the k8s objects
kubectl create secret generic omw-openweather-apikey --from-literal=openweatherapikey=<api_key>