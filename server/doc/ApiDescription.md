# GraphQL API description
This document is to describe how to interact with GraphQL API, especially for those complex API.

## Mutation - createUser
`createUser` API is to create a new User in our Meme server. The curl command is the following.
``` shell script
curl --location --request POST 'localhost:8080/graphql' \
 --header 'Content-Type: application/json' \
 --data-raw '{"query":"mutation createUser ($userInfo: CreateUserInput!) {createUser (userInfo: $userInfo) {\n user {id\n name\n roles\n }\n token\n }\n}","variables":{"userInfo":{"id":"Sam","password":"123","name":"today"}}}'
```
The response is look like the following
``` shell script
{"data":{"createUser":{"user":{"id":"Sam","name":"today","roles":["USER"]},"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYW0iLCJpc3MiOiJtZW1ldGFsayIsImV4cCI6MTYwOTEzOTA1OCwiaWF0IjoxNjA2NTQ3MDU4fQ.Fae1qH7fQoJ1gHIXelOIHBVsXx0thzzdYrxeZrh_8mc"}}}
```
Please reserve this token. If you don't want to call `login` API again to get the token information.

## Mutation - login
After a user create a new account, he can get the Jwt token for authentication by calling `login` API. For example,
``` shell script
curl --location --request POST 'localhost:8080/graphql' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=B616E228ECC9627CCFB570E77BBAA7CC' \
--data-raw '{"query":"mutation login ($id: String!, $password: String!) {\n login (id: $id, password: $password) {\n user {\n id\n name\n roles\n }\n token\n }\n}","variables":{"id":"userId1","password":"123"}}'
```
The response is
``` 
{"data":{"login":{"user":{"id":"userId1","name":"John","roles":["ADMIN"]},"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VySWQxIiwiaXNzIjoibWVtZXRhbGsiLCJleHAiOjE2MDkxMzk0NDgsImlhdCI6MTYwNjU0NzQ0OH0.KhhPbCer3ixu9e43lLqDRrbv7Ql77CuirxrvdJJrcdQ"}}}
```
Please reserve this token. 

## Any API call that needs to have the Jwt token
For any API call that requires Jwt Authentication token, you need to get Jwt token from `login` or `createUser` API.
After that, adding the token into the Http header as the following.

| Key        | Value           |
| ------------- |:-------------:|
| Authorization | Bearer `token`|
Note: In the value field, it is `Bearer` + ` ` (empty whitespace) + `token`.