# Todo API Spec

## Get Todos
```http
GET /api/todos/{idUser}
```

Response Body (Success - 200)
```json
{
  "data" : [
    {
      "id" : "100",
      "description" : "Bermain Basket",
      "status" : true
    },
    {
      "id" : "101",
      "description" : "Bermain Warnet",
      "status" : false
    }
  ]
}
```

Response Body (Failed - 401)
```json
{
  "errors" : "Unauthorized"
}
```

## Create Todo
```http
POST /api/todos
```

Request Body
```json
{
  "description" : "Bermain Sepak Bola"
}
```

Response Body (Success - 200)
```json
{
  "data" : {
    "id" : "103",
    "description" : "Bermain Sepak Bola",
    "status" : false
  }
}
```

Response Body (Failed - 401)
```json
{
  "errors" : "Unauthorized"
}
```

## Update Todo
```http
PUT /api/todos/{idTodo}
```

Request Body
```json
{
  "id": 103,
  "status": true
}
```

Response Body (Success - 200)
```json
{
  "data" : {
    "id" : "103",
    "description" : "Bermain Sepak Bola",
    "status" : true
  }
}
```

Response Body (Failed - 401)
```json
{
  "errors" : "Unauthorized"
}
```

## Remove Todo
```http
DELETE api/todos/{idTodo}
```

Response Body (Success - 200)

```json
{
  "data": "ok"
}
```

Response Body (Failed - 401)
```json
{
  "errors" : "Unauthorized"
}
```