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

```bash
$ ./gradlew run
```

## Test

```bash
$ ./gradlew test
```

## Apply Linter and reformat the code
```bash
$ ./gradlew spotlessApply
```
