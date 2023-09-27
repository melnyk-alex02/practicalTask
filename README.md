# practicalTask

The following endpoints are provided by the UserController:

### Search Users by Date of Birth

- **Endpoint**: `/api/users/search`
- **HTTP Method**: GET
- **Parameters**:
  - `fromDate`: The start date for the date range search (ISO date format).
  - `toDate`: The end date for the date range search (ISO date format).
- **Description**: Retrieves a list of users whose date of birth falls within the specified date range.

### Create User

- **Endpoint**: `/api/users`
- **HTTP Method**: POST
- **Request Body**: UserCreateDTO (User creation data)
- **Description**: Creates a new user based on the provided user data.

### Delete User

- **Endpoint**: `/api/users/{id}`
- **HTTP Method**: DELETE
- **Path Variable**: `id` (User ID)
- **Description**: Deletes a user with the specified ID.

### Update User

- **Endpoint**: `/api/user`
- **HTTP Method**: PUT
- **Request Body**: UserUpdateDTO (User update data)
- **Description**: Updates an existing user based on the provided user update data.

### Update Several Users

- **Endpoint**: `/api/users`
- **HTTP Method**: PUT
- **Request Body**: List of UserUpdateDTOs (User update data for multiple users)
- **Description**: Updates multiple users based on the provided user update data.

###Database
I used mysql with user loaded local instance with username = bestuser, passwword = bestuser
