# AuthController API Documentation

## Base URL
`/api/v1/auth`

### POST `/sign-up`
**Description:** Registers a new user.

**Request Body:**
- `SignUpRequest` (JSON): Contains user registration details.

**Response:**
- **201 Created**: User successfully registered.
  ```json
  {
    "success": true,
    "message": "Created",
    "data": { ... }
  }
  ```

**Error Responses:**
- **400 Bad Request**: Invalid input data.

---

### POST `/sign-in`
**Description:** Authenticates a user and provides a token.

**Request Body:**
- `SignInRequest` (JSON): Contains user login details.

**Response:**
- **200 OK**: User successfully authenticated.
  ```json
  {
    "success": true,
    "message": "OK",
    "data": { ... }
  }
  ```

**Error Responses:**
- **401 Unauthorized**: Invalid credentials.
