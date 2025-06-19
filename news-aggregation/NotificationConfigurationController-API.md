# ExternalServerConfigurationController API Documentation

## Base URL
`/api/v1/server-configurations`

### POST `/`
**Description:** Registers a new external server configuration.

**Request Body:**
- `RegisterExternalServerRequest` (JSON): Contains server configuration details.
# KeywordsController API Documentation
**Response:**
- **201 Created**: Server configuration successfully registered.
  ```json
  {
    "success": true,
    "message": "Created"
  }
  ```

**Error Responses:**
- **400 Bad Request**: Invalid input data.

---

### GET `/`
**Description:** Retrieves all external server configurations.

**Response:**
- **200 OK**: List of server configurations.
  ```json
  {
    "success": true,
    "message": "OK",
    "data": [ ... ]
  }
  ```

**Error Responses:**
- **500 Internal Server Error**: Unexpected error occurred.

---

### PATCH `/`
**Description:** Updates an external server configuration.

**Request Body:**
- `UpdateExternalServerDetailsRequest` (JSON): Contains updated server configuration details.

**Response:**
- **200 OK**: Server configuration successfully updated.
  ```json
  {
    "success": true,
    "message": "OK"
  }
  ```

**Error Responses:**
- **400 Bad Request**: Invalid input data.
- **404 Not Found**: Server configuration not found.

## Base URL
`/api/v1/keywords`

### POST `/`
**Description:** Creates a new keyword for the user.

**Request Body:**
- `CreateKeywordRequest` (JSON): Contains keyword details.

**Response:**
- **201 Created**: Keyword successfully created.
  ```json
  {
    "success": true,
    "message": "Created"
  }
  ```

**Error Responses:**
- **400 Bad Request**: Invalid input data.

---

### PATCH `/{id}/activate`
**Description:** Activates a keyword for the user.

**Path Parameters:**
- `id` (Long): ID of the keyword to activate.

**Response:**
- **200 OK**: Keyword successfully activated.
  ```json
  {
    "success": true,
    "message": "OK"
  }
  ```

**Error Responses:**
- **404 Not Found**: Keyword not found.
# NewsCategoryController API Documentation
---

### PATCH `/{id}/deactivate`
**Description:** Deactivates a keyword for the user.

**Path Parameters:**
- `id` (Long): ID of the keyword to deactivate.

**Response:**
- **200 OK**: Keyword successfully deactivated.
  ```json
  {
    "success": true,
    "message": "OK"
  }
  ```

**Error Responses:**
- **404 Not Found**: Keyword not found.

## Base URL
`/api/v1/categories`

### POST `/`
**Description:** Creates a new news category.

**Request Body:**
- `CreateCategoryRequest` (JSON): Contains category details.
# NewsController API Documentation
**Response:**
- **201 Created**: Category successfully created.
  ```json
  {
    "success": true,
    "message": "Created"
  }
  ```

**Error Responses:**
- **400 Bad Request**: Invalid input data.

---

### GET `/`
**Description:** Retrieves all news categories.

**Response:**
- **200 OK**: List of news categories.
  ```json
  {
    "success": true,
    "message": "OK",
    "data": [ ... ]
  }
  ```

**Error Responses:**
- **500 Internal Server Error**: Unexpected error occurred.

---

### GET `/{categoryName}/keywords`
**Description:** Retrieves all keywords under a specific category.

**Path Parameters:**
- `categoryName` (String): Name of the category.

**Response:**
- **200 OK**: List of keywords under the category.
  ```json
  {
    "success": true,
    "message": "OK",
    "data": [ ... ]
  }
  ```

**Error Responses:**
- **404 Not Found**: Category not found.

## Base URL
`/api/v1/news`

### GET `/headlines`
**Description:** Retrieves the latest news headlines.

**Query Parameters:**
- `ViewHeadlinesRequest` (Model Attribute): Contains filter criteria for headlines.

**Response:**
- **200 OK**: List of news headlines.
  ```json
  {
    "success": true,
    "message": "OK",
    "data": [ ... ]
  }
  ```

**Error Responses:**
- **500 Internal Server Error**: Unexpected error occurred.

---

### GET `/{categoryName}/headlines`
**Description:** Retrieves news headlines under a specific category.

**Path Parameters:**
- `categoryName` (String): Name of the category.

**Query Parameters:**
- `ViewHeadlinesRequest` (Model Attribute): Contains filter criteria for headlines.

**Response:**
- **200 OK**: List of news headlines under the category.
  ```json
  {
    "success": true,
    "message": "OK",
    "data": [ ... ]
  }
  ```

**Error Responses:**
- **404 Not Found**: Category not found.

---

### POST `/{newsId}/like`
**Description:** Likes a news article.

**Path Parameters:**
- `newsId` (Long): ID of the news article to like.

**Response:**
- **200 OK**: News article successfully liked.
  ```json
  {
    "success": true,
    "message": "OK"
  }
  ```

**Error Responses:**
- **404 Not Found**: News article not found.

---

### POST `/{newsId}/dislike`
**Description:** Dislikes a news article.

**Path Parameters:**
- `newsId` (Long): ID of the news article to dislike.

**Response:**
- **200 OK**: News article successfully disliked.
  ```json
  {
    "success": true,
    "message": "OK"
  }
  ```

**Error Responses:**
- **404 Not Found**: News article not found.

---
# NotificationConfigurationController API Documentation

## Base URL
`/api/v1/notifications`

### POST `/`
**Description:** Updates the notification preferences for the user.

**Request Body:**
- `UpdateNotificationPreferencesRequest` (JSON): Contains notification preference details.

**Response:**
- **201 Created**: Notification preferences successfully updated.
  ```json
  {
    "success": true,
    "message": "Created"
  }
  ```

**Error Responses:**
- **400 Bad Request**: Invalid input data.
- **500 Internal Server Error**: Unexpected error occurred.
