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


