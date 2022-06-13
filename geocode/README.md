# Overview

The purpose of geocode is to take a postal code and return lat long coordinates that can be used to look up weather data.  A basic cache exists to persist postal code data that has previously been geocoded.  The cache will be used first when available to minimize external API calls.

# Tech

## RSocket

The service uses RSocket as supported by Spring.  To test the service using the command line, you can use the [rsc client here](https://github.com/making/rsc).

Using that client an example call would be:

```rsc tcp://localhost:8181 --route geocode -d'{"postalcode":"12345"}'```

## Vault

The API key for OpenWeather is stored in Vault.  While setting up vault is beyond this documentation, I followed [this guide](https://spring.io/guides/gs/vault-config/) for the integration.

The relevant commands are:

###Start the server

```vault server --dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"```

####Load secret
In a separate terminal, first export the relevant variables for the Vault CLI:
```
export export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
export VAULT_ADDR="http://127.0.0.1:8200"
vault kv put secret/geocode openweatherapi.apikey=<openweather_api_key>
```
