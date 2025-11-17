# Lab 7 - JSF Implementation
kbao2 lab 7 README - JSF Web Application Implementation

## 🎯 Lab 7 Overview

This project has been enhanced from Lab 6 to implement **JSF (JavaServer Faces)** functionality as required by Lab 7. The implementation replaces previous Servlet/JSP work with modern JSF components:

- ✅ **JSF Backing Bean** with @Named and @RequestScoped annotations
- ✅ **JSF Form Page** with h:form, h:inputText, and h:commandButton
- ✅ **JSF Confirmation Page** displaying saved entity fields
- ✅ **Jakarta Bean Validation** integration with h:message tags
- ✅ **Command Button Action Methods** invoking EJB services
- ✅ **Complete Web Application** with WAR packaging

## 📋 Business Domain Description

I have chosen a **Library Management System** as my business domain for this semester project. This domain is particularly engaging because it involves real-world entities that most people can relate to - books, borrowers, librarians, and library operations. The library system provides a rich environment for learning JPA, EJB, and enterprise Java concepts while building something practical and useful.

A library management system handles the core operations of tracking books, managing checkouts, handling member registrations, and maintaining inventory. This domain offers excellent opportunities to explore various JPA relationships and EJB service layer patterns including stateless session beans, dependency injection, and transaction management.

## 🏗️ Lab 7 JSF Architecture

### **JSF Web Application Design**
The project now implements a complete JSF web application with the following components:

#### **BookController - JSF Backing Bean**
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

#### **JSF Pages**
- **index.xhtml**: Form page with h:form, h:inputText, h:commandButton, h:message
- **confirmation.xhtml**: Confirmation page displaying saved entity details

### **JSF Configuration**
```xml
<!-- faces-config.xml -->
<faces-config version="4.0">
    <application>
        <resource-bundle>
            <base-name>messages</base-name>
            <var>msg</var>
        </resource-bundle>
    </application>
</faces-config>

<!-- web.xml -->
<servlet>
    <servlet-name>Faces Servlet</servlet-name>
    <servlet-class>jakarta.faces.webapp.FacesServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>Faces Servlet</servlet-name>
    <url-pattern>*.xhtml</url-pattern>
</servlet-mapping>
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

## 🚀 Lab 7 JSF Execution

### **JSF Form Demonstration**
The JSF implementation provides a complete web interface for book creation:

1. **Access the form**: Navigate to `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/`
2. **Fill in book details**: Title, Author, ISBN, Publication Date, Page Count, Price
3. **Submit the form**: Click "Create Book" button
4. **View confirmation**: See the saved book details on confirmation page
5. **Create another book**: Click "Create Another Book" to return to form

### **JSF Features Demonstrated**
- **Form Validation**: Real-time validation with h:message display
- **Command Button**: h:commandButton invokes backing bean action
- **Navigation**: Automatic navigation between pages
- **Data Display**: h:outputText displays saved entity fields
- **Styling**: Custom CSS for professional appearance

### **Sample Form Submission**
```
=== JSF FORM SUBMISSION ===
✓ Book Created Successfully!
Book ID: 1
Title: Java Programming Fundamentals
Author: Dr. Alice Smith
ISBN: 9781234567890
Publication Date: January 15, 2024
Page Count: 450
Price: $59.99
Availability: Available
```

### **How to Deploy and Run**
1. **Build the WAR file**: `mvn clean compile war:war -Dmaven.test.skip=true`
2. **Deploy to application server**: GlassFish, WildFly, etc.
3. **Access the application**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/`
4. **Test the JSF functionality**: Create books through the web interface

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

### **JSF Configuration**
```xml
<!-- faces-config.xml -->
<faces-config version="4.0">
    <application>
        <resource-bundle>
            <base-name>messages</base-name>
            <var>msg</var>
        </resource-bundle>
    </application>
</faces-config>

<!-- web.xml -->
<servlet>
    <servlet-name>Faces Servlet</servlet-name>
    <servlet-class>jakarta.faces.webapp.FacesServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>Faces Servlet</servlet-name>
    <url-pattern>*.xhtml</url-pattern>
</servlet-mapping>
```

### **Maven Dependencies for JSF**
```xml
<!-- WAR Plugin for JSF deployment -->
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

### **JSF Annotations Used**
- **@Named**: Marks backing bean for JSF EL expression access
- **@RequestScoped**: Defines bean lifecycle as request scope
- **@EJB**: Enables EJB dependency injection in backing bean
- **@PostConstruct**: Initializes backing bean after construction

### **JSF Components Used**
- **h:form**: JSF form component with automatic CSRF protection
- **h:inputText**: Input field with validation integration
- **h:commandButton**: Action button invoking backing bean methods
- **h:message**: Validation error message display
- **h:outputText**: Data display with formatting support
- **f:validateRegex**: Regular expression validation
- **f:convertDateTime**: Date formatting and conversion

## ✅ Lab 7 Requirements Fulfillment

### **Core Requirements:**
- ✅ **Replace Servlet/JSP functionality**: All previous servlet/JSP code removed
- ✅ **JSF Backing Bean**: BookController with @Named and @RequestScoped annotations
- ✅ **JSF Form Page**: index.xhtml with h:form, h:inputText, h:commandButton
- ✅ **JSF Confirmation Page**: confirmation.xhtml displaying saved entity fields
- ✅ **Command Button Action**: h:commandButton invokes createBook() action method
- ✅ **h:message Tags**: Validation error display with Jakarta Bean Validation
- ✅ **EJB Integration**: Backing bean uses EJB services for persistence
- ✅ **Entity Creation**: Complete book creation through JSF interface
- ✅ **Form Validation**: Client and server-side validation with proper feedback

### **Additional JSF Features:**
- ✅ **Navigation Management**: Proper JSF navigation with faces-redirect
- ✅ **Styling**: Professional CSS styling for modern appearance
- ✅ **Responsive Design**: Clean, user-friendly interface
- ✅ **Validation Integration**: Jakarta Bean Validation with JSF messages
- ✅ **Data Conversion**: Proper date and number formatting
- ✅ **WAR Packaging**: Complete web application packaging

## 🏆 Conclusion

Lab 7 successfully implements a comprehensive **JSF Web Application** that replaces previous Servlet/JSP functionality with modern JavaServer Faces technology. The implementation demonstrates:

- **Component-Based Web Development**: Proper use of JSF UI components and backing beans
- **MVC Architecture**: Clear separation between view (XHTML), controller (backing bean), and model (EJB services)
- **Form Validation**: Integration of Jakarta Bean Validation with JSF error messaging
- **Navigation Management**: Proper JSF navigation patterns with faces-redirect
- **Professional UI**: Clean, styled web interface with responsive design
- **Enterprise Integration**: Seamless integration with existing EJB service layer

The JSF implementation provides a solid foundation for building modern enterprise web applications with proper validation, error handling, and user experience.

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

### **Access JSF Application**
- **Form Page**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/`
- **Create Book**: Fill form and submit
- **Confirmation**: View saved book details
- **Create Another**: Return to form page

## 📁 Project Structure

```
kbao2-lab7/
├── pom.xml                           # Maven configuration with JSF dependencies
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