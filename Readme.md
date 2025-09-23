# Lab 5 - JPA Relationships
kbao2 lab 5 README - JPA Relationships Implementation

## 🎯 Lab 5 Overview

This project has been enhanced from Lab 4 to implement **JPA Relationships** as required by Lab 5. The implementation includes:

- ✅ **DataSourceDefinition** annotation for JNDI data source configuration
- ✅ **Dual Persistence Units**: JTA for web apps, RESOURCE_LOCAL for testing
- ✅ **5 Total Entities** (exceeds minimum requirement of 4)
- ✅ **6 JPA Relationships** including bidirectional relationships
- ✅ **Comprehensive relationship helper methods**
- ✅ **Main class for NetBeans execution**

## 📋 Business Domain Description

I have chosen a **Library Management System** as my business domain for this semester project. This domain is particularly engaging because it involves real-world entities that most people can relate to - books, authors, publishers, and library operations. The library system provides a rich environment for learning JPA and ORM concepts while building something practical and useful.

A library management system handles the core operations of tracking books, managing checkouts, handling member registrations, and maintaining inventory. This domain offers excellent opportunities to explore various JPA relationships including one-to-many (library has many books), many-to-many (books can have multiple authors, members can borrow multiple books), and complex temporal relationships (due dates, borrowing history).

## Business Domain Description

I have chosen a **Library Management System** as my business domain for this semester project. This domain is particularly engaging because it involves real-world entities that most people can relate to - books, authors, publishers, and library operations. The library system provides a rich environment for learning JPA and ORM concepts while building something practical and useful.

A library management system handles the core operations of tracking books, managing checkouts, handling member registrations, and maintaining inventory. This domain offers excellent opportunities to explore various JPA relationships including one-to-many (library has many books), many-to-many (books can have multiple authors, members can borrow multiple books), and complex temporal relationships (due dates, borrowing history).

## Future Entity Design

While Lab 4 focuses on the Book entity, the complete library system would include several interconnected entities:

### Core Entities:
1. **Member** - Library members who can borrow books
   - Personal information (name, email, phone, address)
   - Membership status and dates
   - Relationship with BorrowingRecord

2. **Author** - Book authors
   - Biographical information
   - Many-to-many relationship with Book

3. **Publisher** - Publishing companies
   - Company details
   - One-to-many relationship with Book

4. **BorrowingRecord** - Tracks book loans
   - Links Member and Book
   - Checkout date, due date, return date
   - Fine calculations

5. **LibraryBranch** - Physical library locations
   - Address and contact information
   - One-to-many relationship with Book copies

6. **BookCopy** - Individual physical copies of books
   - Links to Book entity
   - Copy-specific information (condition, acquisition date)
   - Status (available, damaged, lost)

### Entity Relationships:
- **Book** ↔ **Author** (Many-to-Many): Books can have multiple authors, authors can write multiple books
- **Book** → **Publisher** (Many-to-One): Each book has one publisher, publishers publish many books
- **Member** ↔ **Book** (Many-to-Many through BorrowingRecord): Members can borrow multiple books, books can be borrowed by multiple members over time
- **LibraryBranch** ↔ **BookCopy** (One-to-Many): Each branch has multiple book copies

This design allows for complex queries such as finding all books by a specific author, tracking member borrowing history, identifying overdue books, and managing inventory across multiple library branches.

## ✅ Lab 5 Entity Implementation

**Lab 5 has successfully implemented the complete entity system!** Here are the 5 entities created:

### 1. **Book** (Enhanced from Lab 4)
- **Enhanced with relationships**: ManyToOne with Publisher, ManyToMany with Author, OneToMany with BookLoan
- **Fields**: title, author, isbn, publicationDate, pageCount, price, isAvailable, dueDate
- **Relationships**: Publisher (ManyToOne), Authors (ManyToMany), BookLoans (OneToMany)

### 2. **Author** (New)
- **Purpose**: Represents book authors with biographical information
- **Fields**: firstName, lastName, birthDate, email, biography, nationality
- **Relationships**: Books (ManyToMany)

### 3. **Publisher** (New)
- **Purpose**: Represents publishing companies
- **Fields**: name, address, city, country, phoneNumber, email, foundedDate, description, active
- **Relationships**: Books (OneToMany)

### 4. **Library** (New)
- **Purpose**: Represents physical library branches
- **Fields**: name, address, city, state, zipCode, phoneNumber, email, openingTime, closingTime, capacity, active
- **Relationships**: BookLoans (OneToMany)

### 5. **BookLoan** (New)
- **Purpose**: Tracks book borrowing transactions
- **Fields**: loanDate, dueDate, returnDate, borrowerName, borrowerEmail, borrowerPhone, fineAmount, notes
- **Relationships**: Book (ManyToOne), Library (ManyToOne)

## 🔗 JPA Relationships Implemented

### **Unidirectional Relationships:**
1. **Book → Publisher** (ManyToOne): Each book has one publisher

### **Bidirectional Relationships:**
1. **Book ↔ Author** (ManyToMany): Books can have multiple authors, authors can write multiple books
2. **Publisher ↔ Book** (OneToMany): Publishers publish many books, each book belongs to one publisher
3. **Library ↔ BookLoan** (OneToMany): Libraries have many loans, each loan belongs to one library
4. **Book ↔ BookLoan** (OneToMany): Books can have multiple loans, each loan is for one book

## 🛠️ Technical Implementation

### **DataSource Configuration**
```java
@DataSourceDefinition(
    name = "java:app/jdbc/itmd4515DS",
    className = "com.mysql.cj.jdbc.MysqlDataSource",
    portNumber = 3306,
    serverName = "localhost",
    databaseName = "itmd4515",
    user = "itmd4515",
    password = "itmd4515",
    properties = {
        "zeroDateTimeBehavior=CONVERT_TO_NULL",
        "serverTimezone=America/Chicago",
        "useSSL=false"
    }
)
```

### **Dual Persistence Units**
- **itmd4515PU**: JTA persistence unit for web applications using JNDI data source
- **itmd4515StandalonePU**: RESOURCE_LOCAL persistence unit for standalone execution

### **Relationship Helper Methods**
All bidirectional relationships include proper helper methods to maintain synchronization:
- `Book.addAuthor()` / `Book.removeAuthor()`
- `Publisher.addBook()` / `Publisher.removeBook()`
- `Library.addBookLoan()` / `Library.removeBookLoan()`
- `Book.addBookLoan()` / `Book.removeBookLoan()`

## Test Results

### Test Environment Setup
- **Persistence Unit**: itmd4515testPU
- **Database**: MySQL itmd4515 (version 8.0.39)
- **JPA Provider**: EclipseLink 4.0.2
- **Test Framework**: JUnit 5
- **Java Version**: 17
- **Connection String**: jdbc:mysql://localhost:3306/itmd4515?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

### CRUD Operations Test Results

```
[INFO] Running edu.iit.itmd4515.BookTest
[INFO] Creating EntityManagerFactory...
[INFO] Setting up test environment...
[INFO] Testing Create Book operation...
[INFO] Created book with ID: 7
[INFO] Tearing down test environment...
[INFO] Setting up test environment...
[INFO] Testing Read Book operation...
[INFO] Found book: Book{id=6, title='Design Patterns', author='Gang of Four', isbn='9780201633610', publisher='Addison-Wesley', publicationDate=1994-10-31, pageCount=395, price=54.99, isAvailable=true, dueDate=null}
[INFO] Tearing down test environment...
[INFO] Setting up test environment...
[INFO] Testing Update Book operation...
[INFO] Updated book: Book{id=5, title='Refactoring', author='Martin Fowler', isbn='9780201485677', publisher='Addison-Wesley', publicationDate=1999-07-08, pageCount=464, price=49.99, isAvailable=false, dueDate=2025-09-30}
[INFO] Tearing down test environment...
[INFO] Setting up test environment...
[INFO] Testing Delete Book operation...
[INFO] Deleted book with ID: 8
[INFO] Tearing down test environment...
[INFO] Setting up test environment...
[INFO] Testing Find All Books operation...
[INFO] Found 3 books in total
[INFO] Book: Book{id=1, title='Book 1', author='Author 1', isbn='1111111111', publisher='Publisher 1', publicationDate=null, pageCount=null, price=null, isAvailable=true, dueDate=null}
[INFO] Book: Book{id=2, title='Book 3', author='Author 3', isbn='3333333333', publisher='Publisher 3', publicationDate=null, pageCount=null, price=null, isAvailable=true, dueDate=null}
[INFO] Book: Book{id=3, title='Book 2', author='Author 2', isbn='2222222222', publisher='Publisher 2', publicationDate=null, pageCount=null, price=null, isAvailable=true, dueDate=null}
[INFO] Tearing down test environment...
[INFO] Setting up test environment...
[INFO] Testing Find Book by ISBN operation...
[INFO] Found book by ISBN: Book{id=4, title='Test Book', author='Test Author', isbn='1234567890', publisher='Test Publisher', publicationDate=null, pageCount=null, price=null, isAvailable=true, dueDate=null}
[INFO] Tearing down test environment...
[INFO] Closing EntityManagerFactory...
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.743 s
```

### Validation Test Results

```
[INFO] Running edu.iit.itmd4515.BookValidationTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 s
```

### Overall Test Summary

```
[INFO] Results:
[INFO] 
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.312 s
[INFO] Finished at: 2025-09-16T15:36:49+08:00
[INFO] ------------------------------------------------------------------------
```

## 🚀 Lab 5 Main Class Execution

### **NetBeans Execution**
The project now includes a **Main.java** class that demonstrates all JPA relationships:

```
Starting JPA Lab 5 - Relationships Demo
=== DEMONSTRATION RESULTS ===

--- Book: JPA Relationships Guide ---
ISBN: 9781234567890
Author: John Doe
Publisher: Demo Publisher
Authors count: 2
  - John Doe
  - Jane Smith
Book loans count: 1

--- Publisher: Demo Publisher ---
Books published: 1
  - JPA Relationships Guide

--- Library: Demo Library ---
Location: Chicago, IL
Book loans: 1
  - JPA Relationships Guide by Student Name (Due: 2025-10-07)

=== RELATIONSHIPS DEMONSTRATION COMPLETE ===
JPA Lab 5 - Relationships Demo completed successfully!
```

### **How to Run in NetBeans**
1. **Right-click on the project** in NetBeans
2. **Select "Run"** or **"Clean and Build"** then "Run"
3. **NetBeans will automatically find and execute the Main class**
4. **View the console output** to see the relationships demonstration

### **Relationship Test Results**
```
[INFO] Running edu.iit.itmd4515.RelationshipTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.725 s
```

**Test Coverage:**
- ✅ Unidirectional ManyToOne (Book → Publisher)
- ✅ Bidirectional ManyToMany (Book ↔ Author)
- ✅ Bidirectional OneToMany (Publisher ↔ Book)
- ✅ Bidirectional OneToMany (Library ↔ BookLoan)

### Test Analysis

**Create Test (em.persist)**:
- Successfully creates a new Book entity
- Validates that the generated ID is not null after persistence
- Confirms the book can be retrieved after creation

**Read Test (em.find)**:
- Demonstrates successful retrieval of a book by its primary key
- Verifies all field values match the original data
- Shows proper clearing of persistence context

**Update Test (entity mutators)**:
- Updates book price, availability status, and due date
- Demonstrates transaction management for updates
- Verifies changes persist to the database

**Delete Test (em.remove)**:
- Successfully removes a book from the database
- Confirms the book cannot be found after deletion
- Demonstrates proper transaction handling for deletes

**Named Query Tests**:
- `findAll`: Retrieves all books in the database
- `findByIsbn`: Locates a specific book by its ISBN
- Both queries execute successfully and return expected results

### Entity Validation

The Book entity includes comprehensive validation constraints:
- **@NotBlank**: Ensures required fields are not empty
- **@Size**: Limits string lengths to appropriate database column sizes
- **@Pattern**: Validates ISBN format (10-13 digits)
- **@PastOrPresent**: Ensures publication dates are realistic
- **@Min/@Max**: Provides reasonable bounds for numeric values
- **@DecimalMin/@Digits**: Validates price format and range
- **@Future**: Ensures due dates are in the future

All tests pass successfully, demonstrating that the JPA configuration, entity mapping, and CRUD operations are working correctly with the MySQL database.