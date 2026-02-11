# Fishing E-Store

A full-stack e-commerce platform for fishing supplies — built with **Spring Boot**, **Vue.js**, and **PostgreSQL**, fully containerized using **Docker**.

# NB! The project will run but without the cloudinary key you will not be able to add products.

## Project Introduction

FIISH E-Store is a web application designed for selling fishing equipment and supplies online.

### Purpose

The purpose is to learn web development and make Sander's dad a website for his business.

---

## Technology Stack

### Backend

- **Java 21** - Programming language
- **Spring Boot 3.5.5** - Application framework
- **Liquibase** - Database migration tool
- **Lombok** - Code generation
- **PostgreSQL 14.1** - Relational database
- **Gradle** - Build tool

### Frontend

- **Vue.js 3** - Progressive JavaScript framework
- **Vite** - Build tool and dev server
- **Axios** - HTTP client
- **Node.js** - JavaScript runtime

### Infrastructure

- **Docker** - Containerization
- **nginx** - Web server (for frontend production)
- **GitLab CI/CD** - Continuous integration and deployment

---

## How to Run the Application

### Prerequisites

- **Docker** and **Docker Compose** installed
- **Java 21** (for manual backend development)
- **Node.js** (for manual frontend development)

#### 1. Start PostgreSQL Database

```bash
cd backend
docker-compose up -d postgres
```

### 1. Create a .env file

Copy over the .env.example file and fill out with the correct postgres information and cloudinary API information.

#### 2. Run Backend Manually

In your IDE or terminal:

```bash
cd backend
./gradlew bootRun
```

The backend will start on http://localhost:8080

#### 3. Run Frontend Manually

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on http://localhost:5173

---

## How to Build the Project

#### Build JAR (Skip Tests)

```bash
cd backend
./gradlew clean build -x test
```

#### Build Docker Image

Build the Docker image:

```bash
cd backend
docker build -t sandro321/veebilehekala:latest .
```

### Build Frontend

```bash
cd frontend
npm install
npm run build
```

#### Build Docker Image

Build the Docker image:

```bash
cd frontend
docker build -t sandro321/veebilehekala-frontend:latest .
```

### Push Docker Images to Registry

If you want to push the images to Docker Hub:

```bash
# Login to Docker Hub
docker login

# Push backend image
docker push sandro321/veebilehekala:latest

# Push frontend image
docker push sandro321/veebilehekala-frontend:latest
```

---

## How to Build and Run Docker Containers

#### Backend Image

First, build the JAR file, then build the Docker image

```bash
cd backend
./gradlew clean build -x test
docker build -t sandro321/veebilehekala:latest .
```

#### Frontend Image

```bash
cd frontend
docker build -t sandro321/veebilehekala-frontend:latest .
```

#### Start Backend Services

```bash
cd backend
docker-compose up -d
```

#### Start Frontend

```bash
cd frontend
docker-compose up -d
```

### Important Notes

- **Network**: The frontend container must be on the `backend_default` network to communicate with the backend
- **Ports**:
  - Backend: `8080`
  - Frontend: `3000`
  - PostgreSQL: `5432`
- **Data Persistence**: Database data is stored in `backend/postgres-data/` volume
- **Image Updates**: After rebuilding images, restart containers with `docker-compose restart` or `docker-compose up -d`

---

##  Authors

**Fishing E-Store Team**

- Markus Käpp
- Sander Roosimäe
- Gregor Gritsenko

Developed with ❤️ using Java, Spring Boot, Vue, and PostgreSQL.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
