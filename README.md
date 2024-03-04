# TheKnife Restaurant Reservation System

Welcome to TheKnife, an API for restaurant search, reservation, and feedback.

Built with Java 21 and utilizing the Spring Boot 3.2.2 framework, PostgreSQL for data storage, Quarkus for integration with external APIs, and image storage.

## Features:
- Restaurant Search: Easily locate nearby restaurants based on user preferences.
- Reservation Management: Streamlined reservation process for users.
- User Feedback: Gather reviews, ratings, and photos from users to enhance restaurant recommendations.
- External API Integration: Access weather forecasts, nearby parking information, and restaurant facade images.

## Installation:
1. Clone the repository:
<pre>
git clone https://github.com/mtsluis/the-knife.git
cd the-knife
</pre>

2. Database Configuration
   The project uses PostgreSQL as the database. Make sure you have a running PostgreSQL server and update the database configurations in the **application.properties** file.


3. Compile and run the project:

<pre>
mvn clean install
mvn spring-boot:run
</pre>

4. The system will be available at http://localhost:8080.

## Browser URL
* TheKnife provides a RESTful API for interaction with the system. Documentation for the API endpoints can be found at:


* Swagger UI: http://localhost:8080/swagger-ui/index.html

## Technologies Used and Resources:
- RESTful API using Java with Spring Boot and PostgreSQL for data storage.
- JPA with Hibernate for object-relational mapping.
- Image storage.
- Proper usage of the HTTP protocol in REST standards.
- Utilization of Lombok to reduce boilerplate code.
- Detailed README for easy understanding and setup of the project.
- Model Relationships configured to represent object relationships.
- Dockerfile used for creating Docker images.
- Automatic API documentation with Swagger.
- Postman test collection to ensure API integrity and correctness.
- Docker Compose used for easy execution and management of Docker containers.
- Implementation of in-memory cache with Redis for performance optimization.
- Tests to ensure code quality.

## Credits:
This project was developed by Barbara Erler Rolim, Carlos Silva, Luis Matos, and Pedro Soares, for the Backend final project at MindSwap Mindera.
