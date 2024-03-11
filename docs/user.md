# User API Spec

## Register User

```http
  POST /api/users
```
Request Body :
```json
{
  "username" : "kevin",
  "password" : "rahasia"
}
```

Response Body (Success - 200) :

```json
{
  "data" : "ok"
}
```

Response Body (Failed - 401) :

```json
{
  "errors" : "Username must not blank, ???"
}
```

## Get User
```http
  GET /api/users
```

Response Body (Success - 200)
```json
{
  "data" : {
    "username" : "kevin"
  }
}
```

Response Body (Failed - 401)
```json
{
  "errors" : "Unauthorized"
}
```

## Login User

```http
  POST /api/auth/login
```

Request Body :

```json
{
  "username" : "kevin",
  "password" : "rahasia" 
}
```

Response Body (Success - 200) :

```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 2342342423423 // milliseconds
  }
}
```

Response Body (Failed - 401) :

```json
{
  "errors" : "Username or password wrong"
}
```

## Update User Password
```http
  PUT /api/users/password
```

Request Body :
```json
{
  "old_password" : "rahasia",
  "new_password" : "rahasia2",
  "repeat_password" : "rahasia2"
}
```

Response Body (Success - 200)
```json
{
  "data" : "ok"
}
```

Response Body (Failed - 401)
```json
{
  "errors" : "Password do not match"
}
```

## Logout

```http
 DELETE /api/auth/logout
```

Response Body (Success - 200)
```json
{
  "data" : "ok"
}
```

Response Body (Failed - 401)
```json
{
  "errors" : "Unauthorized"
}
```