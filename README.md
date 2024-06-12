# Cafe Management System

This project is a backend for a Cafe Management System, built using Spring Boot and Hibernate. The backend provides APIs for managing products, orders, bills, and user authentication.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)

## Features

- Product Management (Add, Update, Delete, View Products)
- Order Management
- Billing System
- User Authentication and Authorization
- PDF Bill Generation
- JSON and File-based Configuration

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL 
- Maven
- JSON
- PDF Generation (iText)
- JWT for Authentication

## Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/cafe-management-system.git
   cd cafe-management-system```

2. **Update the application.properties file**
   ```sh
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.datasource.url=jdbc:postgresql://localhost:5432/cafe
   spring.datasource.username=postgres
   spring.datasource.password=Enter your Database Password

   spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation= true
   spring.jpa.properties.hibernate.format_sql=true

   # Hibernate ddl auto (create, create-drop, validate, update)
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   #setup jwt secret key
   cafe.app.jwtSecret=Enter secret Key Here (You Can Generate it via KeyGenerator in util package).

   # Mail server configuration
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=Enter your Email Id
   spring.mail.password=Enter 16 digit password 
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true

   #logging.level.org.springframework=DEBUG
   #logging.level.com.zaxxer.hikari=DEBUG
```
   
