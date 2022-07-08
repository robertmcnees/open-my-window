# Overview

The purpose of geocode is to take a postal code and return lat long coordinates that can be used to look up weather data.  A basic cache exists to persist postal code data that has previously been geocoded.  The cache will be used first when available to minimize external API calls.

# Tech

## Secret Management

The application requires an API key to contact the OpenWeather API.  There are a few options configured to obtain the API key:

### Command Line

The API key can be specified as an argument on the command line using the parameter
```
-Dopenweatherapikey=<your_api_key>
```

### Vault Running Locally

While setting up vault is beyond this documentation, I followed [this guide](https://spring.io/guides/gs/vault-config/) for the integration.  The relevant commands are:

#### Start the server: 
```vault server --dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"```

#### Load secret
In a separate terminal, first export the relevant variables for the Vault CLI:
```
export export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
export VAULT_ADDR="http://127.0.0.1:8200"
vault kv put secret/geocode openweatherapikey=<openweather_api_key>
```

### Azure Key Vault

My API key is stored in Azure Key Vault.  To use that method you need to run the build using the azure profile by specifying `-Pazure`.


