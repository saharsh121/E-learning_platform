# E-Learning Platform

A full-stack **E-Learning Platform** developed using **Java Spring Boot** and **MongoDB**. The platform provides a centralized environment where users can access educational content, manage courses, and interact with learning resources through a web-based application.

## Features

* User registration and authentication
* Course creation and management
* Browse and access available courses
* Student enrollment in courses
* Manage learning content
* User and course management
* MongoDB database integration
* Secure and scalable backend architecture

##Technologies Used

* **Java**
* **Spring Boot**
* **Spring Data MongoDB**
* **MongoDB**
* **Maven**
* **HTML/CSS/JavaScript**

## Architecture

The application follows a layered Spring Boot architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MongoDB
```

* **Controller Layer** – Handles HTTP requests and API endpoints.
* **Service Layer** – Contains the application's business logic.
* **Repository Layer** – Handles communication with MongoDB.
* **MongoDB** – Stores users, courses, and other application data.

## Database

The application uses **MongoDB**, a NoSQL database, for persistent data storage.

MongoDB is used to manage application data such as:

* Users
* Courses
* Enrollments
* Learning content
* Other platform-related information

## Uploaded PDF notes

Faculty PDF notes are stored outside the packaged application resources in
`uploads/notes` by default. Set `edumania.upload-dir` in
`src/main/resources/application.properties` to choose another persistent
directory. Uploaded files are served from `/uploads/notes/` and download
metadata is stored in MongoDB.

## ⚙️ Installation & Setup
### 1. Clone the repository
### 2. Open the project
Open the project in **IntelliJ IDEA**, **Eclipse**, or another Java IDE.
### 3. Configure MongoDB
Make sure MongoDB is installed and running locally or use a MongoDB Atlas database.
### 4. Build the project
### 5. Run the application

## 📌 Project Purpose

The main objective of this project is to develop a **web-based E-Learning Management System** that simplifies the process of managing and accessing educational content. The project demonstrates the use of **Spring Boot for backend development, RESTful APIs for communication, and MongoDB for flexible data storage**.

## 🔮 Future Enhancements

* Online video lectures
* Progress tracking
* Online quizzes and assessments
* Course completion certificates
* Student dashboard
* Instructor dashboard
* Payment integration
* Role-based access control
* Course reviews and ratings

## Author

**Saharsh Ojha**
