# Architectural Decision Record (ADR) for Complaint Management Service

This document outlines the key architectural decisions made during the development of the Complaint Management Service.

---

## Decision 1: Use SERIAL PRIMARY KEY for Complaint IDs

### Context
Each complaint record requires a unique identifier in the database. Although using UUIDs is a common practice for distributed systems, we needed an ID that is easily human-readable for debugging and administration purposes.

### Decision
We opted to use a **SERIAL PRIMARY KEY** for complaint IDs instead of UUID.

### Rationale
- **Human Readability:** Serial numbers (e.g., 1, 2, 3) are easier to read and manage compared to UUIDs.
- **Simplicity:** Using a SERIAL column simplifies the database schema and reduces configuration complexity.
- **Performance:** SERIAL keys (auto-incrementing integers) typically have a slight performance benefit over UUIDs in certain scenarios.

### Consequences
- The generated IDs are unique only within this database instance, not globally unique.
- Future integrations with distributed systems might require additional considerations for global uniqueness.

---

## Decision 2: Hardcoding Database Credentials for Development Ease

### Context
Environment variables are generally recommended for storing sensitive configuration like database credentials. However, during rapid development and testing, configuring environment variables can add extra overhead.

### Decision
For the purpose of easing project setup and rapid prototyping, we decided to **hardcode the database username and password** in the configuration rather than externalizing them as environment variables.

### Rationale
- **Ease of Setup:** Developers can quickly start the project without needing to set up external environment variables.
- **Rapid Prototyping:** Simplifies the initial configuration and reduces setup complexity in development environments.

### Consequences
- This approach is **not secure for production** environments.
- In a production setting, credentials should be externalized using environment variables or a secrets management system.
- A future refactor is required to externalize sensitive configuration when moving to production.

---

## Decision 3: Model Product and Complainer as Separate Entities

### Context
A complaint is associated with both a product and a complainer. We considered two approaches:
- Storing product and complainer as simple IDs within the Complaint entity.
- Creating separate entities (`Product` and `Complainer`) and linking them via relationships.

### Decision
We decided to create two additional entities—**Product** and **Complainer**—and establish relationships from Complaint to these entities.

### Rationale
- **Expressive Domain Model:** The domain model becomes clearer by explicitly representing products and complainers.
- **Extensibility:** Additional attributes and relationships for products and complainers can be easily added later without modifying the Complaint entity.
- **Scalability:** Although this increases complexity, it lays a solid foundation for future expansion and evolution of the system.

### Consequences
- Increased complexity in the domain model, requiring more joins in queries.
- Better long-term maintainability and scalability, as changes to product or complainer details are isolated to their respective entities.

---

## Decision 4: Use MapStruct for Entity-to-DTO Mapping

### Context
Mapping between domain entities and Data Transfer Objects (DTOs) is a common task in RESTful applications. Writing boilerplate mapping code can be error-prone and time-consuming.

### Decision
We chose to use **MapStruct** to automatically generate the mapping code between entities and DTOs.

### Rationale
- **One-to-One Mapping:** Our entities closely mirror our DTOs, making MapStruct an ideal choice.
- **Reduced Boilerplate:** MapStruct eliminates the need for manually writing mapping code.
- **Compile-Time Safety:** The library validates mappings during compilation, reducing runtime errors.
- **Maintenance:** Changes in the domain model automatically reflect in generated mappers when recompiled.

### Consequences
- Adds a dependency to the project.
- Improves code clarity and maintainability by reducing manual mapping effort.

---

*These ADRs capture our key architectural decisions and serve as a reference for future improvements and maintenance of the Complaint Management Service.*