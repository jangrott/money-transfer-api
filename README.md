[![Build Status](https://travis-ci.org/jangrott/money-transfer-api.svg?branch=master)](https://travis-ci.org/jangrott/money-transfer-api)
[![codecov](https://codecov.io/gh/jangrott/money-transfer-api/branch/master/graph/badge.svg)](https://codecov.io/gh/jangrott/money-transfer-api)

# money-transfer-api
RESTful API for money transfers

## Overview
Simple java RESTful API for performing money transfer between two internal accounts.

## Endpoints
* `GET` /api/clients - returns all existing clients
* `GET` /api/clients/<clientId> - returns the specified client
* `GET` /api/clients/<clientId>/accounts - returns all accounts of the specified client
* `GET` /api/clients/<clientId>/accounts/<accountId> - returns the specified account of the specified client
* `POST` /api/transfer - performs money transfer between two internal accounts
  - example request body
  ```json
    {
    	"fromAccount": 236476251,
    	"toAccount": 298251251,
    	"amount": 100.00
    }
  ```

## How to run
### without any data in the database
```
./gradlew run
```
### with sample data in the database
```
./gradlew run -PgenerateData
```

## API tests
### Human-readable test scenarios for endpoints:
* `/api/clients` - [see here](src/test/resources/pl/jangrot/mtransfer/rest/clients_accounts.feature)
* `/api/transfer` - [see here](src/test/resources/pl/jangrot/mtransfer/rest/transfer.feature)