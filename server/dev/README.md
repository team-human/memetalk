# Dev

This folder contains tools to help local development.

## Initial Database

```bash
$ ./init_db.sh
```

## Postgres DB Debugging

Connect to PostgreSQL Database

```shell script
psql memetalk
```

## Javascript example code for GraphQL API

When developing client GraphQL API, you may need to use Javascript to debug/develop client codes.
Here, we provide Javascript example codes for those complicated APIs.

### Mutation CreateMeme

Remember to use a valid path for `file` field, and unexpired token for `Authorization` in the headers.

```javascript
var form = new FormData();
form.append(
  "operations",
  '{ "query": "mutation ($file: File!, $tags: [String!]) { createMeme(file: $file, tags: $tags) { tags } }", "variables": { "file": null, "tags": ["software-test-tag", "humor-test-tag"] } }'
);
form.append("map", '{ "0": ["variables.file"] }');
form.append("file", fileInput.files[0], "meme1.png");

var settings = {
  url: "localhost:8080/graphql",
  method: "POST",
  timeout: 0,
  headers: {
    Authorization:
      "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJyeSIsImlzcyI6Im1lbWV0YWxrIiwiZXhwIjoxNjEzMzkzMDE2LCJpYXQiOjE2MTA4MDEwMTZ9.Fa6lR7uC8E4DHKUt-V6TUYXLQJgJWu0DdMUUeulARC8",
  },
  processData: false,
  mimeType: "multipart/form-data",
  contentType: false,
  data: form,
};

$.ajax(settings).done(function (response) {
  console.log(response);
});
```
