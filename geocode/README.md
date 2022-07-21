# Overview

The purpose of ``geocode`` is to take a postal code and return latitude and longitude coordinates that can be used to look up weather data.  A basic cache exists to persist postal code data that has previously been geocoded.  When available the cache will be used first to minimize external API calls.
