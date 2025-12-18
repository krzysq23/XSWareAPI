# XSWareAPI

**XSWareAPI** is a backend application written in **Java** using **Spring Boot**.  
It serves as a **secure communication layer** between the frontend application **XSWareWeb**
(React, *Saldo Planner*) and the backend data service **XSWareDBService** (Kotlin).

The application exposes REST APIs secured with **Spring Security** and **JWT authentication**,
acting as an API gateway and authorization layer for client–server communication.


## 🏗️ Architecture Overview

![apps_architecture](https://xsware.pl/assets/img/other/apps_architecture_2.png)


## ✨ Key Features

- Java 17+ with **Spring Boot**
- **Spring Security** with **JWT authentication**
- Stateless REST API
- Secure communication between frontend and database service
- Environment-based configuration (`local`, `dev`, `prod`)
- Built with **Gradle**
- Ready for Docker / CI/CD environments


## 🛠️ Technology Stack

| Layer        | Technology |
|-------------|------------|
| Language    | Java |
| Framework   | Spring Boot |
| Security    | Spring Security + JWT |
| Build Tool  | Gradle |
| API Style   | REST |
| Frontend    | XSWareWeb (React – Saldo Planner) |
| Data Layer  | XSWareDBService (Kotlin) |


## ⚙️ Configuration

The application uses **Spring profiles** and **environment variables**.  
No sensitive data is stored in the repository.

### Spring Profiles
- `dev` – development environment
- `prod` – production environment


### Required Environment Variables

```bash
JWT_SECRET=your-secret-key
JWT_ACCESS_EXP_MS=900000
JWT_REFRESH_EXP_MS=604800000
```

### 🚀 Running the Application

Local development
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

Build JAR
```bash
./gradlew clean build
```

Run JAR
```bash
java -jar build/libs/xsware-api.jar \
  --spring.profiles.active=prod
```

### 🔐 Security

- Authentication based on JWT tokens
- Stateless session handling
- Authorization handled by Spring Security filters
- Tokens are validated on every request

### 🔄 Communication Flow

1. XSWareWeb authenticates the user via XSWareAPI
2. XSWareAPI issues a **JWT token**
3. Frontend sends authenticated requests with JWT
4. XSWareAPI validates the token
5. XSWareAPI communicates with **XSWareDBService** to fetch or persist data
6. Response is returned to the frontend

### 📦 Project Structure

```
src/main/java
  └── pl.xsware
    ├── api
    ├── config
    ├── domain
    └── util

src/main/resources
  ├── application.yml
  ├── application-dev.yml
  └── application-prod.yml
```

### 🧪 Testing

Run tests using:
```bash
./gradlew test
```

### 🧑‍💻 Related Projects

- **XSWareWeb** – React frontend (Saldo Planner)
- **XSWareDBService** – Kotlin backend service for database access