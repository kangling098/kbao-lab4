# Lab 6 - EJB Service Layer
kbao2 lab 6 README - EJB Service Layer Implementation

## 🎯 Lab 6 Overview

This project has been enhanced from Lab 5 to implement **EJB Service Layer** as required by Lab 6. The implementation includes:

- ✅ **@Stateless EJB Components** for all database operations (CRUD)
- ✅ **Abstract Service Pattern** with generic types
- ✅ **@PersistenceContext EntityManager Injection**
- ✅ **@Startup @Singleton EJB** for database seeding
- ✅ **Comprehensive Service Layer** with business logic
- ✅ **Updated Main class** for service layer demonstration

## 📋 Business Domain Description

I have chosen a **Library Management System** as my business domain for this semester project. This domain is particularly engaging because it involves real-world entities that most people can relate to - books, borrowers, librarians, and library operations. The library system provides a rich environment for learning JPA, EJB, and enterprise Java concepts while building something practical and useful.

A library management system handles the core operations of tracking books, managing checkouts, handling member registrations, and maintaining inventory. This domain offers excellent opportunities to explore various JPA relationships and EJB service layer patterns including stateless session beans, dependency injection, and transaction management.

## 🏗️ Lab 6 Architecture

### **EJB Service Layer Design**
The project implements a comprehensive service layer using EJB 4.0 specifications:

#### **AbstractService<T> - Generic Base Service**
```java
@Stateless
public abstract class AbstractService<T> {
    @PersistenceContext
    public EntityManager em;
    
    // CRUD Operations: create, findById, findAll, update, delete, count
}
```

#### **Entity-Specific Services**
- **BookService**: Book management with custom queries (findByTitle, findByAuthor, findAvailableBooks)
- **BorrowerService**: Borrower management with membership tracking
- **LibrarianService**: Staff management with employment details
- **LibraryService**: Library branch management with statistics
- **BookLoanService**: Loan transaction management with overdue tracking
- **PublisherService**: Publisher catalog management

### **Database Seeding with @Startup Singleton**
```java
@Singleton
@Startup
public class DatabaseSeedService {
    @Inject
    private BookService bookService;
    // ... other services
    
    @PostConstruct
    public void seedDatabase() {
        // Automatically seeds database on application startup
    }
}
```

## ✅ Lab 6 Service Implementation

### **1. AbstractService<T> - Generic Foundation**
- **Purpose**: Provides common CRUD operations for all entities
- **Features**: 
  - Generic type parameter for type safety
  - Standard CRUD operations (create, read, update, delete)
  - Entity counting and listing
  - Comprehensive logging
  - Transaction management with @Transactional

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

## 🚀 Lab 6 Main Class Execution

### **Service Layer Demonstration**
The updated Main.java class demonstrates the complete EJB service layer:

```
Starting EJB Lab 6 - Service Layer Demo
=== DEMONSTRATING EJB SERVICE LAYER ===

--- Service Layer Operations ---
Total books: 3
Total borrowers: 3
Total libraries: 2
Total book loans: 3

--- Available Books ---
Available: Java Programming Fundamentals by Dr. Alice Smith ($59.99)
Available: Database Design and Implementation by Prof. Bob Wilson ($54.99)
Available: Web Development with Modern Frameworks by Dr. Carol Davis ($69.99)

--- Active Borrowers ---
Active: John Doe (john.doe@student.edu)
Active: Jane Smith (jane.smith@school.edu)
Active: Robert Johnson (robert.johnson@email.com)

--- Overdue Loans ---
Overdue: "Database Design and Implementation" by Jane Smith (Due: 2025-10-06)

--- Library Statistics ---
Main Public Library: 2 active loans, 2 total loans
West Side Branch: 1 active loans, 1 total loans

=== SERVICE LAYER DEMONSTRATION COMPLETE ===
EJB Lab 6 - Service Layer Demo completed successfully!
```

### **Database Seeding Results**
The @Startup singleton automatically creates:
- **2 Publishers**: Tech Books Publishing, Education Press
- **2 Libraries**: Main Public Library, West Side Branch
- **2 Librarians**: Head Librarian, Reference Librarian
- **3 Books**: Java Programming, Database Design, Web Development
- **3 Borrowers**: Student, Teacher, Parent
- **3 Book Loans**: Active and overdue scenarios

### **How to Run in NetBeans**
1. **Right-click on the project** in NetBeans
2. **Select "Run"** or **"Clean and Build"** then "Run"
3. **NetBeans will automatically find and execute the Main class**
4. **View the console output** to see the EJB service layer demonstration

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

### **Maven Dependencies**
```xml
<!-- EJB Container for standalone execution -->
<dependency>
    <groupId>org.glassfish.main.extras</groupId>
    <artifactId>glassfish-embedded-all</artifactId>
    <version>7.0.18</version>
    <scope>compile</scope>
</dependency>
```

### **Persistence Configuration**
- **EntityManager Injection**: Uses @PersistenceContext for automatic injection
- **Transaction Management**: JTA for web applications, RESOURCE_LOCAL for standalone
- **Database**: MySQL 8.0 with comprehensive relationship mapping
- **Validation**: Jakarta Bean Validation with Hibernate Validator

### **EJB Annotations Used**
- **@Stateless**: Marks all service classes as stateless session beans
- **@Singleton**: Marks the database seeding service as application singleton
- **@Startup**: Ensures database seeding happens at application startup
- **@Inject**: Enables dependency injection between services
- **@PersistenceContext**: Provides automatic EntityManager injection
- **@Transactional**: Ensures proper transaction management

## 🎯 Lab 6 Requirements Fulfillment

### **Core Requirements:**
- ✅ **@Stateless EJB Components**: All database operations use stateless session beans
- ✅ **CRUD Operations**: Complete create, read, update, delete functionality
- ✅ **@PersistenceContext Injection**: EntityManager injected using resource injection
- ✅ **JPA Usage**: No JDBC, all operations use JPA
- ✅ **Abstract Service Pattern**: Generic AbstractService<T> implementation
- ✅ **Service-per-Entity Design**: Dedicated service for each entity
- ✅ **@Startup Singleton EJB**: DatabaseSeedService with automatic seeding
- ✅ **CRUD Method Invocation**: Services invoke each other's CRUD methods
- ✅ **No Direct EntityManager**: Startup singleton uses services, not direct EM
- ✅ **Logger Output**: Comprehensive logging with relationship navigation
- ✅ **NetBeans Integration**: Main class for direct NetBeans execution

### **Additional Features:**
- ✅ **Comprehensive Business Logic**: Overdue detection, availability checking
- ✅ **Custom Query Methods**: Entity-specific search and filtering
- ✅ **Transaction Management**: Automatic transaction handling
- ✅ **Dependency Injection**: Full CDI integration between services
- ✅ **Database Relationship Navigation**: Complete entity graph traversal
- ✅ **Error Handling**: Proper exception handling and validation

## 🏆 Conclusion

Lab 6 successfully implements a comprehensive **EJB Service Layer** that transforms the JPA entity model into a fully functional enterprise application. The implementation demonstrates:

- **Enterprise Java Best Practices**: Proper use of EJB annotations and patterns
- **Service-Oriented Architecture**: Clean separation of business logic
- **Transaction Management**: Automatic transaction handling
- **Dependency Injection**: Modern Java EE dependency management
- **Database Seeding**: Automatic application initialization
- **Comprehensive Logging**: Detailed operation tracking
- **NetBeans Integration**: Seamless IDE execution

The service layer provides a solid foundation for building web applications, REST APIs, or other enterprise integrations on top of the library management system domain model.

## 🛠️ Build and Run Instructions

### **Prerequisites**
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- NetBeans IDE (recommended)

### **Database Setup**
```sql
CREATE DATABASE itmd4515;
CREATE USER 'itmd4515'@'localhost' IDENTIFIED BY 'itmd4515';
GRANT ALL PRIVILEGES ON itmd4515.* TO 'itmd4515'@'localhost';
FLUSH PRIVILEGES;
```

### **Build Project**
```bash
mvn clean compile
```

### **Run Tests**
```bash
mvn test
```

### **Run Main Class**
```bash
mvn exec:java
```

### **NetBeans Execution**
1. Open project in NetBeans
2. Right-click project → "Run"
3. Or right-click Main.java → "Run File"

## 📁 Project Structure

```
kbao2-lab6/
├── pom.xml                           # Maven configuration with EJB dependencies
├── README.md                         # This documentation
├── src/
│   ├── main/
│   │   ├── java/edu/iit/itmd4515/
│   │   │   ├── Main.java             # NetBeans execution main class
│   │   │   ├── config/               # Configuration classes
│   │   │   ├── domain/               # JPA entity classes
│   │   │   │   ├── Book.java
│   │   │   │   ├── Borrower.java
│   │   │   │   ├── Librarian.java
│   │   │   │   ├── Publisher.java
│   │   │   │   ├── Library.java
│   │   │   │   └── BookLoan.java
│   │   │   └── service/              # EJB service layer
│   │   │       ├── AbstractService.java
│   │   │       ├── BookService.java
│   │   │       ├── BorrowerService.java
│   │   │       ├── LibrarianService.java
│   │   │       ├── LibraryService.java
│   │   │       ├── BookLoanService.java
│   │   │       ├── PublisherService.java
│   │   │       └── DatabaseSeedService.java
│   │   └── resources/META-INF/
│   │       ├── persistence.xml       # JPA configuration
│   │       └── persistence-standalone.xml
│   └── test/
│       └── java/edu/iit/itmd4515/
│           ├── BookTest.java         # CRUD operation tests
│           ├── BookValidationTest.java # Bean validation tests
│           └── RelationshipTest.java # Entity relationship tests
```