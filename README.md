# Authentication Application

Authentication APIs.

## Installation

1. First clone the GitHub Repository by pasting the command given below in the terminal.
```bash oc
git clone https://github.com/Ratnesh2003/AuthenticationAPIs.git
```
2. In application.properties file, you can modify username and password for the MySQL. Currently, it's set to username="root" and password="root".
3. If you have opened the project on IntelliJ, click on the maven button to install the dependencies.
4. This project uses Java 17 and Spring Boot 3.0.6 . Make sure to have JDK 17 for best working.
5. Run the "AuthenticationApplication" file to start the application.



## Usage

Here is the Postman Collection link for the APIs: [Authentication APIs](https://api.postman.com/collections/20949772-bee915c5-f1ab-4358-93a1-38c04c70b3e3?access_key=PMAT-01H6ZPF7TWRGQF7JW5ADB34RQA). 

You can import the Collection by opening the Postman --> Hamburger menu --> File --> Import
Then paste the link given above to import the collection.

## APIs
1. Register (POST)
```
localhost:8080/api/auth/signup
```
```json
{
  "username": "Ratnesh Mishra",
  "email": "ratneshmishrarulz2003@gmail.com",
  "password": "Abcd@1234",
  "roles": ["ROLE_USER, ROLE_ADMIN"]
}
```
2. Login (POST)
```
localhost:8080/api/auth/login
```
```json
{
    "email": "ratneshmishrarulz@gmail.com",
    "password": "Abcd@1234"
}
```

3. User Route (GET)
```
localhost:8080/api/user
```
Authorization Header:
```
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYXRuZXNobWlzaHJhcnVsekBnbWFpbC5jb20iLCJpYXQiOjE2ODM4OTcxNDYsImV4cCI6MTY4NjQ4OTE0Nn0.vWX1Zojb7Y6DyqpOOJFZrtny3a1XUIvvV1-P6o3Lj5nmzJdBuIOLRp1CgQ7m_HzOHdcDnfpYNKTYpwak_J8fyQ
```
4. Admin Route (GET)
```
localhost:8080/api/admin
```
Authorization Header:
```
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYXRuZXNobWlzaHJhcnVsekBnbWFpbC5jb20iLCJpYXQiOjE2ODM4OTcxNDYsImV4cCI6MTY4NjQ4OTE0Nn0.vWX1Zojb7Y6DyqpOOJFZrtny3a1XUIvvV1-P6o3Lj5nmzJdBuIOLRp1CgQ7m_HzOHdcDnfpYNKTYpwak_J8fyQ
```
5. Logout Route (POST)
```
localhost:8080/api/auth/logout/eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYXRuZXNobWlzaHJhcnVsejIwMDNAZ21haWwuY29tIiwiaWF0IjoxNjkxMTMwOTIzfQ.rTWg298J0G_0VLpQunES6YRRnR-S-8ZqKCamq0GDo-z8K53TBfSPyHCFKNVW9q3d1-r-lD-hNVg1ODKuthnJiA
```


## Flow

1. First you must register your self with the Register API.
2. You can now login using the Login API and get your access token in response.
3. The token will be stored in the database for 5 days, after that it will be cleared using a Scheduler. 
4. Scheduler checks every 1 hour to clear all the tokens which were created more than 5 days ago.
5. To check the Auth tokens, you can call the User and Admin Route API, you will have to add token in header of the request.
6. You can access the APIs based on your roles.
7. After hitting the logout route, your token would be cleared from the database and you'll have to login again to access the routes.

## Note
1. Possible values for roles are "ROLE_ADMIN" and "ROLE_USER".
2. I have not covered all the possibilities in the test cases, but provided most of the tests.
