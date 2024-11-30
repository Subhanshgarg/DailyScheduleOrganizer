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
