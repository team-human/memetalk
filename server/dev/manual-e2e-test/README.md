# Manual E2E Test

This folder contains scripts for testing the server end-to-end locally.

## Prerequisites

### Install Postman command line tool
Install newman with:

```
$ npm install -g newman
```
or 
```
$ brew install newman
```

### Install Postman app tool

Install through [Postman download page](https://www.postman.com/downloads/)

## Run Test

### Run the tests through Postman CLI
After you started the database and the server locally, run:
```
$ newman run postman-e2e.json
```

### Run the tests through Postman app tool
1. Know where your Postman working directory is. If you don't know how to find
   it, please see [this description](https://learning.postman.com/docs/getting-started/settings/).
   Default path is `~/Postman/files`.
2. Copy `meme1.png` in the current folder into the Postman working directory.
3. Import `postman-e2e.json` to your Postman app tool (it should be a collection
   type by default).
4. Run all tests through [Collection Runner](https://learning.postman.com/docs/running-collections/intro-to-collection-runs/)
   or individual test through Send button

## Add Test

1. Import `postman-e2e.json` to your Postman app tool (it should be a collection
   type by default).
2. Add new query or add new test, put into the same collection.
   ([Link](https://learning.postman.com/docs/writing-scripts/script-references/test-examples/) to instructions).
3. Export the collection to replace the `postman-e2e.json`.

Note: If you need a login token in your tests, add a key value pair `key: Authorization, value: {{login_token}}`  in Header.
      You can reference [create login_token PR](https://github.com/heronyang/memetalk/pull/82) for how to create `login_token` collection variables,
      and [use login_token to test mutation createMeme API](https://github.com/heronyang/memetalk/pull/83) for how to use it in the tests.

## For API Users (Client Development)

To inspect usage of each API, import `postman-e2e.json` to your Postman app tool.
