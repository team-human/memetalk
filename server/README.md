# MemeTalk - Server

Server code for MemeTalk.

## Development

This section describes how you run a server locally for development purpose.

### Prerequisites

- Install [Amazon Corretto 11 (OpenJDK 11)](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html).
- Install [Gradle 6.5.1](https://gradle.org/install/).
- Install [PostgreSQL 12.4](https://www.postgresql.org/docs/12/index.html). On MacOS, you can install with `brew install postgresql`.

### Init Database

Inits your database before first time you run the server or you want to reset
the data stored in your local database.

```bash
# Starts postgresql service.
$ pg_ctl -D /usr/local/var/postgres start
# Loads database for test. (Re-runing this clears previous states.)
$ psql postgres < dev/init_db.sql
# (If you finished developing) Stops postgresql service.
$ pg_ctl -D /usr/local/var/postgres stop
```

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

This section describes how you deploy the server onto the remote service
(Heroku).

### Setup

- Install [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli).
- Set up Git upstream: `$ heroku git:remote -a memetalk`.

### Test Deployment Locally

```bash
$ heroku local web
```

The server runs at `http://localhost:5000`.

### Deploy

```bash
# Move the to root folder of the project first.
$ cd .. && git subtree push --prefix server heroku master
```

The server runs at `https://memetalk.herokuapp.com/`.

## More

### Apply Linter and reformat the code
```bash
$ ./gradlew spotlessApply
```

### GraphQL
To interact with GraphQL spring boot server, you can use Postman to send the requests and view the response.
Please install [Postman](https://www.postman.com/) and follow [this blog](https://learning.postman.com/docs/sending-requests/supported-api-frameworks/graphql/) to send the requests.
