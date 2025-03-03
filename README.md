# Complaint Management Service

A **RESTful service** for managing customer complaints, built with **Spring Boot** and containerized using **Docker**.

---

## Features

- **Create Complaints**: Users can submit complaints about products.
- **Edit Complaint Content**: Update the complaint description.
- **Retrieve Complaints**: Fetch complaints based on product and complainer.
- **Unique Complaints**: Each complaint is unique per product and complainer. If a duplicate is submitted, the complaint count is incremented instead.
- **Automatic Country Detection**: Determines the country of the user based on their IP address using an external API.
- **Optimistic Locking**: Prevents concurrent modification issues.
- **Fully Containerized**: Uses **Docker** and **Docker Compose** for easy deployment.

---

## Prerequisites

Before running the application, ensure you have:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17+](https://adoptium.net/)
- [Gradle](https://gradle.org/) (if building manually)

---

## How to Build and Run

### 1. Clone the repository
```bash
git clone https://github.com/draggy1/complaint.git
cd ccomplaint
```

### 2. Build the application

Before running the application, you need to build it using **Gradle**.

Run the following command:

```bash
./gradlew build
```

### 3. Start the application using Docker Compose

The application is containerized and can be easily started using **Docker Compose**.

Run the following command:

```bash
./gradlew dockerComposeUp
```

### 4. Access the application

Once the application is running, you can access it via:

- **Base URL**:  
- Visit the application in your browser or API client at:
```
http://localhost:8080
```

---

# API Documentation

## 1. Create a Complaint

Creates a new complaint. If a complaint for the same product and complainer already exists, the `complaintCount` is incremented instead of creating a duplicate.

### Request:
```http
POST /complaint
```

**Body:**
```
{
  "productId": 1,
  "complainerId": 1,
  "content": "The product stopped working after a week."
}
```

**Example:**
```bash
curl -X POST "http://localhost:8080/complaint" \
     -H "Content-Type: application/json" \
     -d '{
           "productId": 1,
           "complainerId": 2,
           "content": "The product stopped working after a week."
         }'
 ```

**Success Response (201 Created):**

```json
{
  "id": 4,
  "product": {
    "id": 1,
    "name": "Laptop Pro X"
  },
  "complainer": {
    "id": 2,
    "name": "Alice",
    "surname": "Johnson"
  },
  "content": "The product stopped working after a week.",
  "createdAt": "2025-03-03T06:22:54Z",
  "complaintCountry": "Poland",
  "complaintCount": 1
}
```

**Response (Error):**
If the product is not found:
```json
{
  "status": "404",
  "message": "Product not found"
}
```
or if the complainer i not found

```json
{
  "status": "404",
  "message": "Complainer not found"
}
```


---

## 2. Retrieve All Complaints
Fetches all stored complaints.

**Request:**
```http
GET /complaint/all
```

**Example:**
```bash
curl -X GET "http://localhost:8080/complaint/all"
```

**Success Response (200 OK):**

```json
[
  {
    "id": 1,
    "product": {
      "id": 1,
      "name": "Laptop Pro X"
    },
    "complainer": {
      "id": 1,
      "name": "John",
      "surname": "Smith"
    },
    "content": "The laptop overheats after 30 minutes of usage.",
    "createdAt": "2023-03-15T10:15:30Z",
    "complaintCountry": "US",
    "complaintCount": 1
  },
  {
    "id": 2,
    "product": {
      "id": 2,
      "name": "Smartphone Ultra Y"
    },
    "complainer": {
      "id": 2,
      "name": "Alice",
      "surname": "Johnson"
    },
    "content": "The smartphone battery drains too fast.",
    "createdAt": "2023-03-16T11:20:00Z",
    "complaintCountry": "UK",
    "complaintCount": 2
  },
  {
    "id": 3,
    "product": {
      "id": 3,
      "name": "Wireless Headphones Z"
    },
    "complainer": {
      "id": 3,
      "name": "Michael",
      "surname": "Brown"
    },
    "content": "The wireless headphones disconnect randomly.",
    "createdAt": "2023-03-17T12:30:15Z",
    "complaintCountry": "Canada",
    "complaintCount": 1
  },
  {
    "id": 4,
    "product": {
      "id": 1,
      "name": "Laptop Pro X"
    },
    "complainer": {
      "id": 2,
      "name": "Alice",
      "surname": "Johnson"
    },
    "content": "The product stopped working after a week.",
    "createdAt": "2025-03-03T06:26:21Z",
    "complaintCountry": "Poland",
    "complaintCount": 1
  }
]
```

**Response (Error):**
If the product is not found:
```json
{
  "status": "404",
  "message": "Product not found"
}
```

## 3. Retrieve a Complaint by ID

Fetches a specific complaint using its unique identifier.

### Request:
```http
GET /complaint/{id}
```

**PathVariable:**
- `{id}`: : The unique identifier of the complaint. Example: `1`.


**Example:**
```bash
curl -X GET "http://localhost:8080/complaint/1"
 ```

**Success Response (200 OK):**

```json
{
  "id": 1,
  "product": {
    "id": 1,
    "name": "Laptop Pro X"
  },
  "complainer": {
    "id": 1,
    "name": "John",
    "surname": "Smith"
  },
  "content": "The laptop overheats after 30 minutes of usage.",
  "createdAt": "2023-03-15T10:15:30Z",
  "complaintCountry": "US",
  "complaintCount": 1
}
```

**Response (Error):**
If the product is not found:
```json
{
  "status": "404",
  "message": "Complaint not found"
}
```

---

## Architectural Decision Records (ADR)

All important architectural and design decisions related to this project are documented as Architectural Decision Records (ADR).

The details regarding the design of the Product API can be found in the following ADR:
- [001: Complaint API Design](docks/adr/001-initial-decisions.md)

You can navigate to the file for detailed explanations of the decisions made during the development of this project.

---

## How to Stop the Application

To stop and remove the containers, run:
```bash
./gradlew dockerComposeDown
```

---

# Future Considerations and Potential Improvements

As the **Complaint Management Service** evolves, there are several areas that require further refinement and improvement. Some of these were identified during development but were not addressed due to time constraints. Below are key aspects to consider in the near future:

## 1. Standardizing Library Versioning in `build.gradle`
- Currently, library versions in `build.gradle` are defined inconsistently. Some are hardcoded, while others are managed via a `dependencyManagement` block or external properties.
- **Best Practice Considerations:**
    - Define all dependency versions in a central location (e.g., `gradle.properties` or a `versions` block).
    - Use **Spring Dependency Management Plugin** for better version control.
    - Consider adopting **Version Catalogs (Toml-based approach)** introduced in Gradle for cleaner version management.
- **Next Steps:** Research best practices and refactor dependency management in `build.gradle` for better maintainability.

## 2. Refactoring `GlobalExceptionHandler` for Maintainability
- The `GlobalExceptionHandler` class has grown significantly, making it harder to maintain.
- **Refactoring Considerations:**
    - Introduce a **modular approach**, grouping exception handlers into **separate classes** based on error categories (e.g., `DatabaseExceptionHandler`, `ValidationExceptionHandler`).
    - Utilize **inheritance or a base exception handler** to reduce redundancy.
    - Consider a **custom annotation-based approach** to improve flexibility.
- **Next Steps:** Analyze the current structure and implement a cleaner, more maintainable exception-handling mechanism.

---

## Conclusion
By addressing these improvements, the project will benefit from **better maintainability, readability, and scalability**. These refinements will enhance the **developer experience** and ensure long-term **code health**.

---

## License

This project is licensed under the MIT License.

---

## Authors

- [Kamil Dragan](https://github.com/draggy1) - Initial work