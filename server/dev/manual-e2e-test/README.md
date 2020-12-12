# Manual E2E Test

This folder contains scripts for testing the server end-to-end locally.

## Prerequisites

Install newman with:

```
$ npm install -g newman
```

or 

```
$ brew install newman
```

## Run Test

After you started the database and the server locally, run:

```
$ newman run postman-e2e.json
```

## Add Test

1. Import `postman-e2e.json` to your Postman tool (it should be a collection
   type by default).
2. Add new query or add new test, put into the same collection.
   ([Link](https://learning.postman.com/docs/writing-scripts/script-references/test-examples/) to instructions).
3. Export the collection to replace the `postman-e2e.json`.

## For API Users (Client Development)

To inspect how each API is used, import `postman-e2e.json` to your Postman tool.
