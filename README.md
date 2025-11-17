# 🚀 Job Application Platform — Spring Boot Microservices

A fully‐functional microservices-based job application platform built using Spring Boot, inspired by the tutorial series from **Faisal Memon (EmbarkX)**.  
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

### Run Locally  
1. Clone the repository:  
   ```bash
   git clone https://github.com/wroxtaaar/Microservices_JobApp.git
   cd Microservices_JobApp

2. Start PostgreSQL (via Docker) if using production mode. Or skip for H2.

---

3. Build and run each module/service:

cd service-reg && ./mvnw spring-boot:run
cd config-server && ./mvnw spring-boot:run
cd gateway && ./mvnw spring-boot:run
cd companyms && ./mvnw spring-boot:run
cd jobms && ./mvnw spring-boot:run
cd reviewms && ./mvnw spring-boot:run
