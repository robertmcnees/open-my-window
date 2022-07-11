### Create an image and push it to the Azure image repository
az acr login && mvn clean compile jib:build 

### Create the secrets that are referenced in the k8s objects
kubectl create secret generic omw-openweather-apikey --from-literal=openweatherapikey=5c312573ceae40d9969f15b92d0c4fa2