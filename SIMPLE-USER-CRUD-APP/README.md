# User Management System

## Table of Contents
- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Features](#features)
- [Setup Instructions](#setup-instructions)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [API Endpoints](#api-endpoints)
- [Usage](#usage)
- [License](#license)

---

## Introduction
The **User Management System** is a full-stack application that enables efficient management of user data. It features secure user authentication, JWT-based authorization, and intuitive CRUD operations through a REST API and Material-UI-powered React frontend.

---

## Technologies Used

### Backend:
- **Spring Boot** (Java Framework)
- **Spring Security** for Authentication and Authorization
- **Spring Data JPA** for Database Interaction
- **JWT (JSON Web Tokens)** for Secure Authentication
- **PostgreSQL Database** ( Database for Development)
- **pgAdmin4** (for PostgreSQL UI)
- **Maven** for Dependency Management

### Frontend:
- **React.js**
- **Material-UI (MUI)** for UI Components
- **React Router DOM** for Navigation

---

## Project Structure

### Backend
```
C:.
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───user_management
│   │   │           │   ServletInitializer.java
│   │   │           │   UserManagementSpringBootApplication.java
│   │   │           │
│   │   │           └───user_management_spring_boot
│   │   │               ├───config
│   │   │               ├───controller
│   │   │               ├───entity
│   │   │               ├───filter
│   │   │               ├───repo
│   │   │               └───service
│   │   └───resources
│   └───test
└───target
```

- **Config**: Contains Spring Security and CORS configurations.
- **Controller**: Handles API endpoints.
- **Entity**: Represents data models (e.g., `UserInfo`, `AuthRequest`).
- **Filter**: Includes JWT authentication filters.
- **Repo**: Data access layer for `UserInfo`.
- **Service**: Business logic and JWT utilities.

### Frontend
```
C:.
├───src
│   ├───component
│   │   ├───Navbar.jsx
│   │   ├───LoginForm.jsx
│   │   ├───HomePage.jsx
│   ├───service
│   │   ├───UserService.jsx
│   ├───App.js
│   ├───index.js
│   └───styles.css
└───public
```
- **Component**: Reusable React components.
- **App.js**: Main app logic and routing.
- **index.js**: Entry point for the React application.

---

## Features

### Backend
- JWT Authentication and Authorization
- Secure Password Management
- CRUD Operations for Users
- CORS Support
- Database Integration using Spring Data JPA

### Frontend
- Responsive Design with Material-UI
- User Authentication with JWT
- Add, View, Update, and Delete Users
- Search and Filter Users

---

## Setup Instructions

### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/pushkar666/SIMPLE-USER-CRUD-APP.git
   ```
2. Navigate to the backend directory:
   ```bash
   cd USER-MANAGEMENT-SPRING-BOOT
   ```
3. Update `application.properties` (if required):
   ```properties
   spring.application.name=user-management-spring-boot
   # Database Config (Consider using environment variables for username and password)
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.datasource.url=jdbc:postgresql://localhost:5432/user
   spring.datasource.username=postgres
   spring.datasource.password={YOUR PASSWORD}

   # ORM Config
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.hibernate.ddl-auto=update

   spring.security.user.password={YOUR PASSWORD}
   
   ```
4. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```
5. The application runs at `http://localhost:8080`.

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd user-management-react-app/
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```
4. The frontend runs at `http://localhost:3000`.

---

## API Endpoints

### Authentication
- **POST** `auth/generateToken`: Authenticate user and generate JWT.

### User Management
- **GET** `/auth/users`: Retrieve all paginated users.
- **POST** `/auth/addNewUser`: Add a new user.
- **POST** `/auth/queryUsers`: Retrieve queried paginated users.

---

## Usage
1. Log in using the `/generateToken` endpoint.
2. Perform user operations via the frontend or API.

---

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.
