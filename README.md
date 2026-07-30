# 🚀 Job Application Platform — Spring Boot Microservices

A fully‐functional microservices-based job application platform built using Spring Boot. 
This project demonstrates a real-world microservices architecture: service discovery, API gateway, resiliency, messaging, monitoring, and containerized deployment.

---

## 📸 Architecture Overview  
The application started as a monolith and was refactored into multiple independent services:  
- 🏢 **Company Service**  
- 💼 **Job Service**  
- ⭐ **Review Service**

Each service is containerized and communicates via REST using OpenFeign (and sometimes RestTemplate).

---

## 🧩 Microservices Structure  
The project contains the following modules:

| Module            | Description                                           |
|-------------------|-------------------------------------------------------|
| `service-reg`     | Service Registry (Eureka)                             |
| `config-server`   | Centralized configuration server (Spring Cloud)      |
| `gateway`         | API Gateway (Spring Cloud Gateway)                    |
| `companyms`       | Company microservice                                  |
| `jobms`           | Job microservice                                      |
| `reviewms`        | Review microservice                                   |

---

## 🔧 Key Features  
- **Spring Boot** – Lightweight microservices  
- **Spring Cloud** – Distributed system support  
- **Service Discovery** – Eureka Server 🚀  
- **API Gateway** – Spring Cloud Gateway (routing, filtering) 🌐  
- **Inter-Service Communication** – OpenFeign 🔗 + RestTemplate  
- **Resilience & Fault-Tolerance** – Resilience4j (circuit breaker, retries, rate-limiting) 🛡  
- **Messaging** – RabbitMQ for asynchronous events 📨  
- **Databases**:  
  - Used **H2** for quick local setup 💾  
  - Switched to **PostgreSQL** via Docker for persistence 🐘  
- **Data Access** – Spring Data JPA for repositories & entities 🗂  
- **Observability / Monitoring** – Actuator + Zipkin for tracing and metrics 👁  
- **Deployment** – Docker containers for each service 📦  
- **Design Pattern** – Factory Pattern (object creation decoupling & modularity)  

---

## 📦 Tech Stack  

Spring Boot | Spring Cloud | OpenFeign | JPA | PostgreSQL | H2 | RabbitMQ
Docker | Resilience4j | Spring Cloud Gateway | Eureka | Zipkin
Java 17+ (or your version)


---

## 📂 Getting Started  
### Prerequisites  
- Java JDK 17+  
- Docker & Docker Compose  
- Git  

### Frontend UI
A lightweight dashboard is included in the UI folder so you can browse the platform in a browser.

Run it with:
```bash
cd ui
python3 -m http.server 3000
```

Then open http://localhost:3000 for the local static server, or http://localhost:3100 when using Docker Compose.

If you are using GitHub Codespaces, open the forwarded port for the UI (3000) and the gateway (8080). The UI will automatically target the forwarded gateway URL from the current host.

### Run Locally  
1. Clone the repository:  
   ```bash
   git clone https://github.com/wroxtaaar/Microservices_JobApp.git
   cd Microservices_JobApp


2. Use Docker
   ```bash
    docker-compose up --build
   ```

## OR

  Build and run each module/service:

   ```bash
    cd service-reg && ./mvnw spring-boot:run
    cd config-server && ./mvnw spring-boot:run
    cd gateway && ./mvnw spring-boot:run
    cd companyms && ./mvnw spring-boot:run
    cd jobms && ./mvnw spring-boot:run
    cd reviewms && ./mvnw spring-boot:run
   ```


  
   
3. Access the API Gateway at http://localhost:8080 (or configured port) and use the REST endpoints for each service.

---

## 📊 Learning Outcomes

- From this project I learned:
- Microservices communication patterns 🔄
- API Gateway routing & filtering 🌐
- Fault tolerance & resilience patterns 🛡
- Distributed tracing and observability 👁
- Database migration: H2 → PostgreSQL 💾
- Containerization and deployment with Docker 🐳

---

## 🎯 What’s Next?

- ☸ Kubernetes — Deploy microservices on Kubernetes for orchestration and scalability
- 🔧 Full CI/CD pipelines for automated builds & deployments
- 🔐 Add Spring Security across services for authentication & authorization
- 📈 Integrate Prometheus + Grafana for monitoring metrics and dashboards


