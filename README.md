## Uploaded PDF Notes

Faculty PDF notes are stored outside the packaged application resources in `uploads/notes` by default. Set `edumania.upload-dir` in `src/main/resources/application.properties` to choose another persistent directory. Uploaded files are served from `/uploads/notes/`, and download metadata is stored in MongoDB.

## Installation & Setup

### 1. Clone the Repository

Clone the repository from GitHub and navigate to the project directory.

### 2. Open the Project

Open the project in IntelliJ IDEA, Eclipse, or another Java IDE.

### 3. Configure MongoDB

Make sure MongoDB is installed and running locally, or configure a MongoDB Atlas database connection.

### 4. Build the Project

Run the following command from the project root:

```bash
mvn clean package
```

### 5. Run the Application

Start the Spring Boot application using:

```bash
mvn spring-boot:run
```

Alternatively, run the main Spring Boot application class from your IDE.