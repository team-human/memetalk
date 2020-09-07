# MemeTalk - Server

Server code for MemeTalk.

## Prerequisites

- Install [Amazon Corretto 11 (OpenJDK 11)](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html).
- Install [Gradle 6.5.1](https://gradle.org/install/).

## Development

### Build

```bash
$ ./gradlew build
```

### Run

To run a spring boot application
```bash
$ ./gradlew bootRun
```

### Test

```bash
$ ./gradlew test
```

## Deployment

### Setup

- Install [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli).
- Set up Git upstream: `$ heroku git:remote -a memetalk`.

### Test Deployment Locally

```bash
$ heroku local web
```

The server runs at `localhost:5000`.

### Deploy

```bash
# Move the to root folder of the project first.
$ cd .. && git subtree push --prefix server heroku master
```

The server runs at .

## More

### Apply Linter and reformat the code
```bash
$ ./gradlew spotlessApply
```

### GraphQL
To interact with GraphQL spring boot server, you can use Postman to send the requests and view the response.
Please install [Postman](https://www.postman.com/) and follow [this blog](https://learning.postman.com/docs/sending-requests/supported-api-frameworks/graphql/) to send the requests.
