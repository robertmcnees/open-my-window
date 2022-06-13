# Overview

The purpose of geocode is to take a postal code and return lat long coordinates that can be used to look up weather data.

## Tech

### RSocket

The service uses RSocket as supported by Spring.  To test the service using the command line, you can use the [rsc client here](https://github.com/making/rsc).

Using that client an example call would be:

rsc tcp://localhost:8181 --route geocode -d'{"postalcode":"12345"}'

### Vault

The API key for OpenWeather is stored in Vault.  To load the information to Vault:

vault kv put secret/geocode openweatherapi.apikey=<openweather_api_key>