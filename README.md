# construction-handler-ktor

construction-handler-ktor project provides functionality for maintaining workers and their attendance. 
For accessing the functionality a REST API is available.


## General Functionality 
The construction-handler-ktor addresses a specific use-case where users can efficiently manage their employees and 
maintain daily attendance records. To ensure secure access to their accounts, users must utilize their mobile numbers and
set a 4-digit pin for login. The system incorporates MongoDB as the database to store and manage user and worker information, 
as well as attendance records persistently.

Additionally, the authentication mechanism has been enhanced to include JWT tokens. Users can authenticate by providing 
their mobile number and 4-digit pin, receiving a JWT token upon successful authentication. This token serves as a 
secure means for subsequent API requests.

List of available APIs provided by construction-handler-ktor:

## Requests

### Create User : ( POST : api/user/create )
This request is adding a new user with mobile number and 4 digit pin. The mobile number must be unique across all users.
After successful creation provide JWT token for authentication.

### Get User Info : ( Authenticated GET : api/user/info )
This request helps to get the user account information.

### User Login : ( POST : api/user/info )
With the given mobile number and pin upon validation provide JWT token for authentication.

### ADD Worker : ( Authenticated POST : api/workers/add )
Helps to create worker.

### Get Worker Info : ( Authenticated GET : api/workers/info )
Helps to get individual worker info.

### Get Workers List : ( Authenticated GET : api/workers/list )
Helps to get all workers under user.

### Delete Worker : ( Authenticated DELETE : api/workers/delete )
Helps to delete worker.

### Update Attendance : ( Authenticated POST : api/attendance/info )
Helps to add worker to attendance register.

### Get Attendance list : ( Authenticated GET : api/attendance/list )
Helps to get all attendees list from specific date range.

### Delete Attendance : ( Authenticated DELETE : api/attendance/delete )
Helps to remove the worker from attendance registry.


#Run construction-handler-ktor :

git clone https://github.com/karnasurendra/construction-handler-ktor.git
cd construction-handler-ktor
./gradlew run
