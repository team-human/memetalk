# MemeTalk - Server

Server code for MemeTalk.

## Prerequisites

- Install [Amazon Corretto 11 (OpenJDK 11)](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html).
- Install [Gradle 6.5.1](https://gradle.org/install/).
- Install [graphql-playground](https://github.com/prisma-labs/graphql-playground)

## Build

```bash
$ ./gradlew build
```

## Run
To run a spring boot application
```bash
$ ./gradlew bootRun
```

## Test

```bash
$ ./gradlew test
```

## Apply Linter and reformat the code
```bash
$ ./gradlew spotlessApply
```

## GraphQL
To interact with GraphQL spring boot server, you can use Postman to send the requests and view the response.
Please install [Postman](https://www.postman.com/) and follow [this blog](https://learning.postman.com/docs/sending-requests/supported-api-frameworks/graphql/) to send the requests.

