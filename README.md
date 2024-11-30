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


## 1. Authentication Endpoints

### 1.1 Login Endpoint
- **Description:** Authenticates a user and returns a login response.
- **URL:** `POST /api/auth/login`
- **Image:** {Insert Image Path Here}

### 1.2 Register Endpoint
- **Description:** Registers a new user in the system.
- **URL:** `POST /api/auth/register`
- **Image:** {Insert Image Path Here}

---

## 2. Task Management Endpoints

### 2.1 Add Task Endpoint
- **Description:** Adds a new task for a specific user.
- **URL:** `POST /taskManager/`
- **Image:** {Insert Image Path Here}

### 2.2 Get All Tasks for a Specific Date
- **Description:** Retrieves all tasks for a specific date.
- **URL:** `GET /taskManager/{date}`
- **Image:** {Insert Image Path Here}

### 2.3 Update Task Endpoint
- **Description:** Updates an existing task for a specific date and task ID.
- **URL:** `PUT /taskManager/{date}/{id}`
- **Image:** {Insert Image Path Here}

### 2.4 Delete Task Endpoint
- **Description:** Deletes a specific task for a given date and task ID.
- **URL:** `DELETE /taskManager/{date}/{id}`
- **Image:** {Insert Image Path Here}

---

## 3. Manager Endpoints

### 3.1 Get All Users Under Manager's Supervision
- **Description:** Retrieves a list of all users under the manager's supervision.
- **URL:** `GET /manager/users`
- **Image:** {Insert Image Path Here}

### 3.2 Update Task Description for User
- **Description:** Allows the manager to update the task description for a user under their supervision.
- **URL:** `PUT /manager/tasks/{date}/{id}`
- **Image:** {Insert Image Path Here}

---

## 4. Admin Endpoints

### 4.1 Register Manager Endpoint
- **Description:** Registers a new manager in the system.
- **URL:** `POST /admin/register-manager`
- **Image:** {Insert Image Path Here}

### 4.2 Get All Users with Role 'USER'
- **Description:** Retrieves a list of all users with the role 'USER'.
- **URL:** `GET /admin/users`
- **Image:** {Insert Image Path Here}

### 4.3 Get All Users with Role 'MANAGER'
- **Description:** Retrieves a list of all users with the role 'MANAGER'.
- **URL:** `GET /admin/managers`
- **Image:** {Insert Image Path Here}

### 4.4 Assign Manager to Users
- **Description:** Assigns a manager to multiple users.
- **URL:** `POST /admin/assign-manager/{id}`
- **Image:** {Insert Image Path Here}
