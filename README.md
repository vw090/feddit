# Feddit Backend
## Table of Contents
* [Description](#description)
* [Requirements](#requirements)
* [Instructions](#instructions)
* [Technologies Utilized](#technologies-utilized)
* [Contributions](#contributions)
* [Questions](#questions)
* [Future Features](#future-features)
## Description
This is the backend for feddit, a project by Victor Weinert.

Similar to Reddit

## Requirements
- Docker

OR

- At least java 17
- Maven
- Postgres DB

## Instructions
The easiest way to run this is to use the feddit-compose repository's compose. All you need is docker.
The instructions are in that repository @ https://github.com/vw0389/feddit-compose

Either way you'll need to set several shell variables, mostly pertaining to the postgres
### Variables
- `${SPRING_SECURITY_USER_PASSWORD}` = at least 32 character password
- `${POSTGRES_HOST}`
- `${POSTGRES_PORT}`
- `${POSTGRES_DB}`
- `${POSTGRES_USER}`
- `${POSTGRES_PASSWORD}`

### Running
`./mvnw spring-boot:run`
## Technologies Utilized
* Spring
* Spring ORM
* Spring Boot
* Spring Boot Web
* Spring Boot JPA
* Spring Boot Security
* ModelMapper
* Hibernate
* Hibernate Validator
* Project Lombok
* jjwt
* PostgresSQL
## Contributions

**Victor Weinert** 
## Questions
If you have any questions email me at  me@vweinert.com
