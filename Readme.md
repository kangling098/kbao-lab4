# Lab 4 - ORM and JPA

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