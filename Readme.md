# Lab 10 - REST API Implementation
kbao2 lab 10 README - REST Web Services Implementation

## 🎯 Lab 10 Overview

This project has been enhanced from Lab 7 to implement **REST (Representational State Transfer) API functionality** as required by Lab 10. The implementation provides comprehensive RESTful web services for the library management system:

- ✅ **Complete REST API** with /api base path for all endpoints
- ✅ **Full CRUD Operations** for all 7 entities (Book, User, Borrower, BookLoan, Librarian, Library, Publisher)
- ✅ **Security Integration** with role-based access control (ADMIN, LIBRARIAN, USER)
- ✅ **Advanced Query Endpoints** for sophisticated data retrieval
- ✅ **Proper Error Handling** with appropriate HTTP status codes
- ✅ **Comprehensive Logging** for monitoring and debugging

## 📋 Business Domain Description

I have chosen a **Library Management System** as my business domain for this semester project. This domain is particularly engaging because it involves real-world entities that most people can relate to - books, borrowers, librarians, and library operations. The library system provides a rich environment for learning JPA, EJB, and enterprise Java concepts while building something practical and useful.

A library management system handles the core operations of tracking books, managing checkouts, handling member registrations, and maintaining inventory. This domain offers excellent opportunities to explore various JPA relationships and EJB service layer patterns including stateless session beans, dependency injection, and transaction management. The REST API implementation allows for integration with various client applications including web frontends, mobile apps, and other services.

## 🏗️ Lab 10 REST API Architecture

### **REST Web Services Design**
The project now implements a complete REST API with the following components:

#### **RestApplication - REST Configuration**
```java
@ApplicationPath("/api")
public class RestApplication extends Application {
    // All REST endpoints are accessible under the /api path
}
```

#### **REST Resource Classes**
- **BookResource**: Complete book management with search capabilities
- **UserResource**: User account management with role-based access
- **BorrowerResource**: Borrower registration and management
- **BookLoanResource**: Loan processing and return functionality
- **LibrarianResource**: Staff management and assignment
- **LibraryResource**: Branch management and operations
- **PublisherResource**: Publishing information management

### **REST API Configuration**
```xml
<!-- JAX-RS API dependency -->
<dependency>
    <groupId>jakarta.ws.rs</groupId>
    <artifactId>jakarta.ws.rs-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>
```

## ✅ Lab 7 JSF Implementation (Previous Work)

### **1. BookController - JSF Backing Bean**
```java
@Named("bookController")
@RequestScoped
public class BookController {
    @EJB
    private BookService bookService;
    
    // Form fields with Jakarta Bean Validation
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    // Action method for form submission
    public String createBook() {
        // Creates book using EJB service
        // Returns navigation outcome
    }
}
```

### **2. BookService - Book Management**
```java
@Stateless
public class BookService extends AbstractService<Book> {
    public List<Book> findByTitle(String title) { /* ... */ }
    public List<Book> findByAuthor(String author) { /* ... */ }
    public Book findByIsbn(String isbn) { /* ... */ }
    public List<Book> findAvailableBooks() { /* ... */ }
    public List<Book> findByPublisher(Long publisherId) { /* ... */ }
}
```

### **3. BorrowerService - Member Management**
```java
@Stateless
public class BorrowerService extends AbstractService<Borrower> {
    public List<Borrower> findByLastName(String lastName) { /* ... */ }
    public Borrower findByEmail(String email) { /* ... */ }
    public List<Borrower> findActiveBorrowers() { /* ... */ }
    public List<Borrower> findBorrowersWithOverdueBooks() { /* ... */ }
    public long countActiveLoans(Long borrowerId) { /* ... */ }
}
```

### **4. LibrarianService - Staff Management**
```java
@Stateless
public class LibrarianService extends AbstractService<Librarian> {
    public List<Librarian> findByLastName(String lastName) { /* ... */ }
    public Librarian findByEmployeeId(String employeeId) { /* ... */ }
    public List<Librarian> findActiveLibrarians() { /* ... */ }
    public List<Librarian> findByLibrary(Long libraryId) { /* ... */ }
    public long countProcessedLoans(Long librarianId) { /* ... */ }
}
```

### **5. LibraryService - Branch Management**
```java
@Stateless
public class LibraryService extends AbstractService<Library> {
    public List<Library> findByName(String name) { /* ... */ }
    public List<Library> findByCity(String city) { /* ... */ }
    public List<Library> findByState(String state) { /* ... */ }
    public long countActiveLoans(Long libraryId) { /* ... */ }
    public long countTotalLoans(Long libraryId) { /* ... */ }
}
```

### **6. BookLoanService - Transaction Management**
```java
@Stateless
public class BookLoanService extends AbstractService<BookLoan> {
    public List<BookLoan> findActiveLoans() { /* ... */ }
    public List<BookLoan> findOverdueLoans() { /* ... */ }
    public List<BookLoan> findByBorrower(Long borrowerId) { /* ... */ }
    public List<BookLoan> findByBook(Long bookId) { /* ... */ }
    public List<BookLoan> findByLibrary(Long libraryId) { /* ... */ }
    @Override
    public BookLoan update(BookLoan loan) { /* Process book returns */ }
}
```

### **7. PublisherService - Publishing Management**
```java
@Stateless
public class PublisherService extends AbstractService<Publisher> {
    public List<Publisher> findByName(String name) { /* ... */ }
    public List<Publisher> findByCity(String city) { /* ... */ }
    public List<Publisher> findByCountry(String country) { /* ... */ }
}
```

## 🔑 Lab 10 REST API Security Implementation

### **Role-Based Access Control**
The REST API implements security with three distinct roles:

- **ADMIN**: Full access to all operations including user management
- **LIBRARIAN**: Access to book, borrower, and loan management operations
- **USER**: Limited access for self-service operations

#### **Security Annotations**
```java
@RolesAllowed("ADMIN")
public class UserResource { ... }

@RolesAllowed({"ADMIN", "LIBRARIAN"})
public Response createBook(Book book) { ... }

@PermitAll
public Response getBookById(@PathParam("id") Long id) { ... }
```

#### **Security Configuration**
```java
@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "java:app/jdbc/itmd4515DS",
    callerQuery = "SELECT password FROM users WHERE username = ? AND is_active = true",
    groupsQuery = "SELECT g.group_name FROM user_groups_table g JOIN user_groups ug ON g.id = ug.group_id JOIN users u ON u.id = ug.user_id WHERE u.username = ?",
    priority = 10
)
@DeclareRoles({"ADMIN", "LIBRARIAN", "USER"})
```

## 🚀 Lab 10 REST API Endpoints

### **REST API Structure**
All endpoints are accessible under the `/api` base path:

#### **Books Management** (`/api/books`)
- `GET /api/books` - Get all books (public access)
- `GET /api/books/{id}` - Get specific book (public access)
- `POST /api/books` - Create new book (ADMIN/LIBRARIAN)
- `PUT /api/books/{id}` - Update book (ADMIN/LIBRARIAN)
- `DELETE /api/books/{id}` - Delete book (ADMIN/LIBRARIAN)
- `GET /api/books/search/title/{title}` - Search by title (public access)
- `GET /api/books/search/author/{author}` - Search by author (public access)
- `GET /api/books/available` - Get available books (public access)

#### **Users Management** (`/api/users`)
- `GET /api/users` - Get all users (ADMIN)
- `GET /api/users/{id}` - Get specific user (ADMIN)
- `GET /api/users/username/{username}` - Get user by username (ADMIN)
- `POST /api/users` - Create new user (ADMIN)
- `PUT /api/users/{id}` - Update user (ADMIN)
- `DELETE /api/users/{id}` - Delete user (ADMIN)

#### **Borrowers Management** (`/api/borrowers`)
- `GET /api/borrowers` - Get all borrowers (ADMIN/LIBRARIAN)
- `GET /api/borrowers/{id}` - Get specific borrower (ADMIN/LIBRARIAN)
- `POST /api/borrowers` - Create new borrower (ADMIN/LIBRARIAN)
- `PUT /api/borrowers/{id}` - Update borrower (ADMIN/LIBRARIAN)
- `DELETE /api/borrowers/{id}` - Delete borrower (ADMIN/LIBRARIAN)
- `GET /api/borrowers/search/lastName/{lastName}` - Search by last name (ADMIN/LIBRARIAN)
- `GET /api/borrowers/search/email/{email}` - Search by email (ADMIN/LIBRARIAN)
- `GET /api/borrowers/active` - Get active borrowers (ADMIN/LIBRARIAN)
- `GET /api/borrowers/overdue` - Get borrowers with overdue books (ADMIN/LIBRARIAN)

#### **Book Loans Management** (`/api/loans`)
- `GET /api/loans` - Get all loans (ADMIN/LIBRARIAN)
- `GET /api/loans/{id}` - Get specific loan (ADMIN/LIBRARIAN)
- `POST /api/loans` - Create new loan (ADMIN/LIBRARIAN)
- `PUT /api/loans/{id}` - Update loan (ADMIN/LIBRARIAN)
- `PUT /api/loans/{id}/return` - Return book (ADMIN/LIBRARIAN)
- `DELETE /api/loans/{id}` - Delete loan (ADMIN/LIBRARIAN)
- `GET /api/loans/active` - Get active loans (ADMIN/LIBRARIAN)
- `GET /api/loans/overdue` - Get overdue loans (ADMIN/LIBRARIAN)
- `GET /api/loans/borrower/{borrowerId}` - Get loans by borrower (ADMIN/LIBRARIAN)
- `GET /api/loans/book/{bookId}` - Get loans by book (ADMIN/LIBRARIAN)
- `GET /api/loans/library/{libraryId}` - Get loans by library (ADMIN/LIBRARIAN)

#### **Librarians Management** (`/api/librarians`)
- `GET /api/librarians` - Get all librarians (ADMIN)
- `GET /api/librarians/{id}` - Get specific librarian (ADMIN/LIBRARIAN)
- `POST /api/librarians` - Create new librarian (ADMIN)
- `PUT /api/librarians/{id}` - Update librarian (ADMIN)
- `DELETE /api/librarians/{id}` - Delete librarian (ADMIN)
- `GET /api/librarians/search/lastName/{lastName}` - Search by last name (ADMIN/LIBRARIAN)
- `GET /api/librarians/search/employeeId/{employeeId}` - Search by employee ID (ADMIN/LIBRARIAN)
- `GET /api/librarians/active` - Get active librarians (ADMIN/LIBRARIAN)
- `GET /api/librarians/library/{libraryId}` - Get librarians by library (ADMIN/LIBRARIAN)

#### **Libraries Management** (`/api/libraries`)
- `GET /api/libraries` - Get all libraries (ADMIN)
- `GET /api/libraries/{id}` - Get specific library (ADMIN/LIBRARIAN)
- `POST /api/libraries` - Create new library (ADMIN)
- `PUT /api/libraries/{id}` - Update library (ADMIN)
- `DELETE /api/libraries/{id}` - Delete library (ADMIN)
- `GET /api/libraries/search/name/{name}` - Search by name (ADMIN/LIBRARIAN)
- `GET /api/libraries/search/city/{city}` - Search by city (ADMIN/LIBRARIAN)
- `GET /api/libraries/search/state/{state}` - Search by state (ADMIN/LIBRARIAN)

#### **Publishers Management** (`/api/publishers`)
- `GET /api/publishers` - Get all publishers (public access)
- `GET /api/publishers/{id}` - Get specific publisher (public access)
- `POST /api/publishers` - Create new publisher (ADMIN/LIBRARIAN)
- `PUT /api/publishers/{id}` - Update publisher (ADMIN/LIBRARIAN)
- `DELETE /api/publishers/{id}` - Delete publisher (ADMIN/LIBRARIAN)
- `GET /api/publishers/search/name/{name}` - Search by name (public access)
- `GET /api/publishers/search/city/{city}` - Search by city (public access)
- `GET /api/publishers/search/country/{country}` - Search by country (public access)

### **Sample API Request**
```
=== REST API REQUEST ===
POST /api/books
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Java RESTful Web Services",
  "author": "John Smith",
  "isbn": "9781234567890",
  "publicationDate": "2024-01-15",
  "pageCount": 450,
  "price": 49.99,
  "isAvailable": true
}

=== REST API RESPONSE ===
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "title": "Java RESTful Web Services",
  "author": "John Smith",
  "isbn": "9781234567890",
  "publicationDate": "2024-01-15",
  "pageCount": 450,
  "price": 49.99,
  "isAvailable": true,
  "dueDate": null,
  "publisher": null,
  "bookLoans": []
}
```

### **Error Handling**
- **404 Not Found**: Resource does not exist
- **403 Forbidden**: Insufficient permissions
- **500 Internal Server Error**: Server-side error with detailed message
- **Proper Error Messages**: Descriptive error responses for debugging

## 🧪 Test Results

### **EJB Service Layer Test Coverage**
```
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
```

### **Service Operations Tested:**
- ✅ **CRUD Operations**: All services implement create, read, update, delete
- ✅ **Custom Queries**: Title search, author search, availability filtering
- ✅ **Business Logic**: Overdue detection, loan counting, membership validation
- ✅ **Transaction Management**: Automatic transaction handling with @Transactional
- ✅ **Dependency Injection**: @PersistenceContext EntityManager injection
- ✅ **Database Seeding**: @Startup @Singleton automatic data population

### **Entity Relationship Validation**
- ✅ **Publisher → Book** (OneToMany): Publishers track their published books
- ✅ **Book → BookLoan** (OneToMany): Books maintain loan history
- ✅ **Borrower → BookLoan** (OneToMany): Borrowers have complete loan records
- ✅ **Library → BookLoan** (OneToMany): Libraries track all transactions
- ✅ **Librarian → BookLoan** (OneToMany): Staff process loan transactions

## 📊 Business Logic Implementation

### **Overdue Detection**
```java
public boolean isOverdue() {
    return returnDate == null && LocalDate.now().isAfter(dueDate);
}
```

### **Availability Checking**
```java
public boolean isAvailableForLoan() {
    return isAvailable && dueDate == null;
}
```

### **Loan Processing**
```java
public void loanBook(LocalDate dueDate) {
    if (!isAvailable) {
        throw new IllegalStateException("Book is not available for loan");
    }
    this.isAvailable = false;
    this.dueDate = dueDate;
}
```

### **Return Processing**
```java
public void returnBook() {
    this.isAvailable = true;
    this.dueDate = null;
}
```

## 🔧 Technical Configuration

### **REST API Configuration**
```xml
<!-- JAX-RS API dependency -->
<dependency>
    <groupId>jakarta.ws.rs</groupId>
    <artifactId>jakarta.ws.rs-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>
```

### **Maven Dependencies for REST**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <version>3.4.0</version>
    <configuration>
        <warSourceDirectory>src/main/webapp</warSourceDirectory>
        <failOnMissingWebXml>false</failOnMissingWebXml>
    </configuration>
</plugin>
```

### **REST Annotations Used**
- **@ApplicationPath**: Defines the base URL for all REST endpoints
- **@Path**: Defines resource paths for each endpoint
- **@GET/@POST/@PUT/@DELETE**: HTTP method annotations
- **@Produces/@Consumes**: Defines content types (JSON)
- **@RolesAllowed/@PermitAll**: Security role annotations

## ✅ Lab 10 Requirements Fulfillment

### **Core Requirements:**
- ✅ **Complete REST API**: All entities have full CRUD operations exposed via REST
- ✅ **Resource Classes**: One resource class per entity (7 total)
- ✅ **JAX-RS Implementation**: Using Jakarta EE JAX-RS API
- ✅ **Security Integration**: Role-based access control properly implemented
- ✅ **HTTP Methods**: Proper use of GET, POST, PUT, DELETE for CRUD operations
- ✅ **Proper Status Codes**: Correct HTTP status codes for all responses
- ✅ **Error Handling**: Comprehensive error handling with meaningful messages

### **Advanced Features:**
- ✅ **Query Endpoints**: Advanced search functionality for each entity
- ✅ **Security Roles**: Implementation of ADMIN, LIBRARIAN, USER roles
- ✅ **REST Best Practices**: Following REST architectural principles
- ✅ **API Documentation**: Comprehensive endpoint documentation
- ✅ **Logging**: Proper logging for monitoring and debugging
- ✅ **Input Validation**: Integration with existing validation constraints

## 🏆 Conclusion

Lab 10 successfully implements a comprehensive **REST API** that extends the existing JSF web application with modern web services. The implementation demonstrates:

- **REST Architecture**: Proper implementation of RESTful principles with clear resource identification
- **Security Integration**: Jakarta EE security framework integrated with REST endpoints
- **Scalability**: API design allows for integration with multiple client types
- **Error Handling**: Robust error handling with appropriate HTTP status codes
- **Enterprise Integration**: Seamless integration with existing EJB service layer
- **Performance**: Efficient queries leveraging existing EJB service layer

The REST API provides a solid foundation for building modern client applications, including web frontends, mobile apps, and third-party integrations, while maintaining the existing JSF functionality for traditional web access.

## 🛠️ Build and Run Instructions

### **Prerequisites**
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Application Server (GlassFish, WildFly, etc.)

### **Database Setup**
```sql
CREATE DATABASE itmd4515;
CREATE USER 'itmd4515'@'localhost' IDENTIFIED BY 'itmd4515';
GRANT ALL PRIVILEGES ON itmd4515.* TO 'itmd4515'@'localhost';
FLUSH PRIVILEGES;
```

### **Build WAR File**
```bash
mvn clean compile war:war -Dmaven.test.skip=true
```

### **Deploy Application**
1. **Copy WAR file**: `target/itmd4515-fp-1.0-SNAPSHOT.war`
2. **Deploy to server**: Use application server admin console or deployment tools
3. **Start server**: Ensure MySQL is running and application server is started

### **Access REST API**
- **Base URL**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/api`
- **Books Endpoint**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/api/books`
- **Users Endpoint**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/api/users`
- **Borrowers Endpoint**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/api/borrowers`

### **Access JSF Application**
- **Form Page**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/`
- **Create Book**: Fill form and submit
- **Confirmation**: View saved book details
- **Create Another**: Return to form page

## 📁 Project Structure

```
kbao2-lab10/
├── pom.xml                           # Maven configuration with REST dependencies
├── README.md                         # This documentation
├── src/
│   ├── main/
│   │   ├── java/edu/iit/itmd4515/
│   │   │   ├── Main.java             # NetBeans execution main class
│   │   │   ├── config/               # Configuration classes
│   │   │   ├── controller/           # JSF backing beans
│   │   │   │   └── BookController.java
│   │   │   ├── domain/               # JPA entity classes
│   │   │   │   ├── Book.java
│   │   │   │   ├── Borrower.java
│   │   │   │   ├── Librarian.java
│   │   │   │   ├── Publisher.java
│   │   │   │   ├── Library.java
│   │   │   │   ├── BookLoan.java
│   │   │   │   ├── User.java
│   │   │   │   └── Group.java
│   │   │   ├── rest/                 # REST resource classes
│   │   │   │   ├── RestApplication.java
│   │   │   │   ├── BookResource.java
│   │   │   │   ├── UserResource.java
│   │   │   │   ├── BorrowerResource.java
│   │   │   │   ├── BookLoanResource.java
│   │   │   │   ├── LibrarianResource.java
│   │   │   │   ├── LibraryResource.java
│   │   │   │   └── PublisherResource.java
│   │   │   └── service/              # EJB service layer
│   │   │       ├── AbstractService.java
│   │   │       ├── BookService.java
│   │   │       ├── BorrowerService.java
│   │   │       ├── LibrarianService.java
│   │   │       ├── LibraryService.java
│   │   │       ├── BookLoanService.java
│   │   │       ├── PublisherService.java
│   │   │       ├── UserService.java
│   │   │       ├── GroupService.java
│   │   │       └── DatabaseSeedService.java
│   │   ├── resources/META-INF/
│   │   │   ├── persistence.xml       # JPA configuration
│   │   │   └── persistence-standalone.xml
│   │   └── webapp/                   # JSF web application
│   │       ├── WEB-INF/
│   │       │   ├── faces-config.xml  # JSF configuration
│   │       │   └── web.xml           # Web application configuration
│   │       ├── index.xhtml           # JSF form page
│   │       └── confirmation.xhtml    # JSF confirmation page
│   └── test/
│       └── java/edu/iit/itmd4515/
│           ├── BookTest.java         # CRUD operation tests
│           ├── BookValidationTest.java # Bean validation tests
│           └── RelationshipTest.java # Entity relationship tests
```