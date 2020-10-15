book-management-api
Plugins:

Spring Boot 2
Spring Security With Bear Token
MySQL
Mybatis
JPA/Hibernate
Eclipse
Install:

Clone source
Import Maven project at Eclipse
Install maven
Edit your database sever at line 71,72,37 at DatabaseConfig.java
Run BookManagementApplication > Your database sever will be auto create database as name "book"
Api:

Public

Login
Url: http://localhost:8888/api/auth/signin
Method: POST
Body (raw,JSON) : { "email":"admin@gmail.com", "password":"111111" }
All books - Url
