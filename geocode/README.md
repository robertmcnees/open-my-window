# Overview

The purpose of geocode is to take a zip code and return lat long coordinates that can be used to look up weather data.

## Tech

The service uses RSocket as supported by Spring.  To test the service using the command line, you can use the [rsc client here](https://github.com/making/rsc).

Using that client an example call would be:

rsc tcp://localhost:8181 --route geocode -d'{"zipcode":"12345"}'