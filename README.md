# Daily Schedule Organiser API Documentation

## API Endpoints Table

| **Method** | **Request Type** | **Access**    | **Role**         | **Description**                                        | **Endpoint**                      |
|------------|------------------|---------------|------------------|--------------------------------------------------------|-----------------------------------|
| POST       | `application/json` | Public        | None             | User login attempt for authentication.                 | `/api/auth/login`                 |
| POST       | `application/json` | Public        | None             | Register a new user.                                   | `/api/auth/register`              |
| POST       | `application/json` | Authenticated | ROLE_USER        | Add a new task for the authenticated user.              | `/taskManager/`                   |
| GET        | `application/json` | Authenticated | ROLE_USER        | Get all tasks for a specific date for the authenticated user. | `/taskManager/{date}`             |
| PUT        | `application/json` | Authenticated | ROLE_USER        | Update an existing task for a specific date.            | `/taskManager/{date}/{id}`        |
| DELETE     | `application/json` | Authenticated | ROLE_USER        | Delete a task for a specific date.                      | `/taskManager/{date}/{id}`        |
| GET        | `application/json` | Authenticated | ROLE_MANAGER     | Get all users under the manager's supervision.          | `/manager/users`                  |
| PUT        | `application/json` | Authenticated | ROLE_MANAGER     | Update task description for a specific user under the manager's supervision. | `/manager/tasks/{date}/{id}`      |
| POST       | `application/json` | Authenticated | ROLE_ADMIN       | Register a new manager.                                | `/admin/register-manager`         |
| GET        | `application/json` | Authenticated | ROLE_ADMIN       | Get all users with role `ROLE_USER`.                    | `/admin/users`                    |
| GET        | `application/json` | Authenticated | ROLE_ADMIN       | Get all users with role `ROLE_MANAGER`.                 | `/admin/managers`                 |
| POST       | `application/json` | Authenticated | ROLE_ADMIN       | Assign a manager to users.                              | `/admin/assign-manager/{id}`      |

# JWT Generation, Validation, and Role-Based Access Control (RBAC)

This section explains the security mechanism implemented in the project using **JWT (JSON Web Token)** for authentication and **RBAC (Role-Based Access Control)** for authorization. These mechanisms ensure secure access to the API by verifying users' identity and granting access based on their assigned roles.

## Table of Contents

1. [JWT Generation](#jwt-generation)
2. [JWT Validation](#jwt-validation)
3. [Role-Based Access Control (RBAC)](#role-based-access-control-rbac)
4. [JWT Flow](#jwt-flow)

---

## JWT Generation

### 1. User Login
The user initiates the authentication process by sending a **login request** containing the `username` and `password` to the server.

- **Endpoint**: `/api/auth/login`
- **Method**: `POST`
- **Request Body**:
    ```json
    {
      "username": "user@example.com",
      "password": "password123"
    }
    ```
    
Upon receiving the login request, the server **validates** the credentials by comparing them against the stored user data in the database.

### 2. JWT Token Generation
Once the credentials are validated and the user is authenticated, the server generates a **JWT** that contains the following claims:
- **User ID**: A unique identifier for the user.
- **Username**: The user's username.
- **Roles**: The user's roles, such as `ROLE_USER`, `ROLE_MANAGER`,`ROLE_ADMIN`, etc.
- **Issued At (iat)**: The time the token was issued.
- **Expiration (exp)**: The expiration time of the token.

The server uses a **secret key** (e.g., `JWT_SECRET_KEY`) to sign the token, ensuring that the token is tamper-proof and can be verified on subsequent requests.

Example JWT structure:

- **Header**: Contains information about the algorithm used for signing the token (e.g., `HS256`).
- **Payload**: Contains the claims such as user ID, roles, and expiration time.
- **Signature**: Generated by signing the header and payload with the secret key.

The JWT is sent back to the client in the response.

### Example Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZSI6Ik1hbmFnZXIiLCJpYXQiOjE2Mzg5NzQwMDB9.X2n3Vgkqkjl2z5OkLhDxvtuYXs1NlqAn4u0lbnNTnm4"
}
```
## JWT Validation

### 1. Token Extraction
In order to access protected resources, the client needs to include the JWT in the **Authorization** header of the HTTP request. The format for including the JWT is as follows:


### 2. Token Decoding and Validation
Once the server receives the request, it performs the following steps to validate the JWT:

1. **Extract the JWT**: The server extracts the JWT from the `Authorization` header.
2. **Decode the JWT**: The JWT is divided into three parts: the header, the payload, and the signature.
3. **Validate the Signature**: The server uses a secret key (e.g., `JWT_SECRET_KEY`) to validate the signature of the token. This ensures that the token has not been tampered with.
4. **Check Expiration**: The server checks the `exp` claim in the token to verify if the token has expired. If the current time is greater than the `exp` time, the token is considered invalid, and the server will reject the request.
5. **Extract Claims**: The server extracts the claims from the payload of the token. Claims usually include the user ID, roles, and any other relevant data.

If the token is valid and the expiration time has not passed, the server proceeds with the request. If the token is invalid or expired, the server returns an **Unauthorized (401)** response, prompting the user to log in again.

---

## Role-Based Access Control (RBAC)

### 1. Role Assignment
Each user is assigned one or more roles during the authentication process. These roles define what resources the user can access and what actions they are allowed to perform. The roles are embedded in the JWT during the authentication process.

For example, roles might include:
- `ROLE_USER`: A regular user with basic access to their own data.
- `ROLE_ADMIN`: An admin user with higher-level access to manage users and view reports.
- `ROLE_MANAGER`: A manager who can access specific data and manage teams.

### 2. Protecting Endpoints with Roles
Once a user is authenticated, their roles are included in the JWT. The server uses these roles to authorize access to specific endpoints. Role-based authorization is implemented using the `@PreAuthorize` annotation or `@Secured` annotation in Spring Security.

#### Example:

```java
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
public List<User> getAllUsers() {
    return userService.findAllUsers();
}
```
## JWT Flow

The **JWT (JSON Web Token)** flow is used for authentication and authorization in the Daily Schedule Organizer (DSO) API. Here's the sequence of steps involved in the JWT authentication process:

---

### Steps in JWT Flow

1. **User Login**
   - The user sends a login request with their **username** and **password** to the `/api/auth/login` endpoint.
   - If the credentials are correct, the server generates a JWT and returns it in the response.

2. **Token Storage**
   - The client stores the received JWT token (usually in `localStorage` or `sessionStorage` in a web application).

3. **Token in Request**
   - For every subsequent request that requires authentication, the client includes the JWT token in the `Authorization` header with the `Bearer` prefix.

4. **Token Validation**
   - The server validates the JWT by checking its signature and expiration. If valid, the server processes the request and returns the response.
   - If the JWT is invalid or expired, the server returns an error message.

---

### JWT Flow Sequence Diagram

```plaintext
sequenceDiagram
    participant C as Client
    participant S as Server
    participant DB as Database

    C->>S: POST /api/auth/login (username, password)
    S->>DB: Validate credentials (username, password)
    DB-->>S: Return user data (valid/invalid)
    S->>C: Generate JWT and send it back
    C->>C: Store JWT in localStorage or sessionStorage

    C->>S: GET /some-protected-resource (Authorization: Bearer <JWT>)
    S->>S: Validate JWT (check signature and expiration)
    S->>DB: Fetch requested data (if token is valid)
    DB-->>S: Return data
    S->>C: Return the requested data

    C->>S: GET /some-protected-resource (Authorization: Bearer <invalidJWT>)
    S->>S: Validate JWT (invalid or expired)
    S->>C: Return error "Unauthorized"





