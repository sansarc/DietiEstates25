# DietiEstates25

![logo](src/main/resources/static/images/logo.png)

**DietiEstates25** is a modern Real Estate Management platform built with **Java 21**, **Spring Boot**, and **Vaadin Flow**. This repository contains the **frontend UI**, which allows users to browse ads, place bids, and manage real estate agencies, communicating with a separate REST API backend.

---

## 🚀 Live Demo & Documentation

* Live Application: [Link to Demo](http://13.39.106.216:8080/)
* Project Documentation: [Link to Documentation](https://drive.google.com/file/d/1zLKVpf4YuNPdB1Gs9EugkauPbv0Zs-z0/view)

### 🔌 API
* API Repository: [Link to Backend](https://github.com/luigisabatino/API_DietiEstates25)
* Swagger Documentation: [Link to Swagger](http://51.45.7.98:8082/swagger-ui/index.html)
---

## 🛠 Tech Stack

* **Language**: Java 21
* **Framework**: Spring Boot
* **UI Framework**: Vaadin Flow (Server-side Java UI)
* **Build Tool**: Maven
* **Containerization**: Docker & AWS ECR
* **External Integrations**: Leaflet (Maps), SplideJS (Carousels)
---

## 🏗 Architecture

Layered architecture:
* Views (`/views`) – UI pages (e.g., HomeView)
* Services (`/services`) – Business logic & API communication
* DTOs (`/dto`) – Data Transfer Objects for mapping JSON responses
* Utils (`/utils`) – Helper classes for notifications, formatting, and UI factories
* UI Components (`/ui_components`) – Reusable Vaadin components
---

## ⚙️ Configuration

The UI connects to the backend API using environment variables:

| Variable     | Description                 | Default              |
|--------------|-----------------------------|----------------------|
| API_BASE_URL | URL of the backend REST API | http://localhost:8082 |
| PORT         | Port for the UI server      | 8080                 |


> Note: For public use, replace `API_BASE_URL` with the URL of a deployed backend instance.

## 💻 Running Locally 
Prerequisites:
* JDK 21
* Maven 3.9+
* Access to a running backend API (local or deployed)

You can run the frontend locally via Maven. Make sure `API_BASE_URL` points to API address:

On Linux/macOS:
```bash
API_BASE_URL=http://51.45.7.98 mvn clean spring-boot:run
```
or, to create a frontend-optimized production build:
```bash
API_BASE_URL=http://51.45.7.98 mvn clean package -Pproduction -DskipTests 
```

On Windows:
```cmd
set API_BASE_URL=http://51.45.7.98 && mvn clean spring-boot:run
```
or
```cmd
set API_BASE_URL=http://51.45.7.98 && mvn clean package -Pproduction -DskipTests
```

Without a backend, the UI will start but won’t display data.

---

## 🐳 Docker Deployment

You can build and run the UI using the provided scripts.

On Linux/macOS:
```bash
chmod +x ./docker-build.sh
./docker-build.sh
```

On Windows:
```cmd
./docker_build.bat
```
---

## 📂 Project Structure

    src/main/java/com/dieti/dietiestates25
    ├── annotations/       # Custom security annotations (e.g., @ManagerOnly)
    ├── constants/         # App-wide constants and API endpoints
    ├── dto/               # Data Transfer Objects
    ├── observers/         # Event listeners (Theme changes, Bid actions)
    ├── services/          # HTTP Request handlers and Logic
    ├── ui_components/     # Reusable Vaadin components (Cards, Forms)
    ├── utils/             # Static utility classes
    └── views/             # Application pages (Home, Profile, Upload, etc.)