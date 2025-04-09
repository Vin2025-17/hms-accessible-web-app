# HMS – Hotel Management System (Backend Only)

This is a **backend-only** Hotel Management System built using **Java Spring Boot**, designed to handle hotel operations such as room bookings, user management, and authentication. This backend can be integrated with any frontend of your choice (React, Angular, etc.).

---

## 📦 Features

- 🛏️ Room booking management
- 👤 User and admin authentication (Spring Security)
- 🗃️ MySQL database integration
- 🔐 Role-based access control
- ⚙️ MVC architecture
- 🔄 RESTful APIs

---

## ⚙️ Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Maven**

---

## 📁 Project Structure

HMS-Accessible-Git/ ├── .mvn/ # Maven wrapper files ├── src/ │ └── main/ │ ├── java/ │ │ └── com/ │ │ └── hmsapp/ │ │ ├── controller/ # REST controllers │ │ ├── model/ # Entity classes │ │ ├── repository/ # Spring Data JPA repositories │ │ ├── service/ # Business logic │ │ └── HMSApplication.java # Spring Boot main class │ └── resources/ │ ├── application.properties # Configuration file │ └── templates/ # (Optional) Template files ├── target/ # Compiled files (generated) ├── mvnw # Maven wrapper ├── mvnw.cmd # Maven wrapper (Windows) ├── pom.xml # Maven project configuration └── .gitignore


---

## 📌 Key Features

- ✅ **User Management** – register, login, role-based access
- ✅ **Room Management** – view, create, update, delete rooms
- ✅ **Booking System** – handle bookings with validation
- ✅ **Feedback Module** – user feedback system
- ✅ **Admin Features** – manage rooms, bookings, users
- ✅ **RESTful API Design** – clean architecture using layered principles

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or later
- Maven
- MySQL (configured and running)

### Setup & Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Vin2025-17/hms-accessible-web-app.git
   cd hms-accessible-web-app/HMS-Accessible-Git
  🧠 Application Layers
Controller: Handles API requests

Service: Core business logic and rules

Repository: JPA-based DB access

Model: Java classes mapped to DB tables

💡 Future Improvements
 Add frontend (React/Angular)

 Add JWT-based authentication

 Docker support

 API documentation with Swagger

 CI/CD using GitHub Actions

 Add unit/integration tests

 Fully responsive accessible frontend

👨‍💻 Author

Vinayaka S Kudva



