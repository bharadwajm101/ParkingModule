```markdown
# Parking Slot Management System

## 🚀 Overview

The **Parking Slot Management System** is a modern, microservice-based application designed to efficiently manage parking operations. It focuses on real-time parking slot management, vehicle movement tracking, reservations, and billing. This system is built to be modular, scalable, and easy to maintain by leveraging a microservices architecture with Spring Boot.

This repository specifically focuses on setting up the foundational services that enable this distributed system: a **Discovery Server**, a **Gateway Service**, and the core **Parking Slot Service**.

### Why Microservices?

This project adopts a microservices architecture for several key benefits:
* **Scalability:** Each service can be scaled independently based on its demand. For example, if parking slot lookups are frequent, only the `Parking Slot Service` needs more resources, not the entire application.
* **Modularity & Maintainability:** Services are small, focused, and independently deployable. This makes the codebase easier to understand, develop, and maintain for different teams.
* **Technology Diversity:** While this project primarily uses Java Spring Boot, a microservices approach allows different services to be built with different technologies if needed (though not demonstrated here).
* **Resilience:** If one service fails (e.g., the Billing service), other services (like Parking Slot Management) can continue to operate, minimizing impact on the overall system.

## 🏛️ Architecture & Modules

The system is comprised of three main Spring Boot microservices:

1.  ### **Discovery Server (Eureka Server)**
    * **Role:** Acts as the central **service registry** for all microservices. When a new service starts, it registers itself with the Discovery Server. Other services can then query the Discovery Server to find out where a particular service is running (its IP address and port). This eliminates hardcoding service locations and enables dynamic scaling.
    * **Core Function:** Service registration and lookup.
    * **Key Files:**
        * `discovery-server/src/main/resources/application.properties` (Configuration)
        * `discovery-server/src/main/java/com/parking/discovery_server/DiscoveryServerApplication.java` (Main application class)

2.  ### **Gateway Service (Spring Cloud Gateway)**
    * **Role:** This is the **single entry point** for all client requests into the microservices ecosystem. Instead of clients needing to know the specific addresses of each backend service, they only communicate with the Gateway. The Gateway then intelligently routes requests to the correct service based on predefined rules, also providing cross-cutting concerns like load balancing, security, and rate limiting (though advanced features might not be fully implemented in this initial phase).
    * **Core Function:** Request routing, load balancing, API aggregation.
    * **Key Files:**
        * `gateway-service/src/main/resources/application.properties` (Configuration, including routing rules)
        * `gateway-service/src/main/java/com/parking/gateway_service/GatewayServiceApplication.java` (Main application class)

3.  ### **Parking Slot Service**
    * **Role:** This is the core business logic service for managing parking slots. It handles all operations related to parking slot creation, updates, deletion, and retrieval of real-time occupancy status. It interacts directly with the database to persist and retrieve parking slot information.
    * **Core Function:** CRUD operations and status management for parking slots.
    * **Key Files (Detailed in this repository):**
        * **Controller:** `parking-slot-service/src/main/java/com/parking/parking_slot_service/controller/ParkingSlotController.java`
        * **Service Implementation:** `parking-slot-service/src/main/java/com/parking/parking_slot_service/service/impl/ParkingSlotServiceImpl.java`
        * **Repository:** `parking-slot-service/src/main/java/com/parking/parking_slot_service/repository/ParkingSlotRepository.java`
        * **Entity:** `parking-slot-service/src/main/java/com/parking/parking_slot_service/entity/ParkingSlot.java`
        * **DTOs:** `parking-slot-service/src/main/java/com/parking/parking_slot_service/dto/ParkingSlotDTO.java` and `OccupancyStatusDTO.java`
        * **API Documentation:** Configured via `parking-slot-service/src/main/java/com/parking/parking_slot_service/config/SwaggerConfig.java`
        * **Security Configuration:** `parking-slot-service/src/main/java/com/parking/parking_slot_service/config/SecurityConfig.java`

### Component Interaction

* **Frontend (Conceptual, not in this repo):** An Angular or React frontend (as per LLD) would interact solely with the **Gateway Service**.
* **Gateway Service:** Communicates with the **Discovery Server** to find the addresses of backend services (like the Parking Slot Service). It then forwards client requests to the appropriate backend service.
* **Backend Services (e.g., Parking Slot Service):** Register themselves with the **Discovery Server** upon startup. They handle specific business logic and interact with their respective databases.
* **Database:** A relational database (like MySQL or SQL Server) stores persistent data for each service (e.g., `ParkingSlot` data for the Parking Slot Service).

## 📂 Project Structure

The project is organized into separate modules for each microservice:

```

Parking\_Slot\_Management/
├── discovery-server/
│   ├── src/main/java/com/parking/discovery\_server/DiscoveryServerApplication.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
├── gateway-service/
│   ├── src/main/java/com/parking/gateway\_service/GatewayServiceApplication.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
└── parking-slot-service/
    ├── src/main/java/com/parking/parking\_slot\_service/
    │   ├── config/SwaggerConfig.java
    │   ├── config/SecurityConfig.java
    │   ├── controller/ParkingSlotController.java
    │   ├── dto/ParkingSlotDTO.java
    │   ├── dto/OccupancyStatusDTO.java
    │   ├── entity/ParkingSlot.java
    │   ├── repository/ParkingSlotRepository.java
    │   ├── service/ParkingSlotService.java
    │   └── service/impl/ParkingSlotServiceImpl.java
    ├── src/main/resources/application.properties
    └── pom.xml

````

## 🌊 Data Flow Explained

Understanding how data moves through the system is key to grasping the microservices concept:

1.  ### **Service Registration (Startup)**
    * When the `Gateway Service` and `Parking Slot Service` start up, they send their network location (IP address and port) to the `Discovery Server` (Eureka, running on port `8761`).
    * The `Discovery Server` maintains a registry of all active services and their addresses. This allows services to find each other dynamically.

2.  ### **Client Request Handling (Runtime)**
    * A client (e.g., a web browser or a mobile app) sends an API request (e.g., `GET /api/slots/available`) to the `Gateway Service` (running on port `8090`).
    * The `Gateway Service` inspects the incoming request's URL path (`/api/slots/**`). Based on its internal routing configuration, it identifies that this request needs to be handled by the `Parking Slot Service`.
    * The `Gateway` then consults the `Discovery Server` to get the current network location of an available instance of the `Parking Slot Service`.
    * It forwards the request to the chosen `Parking Slot Service` instance.

3.  ### **Business Logic & Persistence (Parking Slot Service)**
    * The `Parking Slot Service` (running on port `8082`) receives the request.
    * Its `ParkingSlotController` maps the incoming request to the appropriate method in the `ParkingSlotService` (e.g., `getAvailableSlots()`).
    * The `ParkingSlotService` (implementation in `ParkingSlotServiceImpl`) contains the business logic. It interacts with the `ParkingSlotRepository` to perform database operations (e.g., querying the `ParkingSlot` table for slots where `isOccupied = false`).
    * The `Parking Slot Service` uses Spring Data JPA to simplify these database interactions with a MySQL database.

4.  ### **Response Flow**
    * Once the `Parking Slot Service` processes the request and retrieves/manipulates the data, it sends the response (typically as a `ParkingSlotDTO` or a list of `ParkingSlotDTO`s) back to the `Gateway Service`.
    * The `Gateway Service` then forwards this response directly back to the original client.

## 🚦 API Endpoints (Parking Slot Service)

The `Parking Slot Service` exposes a comprehensive set of RESTful API endpoints for managing parking slots. All endpoints are prefixed with `/api/slots` and are documented via Swagger UI.

* **Add a Parking Slot**
    * **Method:** `POST`
    * **Endpoint:** `/api/slots`
    * **Description:** Creates a new parking slot record in the system.
    * **Request Body Example (ParkingSlotDTO):**
        ```json
        {
          "type": "4W",
          "isOccupied": false,
          "location": "Zone B, Level 2, Slot 15"
        }
        ```
    * **Response:** Returns the created `ParkingSlotDTO` with its `slotId`.

* **Update a Parking Slot**
    * **Method:** `PUT`
    * **Endpoint:** `/api/slots/{slotId}`
    * **Description:** Updates the full details of an existing parking slot identified by its `slotId`.
    * **Path Variable:** `slotId` (e.g., `PUT /api/slots/1`)
    * **Request Body Example (ParkingSlotDTO):**
        ```json
        {
          "type": "4W",
          "isOccupied": true,
          "location": "Zone B, Level 2, Slot 15"
        }
        ```
    * **Response:** Returns the updated `ParkingSlotDTO`.

* **Delete a Parking Slot**
    * **Method:** `DELETE`
    * **Endpoint:** `/api/slots/{slotId}`
    * **Description:** Removes a parking slot from the system.
    * **Path Variable:** `slotId` (e.g., `DELETE /api/slots/1`)
    * **Response:** HTTP 204 No Content (indicating successful deletion with no response body).

* **Get All Parking Slots**
    * **Method:** `GET`
    * **Endpoint:** `/api/slots`
    * **Description:** Retrieves a list of all parking slots currently configured in the system, regardless of their occupancy status.
    * **Response:** A list of `ParkingSlotDTO` objects.

* **Get Available Parking Slots**
    * **Method:** `GET`
    * **Endpoint:** `/api/slots/available`
    * **Description:** Retrieves a list of all parking slots that are currently marked as `isOccupied: false`.
    * **Response:** A list of `ParkingSlotDTO` objects.

* **Get Occupancy Status**
    * **Method:** `GET`
    * **Endpoint:** `/api/slots/occupancy-status`
    * **Description:** Provides a summary of the current parking lot status, showing the count of occupied and available slots.
    * **Response Example (OccupancyStatusDTO):**
        ```json
        {
          "occupiedCount": 50,
          "availableCount": 120
        }
        ```

* **Get Parking Slots by Type**
    * **Method:** `GET`
    * **Endpoint:** `/api/slots/type/{type}`
    * **Description:** Retrieves a list of parking slots filtered by their type (e.g., "2W" for two-wheelers or "4W" for four-wheelers). The type comparison is case-insensitive.
    * **Path Variable:** `type` (e.g., `GET /api/slots/type/4W`)
    * **Response:** A list of `ParkingSlotDTO` objects.

* **Get a Parking Slot by ID**
    * **Method:** `GET`
    * **Endpoint:** `/api/slots/{slotId}`
    * **Description:** Retrieves the detailed information for a specific parking slot using its unique ID.
    * **Path Variable:** `slotId` (e.g., `GET /api/slots/1`)
    * **Response:** A single `ParkingSlotDTO` object.

* **Change Occupancy Status**
    * **Method:** `PATCH`
    * **Endpoint:** `/api/slots/{slotId}/occupancy`
    * **Description:** Allows for partial update of a parking slot's status, specifically changing only its `isOccupied` flag. This is crucial for integration with Vehicle Entry & Exit Logging.
    * **Path Variable:** `slotId` (e.g., `PATCH /api/slots/1/occupancy`)
    * **Query Parameter:** `isOccupied` (e.g., `?isOccupied=true` or `?isOccupied=false`)
    * **Example Usage:** `PATCH /api/slots/123/occupancy?isOccupied=true`
    * **Response:** The updated `ParkingSlotDTO` object.

## 🛠️ Key Technologies & Concepts

* **Spring Boot:** Framework for building production-ready, stand-alone Spring applications with minimal configuration.
* **Spring Cloud Netflix Eureka:** Provides the Discovery Server functionality for service registration and discovery.
* **Spring Cloud Gateway:** Provides an API Gateway for routing and cross-cutting concerns.
* **Spring Data JPA:** Simplifies database access and operations by reducing boilerplate code. It maps Java objects (Entities) to database tables.
* **REST API:** A set of rules for how components in a distributed system communicate. Data is exchanged using standard HTTP methods (GET, POST, PUT, DELETE, PATCH) and typically JSON format.
* **DTO (Data Transfer Object):** Simple objects used to transfer data between different layers of the application or over the network. They help in separating the internal entity model from the external API representation.
* **Lombok:** A Java library that automatically generates boilerplate code (getters, setters, constructors, etc.), making code cleaner and more concise.
* **Swagger (OpenAPI 3):** An industry-standard specification and toolset for designing, building, documenting, and consuming RESTful APIs. It generates interactive API documentation.
* **MySQL:** A popular open-source relational database management system used for persistent data storage.

## ▶️ How to Run the Project

To get this microservices system up and running, you will need to start each service independently in the specified order.

**Prerequisites:**
* Java Development Kit (JDK) 17 or newer installed.
* Apache Maven installed.
* A running MySQL instance with a configured database for the `Parking Slot Service`. You'll need to update `src/main/resources/application.properties` in `parking-slot-service` with your database connection details (URL, username, password).

**Steps:**

1.  **Start the Discovery Server:**
    * Open a terminal or command prompt.
    * Navigate to the `discovery-server` directory:
        ```bash
        cd Parking_Slot_Management/discovery-server
        ```
    * Run the application:
        ```bash
        mvnw spring-boot:run
        ```
    * The Eureka server will typically be available on `http://localhost:8761`. You can visit this URL in your browser to see registered services.

2.  **Start the Parking Slot Service:**
    * Open a **new** terminal or command prompt.
    * Navigate to the `parking-slot-service` directory:
        ```bash
        cd Parking_Slot_Management/parking-slot-service
        ```
    * Run the application:
        ```bash
        mvnw spring-boot:run
        ```
    * This service will run on port `8082` and register itself with the Discovery Server.

3.  **Start the Gateway Service:**
    * Open another **new** terminal or command prompt.
    * Navigate to the `gateway-service` directory:
        ```bash
        cd Parking_Slot_Management/gateway-service
        ```
    * Run the application:
        ```bash
        mvnw spring-boot:run
        ```
    * The Gateway will run on port `8090` and register itself with the Discovery Server. All your client API requests will go through this port.

4.  **Access API Documentation (Swagger UI):**
    * Once all three services are running, open your web browser and navigate to:
        `http://localhost:8090/swagger-ui.html` (or `http://localhost:8090/swagger-ui/index.html`)
    * This will display the interactive Swagger UI for your `Parking Slot Service` API, allowing you to explore all endpoints and even test them directly from the browser.

## ✅ Assumptions and Constraints

* **Single Vehicle per Slot:** Each parking slot is designed to accommodate only one vehicle at a time.
* **Manual/Scanner Operations:** Vehicle entry and exit operations are assumed to be initiated manually (e.g., by staff) or via an external scanner system that communicates with the `Parking Slot Service`'s API.
* **Out of Scope (Current Phase):**
    * SMS/Email notification system.
    * Direct integration with third-party payment gateways (payment processing is conceptual within the `Billing and Payments` module, but actual external API calls are not implemented in this phase).
    * A full user interface (frontend application) is not part of this repository; it would consume these APIs.

## 🔒 Security Notes (Important!)

* **Development-Time Security:** The `SecurityConfig.java` currently has `"/api/slots/**"` set to `permitAll()`. This means all parking slot API endpoints are publicly accessible without authentication.
* **Production Readiness:** For a production environment, this `permitAll()` configuration is **not secure**. You would typically implement robust authentication (e.g., using JWTs in conjunction with your User Management module) and authorization (role-based access control) to restrict access to these endpoints based on user roles (e.g., only admins can add/remove slots, only authenticated users can view available slots). This setup allows for easier development and testing of individual services.
````
