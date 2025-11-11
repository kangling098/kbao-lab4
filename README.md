# Lab 9 - JSF Data Tables
kbao2 lab 9 README - JSF Data Tables with Bootstrap Template and Enhanced Error Handling

## 🎯 Lab 9 Overview

This project has been enhanced from Lab 8 to implement **JSF h:dataTable functionality** as required by Lab 9. The implementation adds comprehensive features including Bootstrap-based templates, data table components, error handling, and role-based content display:

- ✅ **Bootstrap-based Application Template** with responsive design
- ✅ **JSF h:dataTable Components** displaying entity information
- ✅ **Enhanced Error Handling** with custom error pages
- ✅ **JSF Controllers/Backing Beans** for data table functionality
- ✅ **DateTime Converters** for proper data formatting
- ✅ **Role-Based Content Display** showing relevant content to authenticated users
- ✅ **Navigation and Logout** through template links
- ✅ **Professional UI Design** with modern Bootstrap styling

- ✅ **JSF Backing Bean** with @Named and @RequestScoped annotations
- ✅ **JSF Form Page** with h:form, h:inputText, and h:commandButton
- ✅ **JSF Confirmation Page** displaying saved entity fields
- ✅ **Jakarta Bean Validation** integration with h:message tags
- ✅ **Command Button Action Methods** invoking EJB services
- ✅ **Complete Web Application** with WAR packaging

## 📋 Business Domain Description

I have chosen a **Library Management System** as my business domain for this semester project. This domain is particularly engaging because it involves real-world entities that most people can relate to - books, borrowers, librarians, and library operations. The library system provides a rich environment for learning JPA, EJB, and enterprise Java concepts while building something practical and useful.

A library management system handles the core operations of tracking books, managing checkouts, handling member registrations, and maintaining inventory. This domain offers excellent opportunities to explore various JPA relationships and EJB service layer patterns including stateless session beans, dependency injection, and transaction management.

**Lab 9 Enhancement**: The system now includes Bootstrap-based templates, h:dataTable components for displaying library data, enhanced error handling, and role-based content display, making it suitable for real-world library management scenarios with a modern UI and responsive design.

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

#### **LoginController - Security Authentication**
```java
@Named("loginController")
@RequestScoped
public class LoginController {
    @EJB
    private UserService userService;
    
    @Inject
    private SecurityContext securityContext;
    
    // Form fields for login
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(max = 50, message = "Password must not exceed 50 characters")
    private String password;
    
    // Action method for authentication
    public String login() {
        // Authenticates user using Jakarta EE Security
        // Returns navigation outcome based on role
    }
    
    // Logout functionality
    public String logout() {
        // Logs out user and invalidates session
        // Returns navigation to login page
    }
    
    // Utility methods
    public boolean isAuthenticated() {
        return securityContext.getCallerPrincipal() != null;
    }
    
    public boolean hasRole(String role) {
        return securityContext.isCallerInRole(role);
    }
}
```

#### **JSF Pages**
- **index.xhtml**: Enhanced main page with user authentication status and navigation
- **confirmation.xhtml**: Confirmation page displaying saved entity details
- **login.xhtml**: Professional login form with demo account information
- **error.xhtml**: Authentication error page with detailed feedback and navigation

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

## 🎨 Lab 9 Template and UI Architecture

### **Bootstrap-based JSF Template**
The project implements a comprehensive Bootstrap-based template with the following components:

#### **Template.xhtml - Application Template**
```xhtml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">

    <h:head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title><ui:insert name="title">Library Management System</ui:insert></title>
        
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" />
        
        <!-- Custom Styles -->
        <style>
            body {
                background-color: #f8f9fa;
            }
            .navbar-custom {
                background-color: #343a40;
            }
            .sidebar {
                min-height: calc(100vh - 56px);
            }
            .main-content {
                min-height: calc(100vh - 56px);
                background-color: #ffffff;
            }
            .footer {
                background-color: #343a40;
                color: white;
                padding: 20px 0;
            }
            .card-custom {
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                border: none;
            }
        </style>
    </h:head>

    <h:body>
        <!-- Navigation Bar -->
        <nav class="navbar navbar-expand-lg navbar-dark navbar-custom">
            <div class="container-fluid">
                <h:link value="Library Management System" styleClass="navbar-brand" outcome="/index" />
                
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" 
                        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <h:link styleClass="nav-link" outcome="/index" value="Home" />
                        </li>
                        
                        <ui:fragment rendered="#{loginController.authenticated}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    Books
                                </a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <h:link styleClass="dropdown-item" outcome="/index" value="View Books" />
                                    </li>
                                    <li>
                                        <h:link styleClass="dropdown-item" outcome="/index" value="Manage Books" />
                                    </li>
                                </ul>
                            </li>
                            
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    Loans
                                </a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <h:link styleClass="dropdown-item" outcome="/index" value="View Loans" />
                                    </li>
                                    <li>
                                        <h:link styleClass="dropdown-item" outcome="/index" value="Manage Loans" />
                                    </li>
                                </ul>
                            </li>
                        </ui:fragment>
                        
                        <ui:fragment rendered="#{loginController.hasRole('ADMIN') or loginController.hasRole('LIBRARIAN')}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    Admin
                                </a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <h:link styleClass="dropdown-item" outcome="/admin/dashboard" value="Dashboard" />
                                    </li>
                                    <li>
                                        <h:link styleClass="dropdown-item" outcome="/admin/users" value="Manage Users" />
                                    </li>
                                    <li>
                                        <h:link styleClass="dropdown-item" outcome="/admin/reports" value="Reports" />
                                    </li>
                                </ul>
                            </li>
                        </ui:fragment>
                    </ul>
                    
                    <ul class="navbar-nav">
                        <ui:fragment rendered="#{loginController.authenticated}">
                            <li class="nav-item">
                                <span class="navbar-text me-3">
                                    Welcome, <strong>#{loginController.currentUsername}</strong> 
                                    <span class="badge bg-secondary">#{loginController.hasRole('ADMIN') ? 'ADMIN' : 
                                        loginController.hasRole('LIBRARIAN') ? 'LIBRARIAN' : 'USER'}</span>
                                </span>
                            </li>
                            <li class="nav-item">
                                <h:form styleClass="d-inline">
                                    <h:commandLink styleClass="nav-link" action="#{loginController.logout}" value="Logout" />
                                </h:form>
                            </li>
                        </ui:fragment>
                        <ui:fragment rendered="#{!loginController.authenticated}">
                            <li class="nav-item">
                                <h:link styleClass="nav-link" outcome="/login" value="Login" />
                            </li>
                        </ui:fragment>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Main Content -->
        <main class="main-content">
            <div class="container-fluid py-4">
                <ui:insert name="content">
                    <!-- Default content area -->
                </ui:insert>
            </div>
        </main>

        <!-- Footer -->
        <footer class="footer mt-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <p>&copy; 2025 Library Management System. All rights reserved.</p>
                        <p>Contact: admin@library.edu</p>
                    </div>
                </div>
            </div>
        </footer>

        <!-- Bootstrap 5 JS Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </h:body>
</html>
```

### **JSF h:dataTable Implementation**
The project implements h:dataTable components to display library data with proper formatting and styling:

#### **User Dashboard - Available Books Table**
```xhtml
<!-- Books DataTable -->
<div class="row mt-4">
    <div class="col-12">
        <div class="card card-custom">
            <div class="card-header">
                <h3 class="mb-0">Available Books</h3>
            </div>
            <div class="card-body">
                <h:form id="booksForm">
                    <div class="table-responsive">
                        <h:dataTable id="booksTable" 
                                     value="#{bookService.findAvailableBooks()}" 
                                     var="book"
                                     styleClass="table table-striped table-hover"
                                     headerClass="thead-dark">
                            
                            <h:column>
                                <f:facet name="header">ID</f:facet>
                                #{book.id}
                            </h:column>
                            
                            <h:column>
                                <f:facet name="header">Title</f:facet>
                                #{book.title}
                            </h:column>
                            
                            <h:column>
                                <f:facet name="header">Author</f:facet>
                                #{book.author}
                            </h:column>
                            
                            <h:column>
                                <f:facet name="header">ISBN</f:facet>
                                #{book.isbn}
                            </h:column>
                            
                            <h:column>
                                <f:facet name="header">Publication Date</f:facet>
                                <f:convertDateTime pattern="MM/dd/yyyy" />
                                #{book.publicationDate}
                            </h:column>
                            
                            <h:column>
                                <f:facet name="header">Price</f:facet>
                                $#{book.price}
                            </h:column>
                            
                            <h:column>
                                <f:facet name="header">Status</f:facet>
                                <span class="badge #{book.isAvailable ? 'bg-success' : 'bg-warning'}">
                                    #{book.isAvailable ? 'Available' : 'Checked Out'}
                                </span>
                            </h:column>
                            
                            <f:facet name="footer">
                                Total Available Books: #{bookService.findAvailableBooks().size()}
                            </f:facet>
                        </h:dataTable>
                    </div>
                </h:form>
            </div>
        </div>
    </div>
</div>
```

#### **Role-Based Data Display**
The system implements role-based data display with different dashboards for different user roles:

- **User Dashboard**: Displays available books and user's active loans
- **Librarian Dashboard**: Displays available books and all active loans
- **Admin Dashboard**: Displays all books and all loans in the system

### **JSF Controllers and Backing Beans**
The project uses existing EJB services as backing beans for the data tables, with a new method added to support user-specific loan queries:

#### **Updated BookLoanService with User-Specific Queries**
```java
/**
 * Find active loans by borrower name.
 * @param borrowerName the borrower name
 * @return list of active loans for the borrower
 */
public List<BookLoan> findActiveLoansByUser(String borrowerName) {
    LOG.log(Level.INFO, "Finding active loans by borrower name: {0}", borrowerName);
    TypedQuery<BookLoan> query = em.createQuery(
        "SELECT bl FROM BookLoan bl WHERE bl.borrowerName = :borrowerName AND bl.returnDate IS NULL", BookLoan.class);
    query.setParameter("borrowerName", borrowerName);
    return query.getResultList();
}
```

### **DateTime Converters and Data Formatting**
The implementation uses f:convertDateTime for proper date formatting in data tables:

```xhtml
<h:column>
    <f:facet name="header">Publication Date</f:facet>
    <f:convertDateTime pattern="MM/dd/yyyy" />
    #{book.publicationDate}
</h:column>
```

### **Role-Based Content Display**
The system implements role-based content display using rendered attributes:

```xhtml
<!-- Admin-only menu items -->
<ui:fragment rendered="#{loginController.hasRole('ADMIN') or loginController.hasRole('LIBRARIAN')}">
    <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
            Admin
        </a>
    </li>
</ui:fragment>

<!-- User dashboard shows user-specific loans -->
<ui:fragment rendered="#{loginController.authenticated}">
    <div class="row mt-4">
        <div class="col-12">
            <h3>My Book Loans</h3>
            <h:dataTable value="#{bookLoanService.findActiveLoansByUser(loginController.currentUsername)}" var="loan">
                <!-- Loan data columns -->
            </h:dataTable>
        </div>
    </div>
</ui:fragment>
```

### **Enhanced Error Handling Configuration**
The project implements comprehensive error handling with custom error pages:

#### **web.xml Error Page Configuration**
```xml
<!-- Error Pages Configuration -->
<error-page>
    <error-code>403</error-code>
    <location>/error.xhtml</location>
</error-page>

<error-page>
    <error-code>404</error-code>
    <location>/error.xhtml</location>
</error-page>

<error-page>
    <error-code>500</error-code>
    <location>/error.xhtml</location>
</error-page>

<error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/error.xhtml</location>
</error-page>
```

#### **Error Page Implementation**
The error.xhtml page provides user-friendly error messages and navigation options with Bootstrap styling.

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
2. **Login**: Use demo accounts (admin/admin123, librarian/lib123, student/student123, etc.)
3. **Fill in book details**: Title, Author, ISBN, Publication Date, Page Count, Price
4. **Submit the form**: Click "Create Book" button
5. **View confirmation**: See the saved book details on confirmation page
6. **Create another book**: Click "Create Another Book" to return to form

### **Security Authentication Demonstration**
The security implementation provides comprehensive authentication and authorization:

1. **Access login page**: Navigate to `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/login.xhtml`
2. **Login with credentials**: Use demo accounts provided on the login page
3. **Role-based navigation**: System automatically redirects based on user role
4. **User status display**: Navigation bar shows authenticated user and role badges
5. **Logout functionality**: Secure logout with session invalidation

### **Demo Accounts**
- **Admin**: admin / admin123 (Full system access)
- **Librarian**: librarian / lib123 (Book management access)
- **User**: student / student123 (Borrowing privileges)
- **User**: teacher / teacher123 (Borrowing privileges)
- **User**: parent / parent123 (Borrowing privileges)

### **JSF Features Demonstrated**
- **Form Validation**: Real-time validation with h:message display
- **Command Button**: h:commandButton invokes backing bean action
- **Navigation**: Automatic navigation between pages
- **Data Display**: h:outputText displays saved entity fields
- **Styling**: Custom CSS for professional appearance
- **Security Integration**: User authentication status and role-based UI elements
- **Conditional Rendering**: h:panelGroup for role-specific content display
- **Authentication Flow**: Complete login/logout functionality with error handling

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
- ✅ **User ↔ Group** (ManyToMany): Users belong to multiple security groups
- ✅ **User → Borrower** (OneToOne): Security users linked to library members
- ✅ **Group → User** (ManyToMany): Groups contain multiple users

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

### **Security Annotations Used**
- **@CustomFormAuthenticationMechanismDefinition**: Configures custom form-based authentication
- **@DatabaseIdentityStoreDefinition**: Configures database-based identity store
- **@DeclareRoles**: Declares security roles for the application
- **@Startup**: Ensures security configuration initializes on application startup
- **@Singleton**: Creates a singleton security configuration bean

### **JSF Components Used**
- **h:form**: JSF form component with automatic CSRF protection
- **h:inputText**: Input field with validation integration
- **h:commandButton**: Action button invoking backing bean methods
- **h:message**: Validation error message display
- **h:outputText**: Data display with formatting support
- **f:validateRegex**: Regular expression validation
- **f:convertDateTime**: Date formatting and conversion

## ✅ Lab 9 Requirements Fulfillment

### **Core Lab 9 Requirements:**
- ✅ **Bootstrap Template**: Applied Bootstrap styling to JSF template for consistent application-wide look
- ✅ **h:dataTable Components**: Implemented data tables to display books, loans, and other library entities
- ✅ **Error Handling**: Added error page configuration in web.xml for 403, 404, 500 errors
- ✅ **JSF Controllers/Backing Beans**: Utilized existing EJB services as backing beans for data tables
- ✅ **DateTime Converters**: Used f:convertDateTime for proper date formatting in tables
- ✅ **Role-Based Content**: Implemented role-based content display using rendered attributes
- ✅ **Navigation Integration**: Added navigation and logout functionality through template
- ✅ **Professional UI**: Created professional-looking dashboards for different user roles

### **Additional Lab 9 Features:**
- ✅ **Responsive Design**: Bootstrap-based responsive layout that works on different screen sizes
- ✅ **User Dashboard**: Personalized dashboard showing relevant information based on user role
- ✅ **Admin Dashboard**: Comprehensive view of all system data with management capabilities  
- ✅ **Librarian Dashboard**: Focused view on library operations and active loans
- ✅ **Data Formatting**: Proper formatting of dates, prices, and status indicators
- ✅ **Role-Based Navigation**: Menu items that appear based on user role permissions
- ✅ **Template Consistency**: Consistent look and feel across all application pages
- ✅ **Error Page Styling**: Professional error page with Bootstrap styling and navigation options

## 🏆 Conclusion

Lab 9 successfully implements **JSF h:dataTable functionality** with Bootstrap templates and enhanced error handling that transform the library management system into a modern, professional web application. The implementation demonstrates:

- **Modern UI Design**: Bootstrap-based responsive templates with consistent styling
- **Data Visualization**: h:dataTable components for displaying library data in tabular format
- **Role-Based Dashboards**: Different dashboards for users, librarians, and administrators
- **Enhanced Error Handling**: Comprehensive error page configuration and user-friendly error messages
- **Professional Navigation**: Template-based navigation with role-based menu items
- **Date Formatting**: Proper date formatting using f:convertDateTime components
- **Status Indicators**: Visual indicators for book availability and loan status
- **Responsive Design**: Mobile-friendly layout that works on different screen sizes

The implementation provides a solid foundation for building enterprise web applications with modern UI design and effective data presentation.

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

### **Access JSF Application with DataTables**
- **Login Page**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/login.xhtml`
- **Main Application**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/index.xhtml`
- **User Dashboard**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/user/dashboard.xhtml`
- **Librarian Dashboard**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/librarian/dashboard.xhtml`
- **Admin Dashboard**: `http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/admin/dashboard.xhtml`
- **Authentication**: Use demo accounts provided on login page
- **Role-Based Access**: Different functionality based on user role
- **Logout**: Secure logout functionality with session management

## 📁 Project Structure

```
kbao2-lab9/
├── pom.xml                           # Maven configuration with JSF dependencies
├── README.md                         # This documentation
├── src/
│   ├── main/
│   │   ├── java/edu/iit/itmd4515/
│   │   │   ├── Main.java             # NetBeans execution main class
│   │   │   ├── config/               # Configuration classes
│   │   │   │   ├── DataSourceConfig.java
│   │   │   │   └── SecurityConfig.java # Jakarta EE security configuration
│   │   │   ├── controller/           # JSF backing beans
│   │   │   │   ├── BookController.java
│   │   │   │   └── LoginController.java # Security authentication controller
│   │   │   ├── domain/               # JPA entity classes
│   │   │   │   ├── Book.java
│   │   │   │   ├── BookLoan.java
│   │   │   │   ├── Borrower.java     # Enhanced with User relationship
│   │   │   │   ├── Librarian.java
│   │   │   │   ├── Publisher.java
│   │   │   │   ├── Library.java
│   │   │   │   ├── User.java         # Security user entity
│   │   │   │   └── Group.java        # Security group/role entity
│   │   │   └── service/              # EJB service layer
│   │   │       ├── AbstractService.java
│   │   │       ├── BookService.java
│   │   │       ├── BookLoanService.java # Updated with findActiveLoansByUser method
│   │   │       ├── BorrowerService.java
│   │   │       ├── LibrarianService.java
│   │   │       ├── LibraryService.java
│   │   │       ├── PublisherService.java
│   │   │       ├── DatabaseSeedService.java # Enhanced with security data
│   │   │       ├── UserService.java    # Security user management
│   │   │       └── GroupService.java   # Security group management
│   │   ├── resources/META-INF/
│   │   │   ├── persistence.xml       # JPA configuration
│   │   │   └── persistence-standalone.xml
│   │   └── webapp/                   # JSF web application
│   │       ├── WEB-INF/
│   │       │   ├── faces-config.xml  # JSF configuration
│   │       │   ├── web.xml           # Web application configuration with error handling
│   │       │   ├── payara-web.xml    # Payara security role mappings
│   │       │   └── template.xhtml    # Bootstrap-based JSF template
│   │       ├── admin/
│   │       │   └── dashboard.xhtml   # Admin dashboard with data tables
│   │       ├── librarian/
│   │       │   └── dashboard.xhtml   # Librarian dashboard with data tables
│   │       ├── user/
│   │       │   └── dashboard.xhtml   # User dashboard with data tables
│   │       ├── index.xhtml           # Main page with role-based redirection
│   │       ├── confirmation.xhtml    # JSF confirmation page
│   │       ├── login.xhtml           # Professional login page
│   │       └── error.xhtml           # Enhanced error page
│   └── test/
│       └── java/edu/iit/itmd4515/
│           ├── BookTest.java         # CRUD operation tests
│           ├── BookValidationTest.java # Bean validation tests
│           └── RelationshipTest.java # Entity relationship tests
```

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
- ✅ **User ↔ Group** (ManyToMany): Users belong to multiple security groups
- ✅ **User → Borrower** (OneToOne): Security users linked to library members
- ✅ **Group → User** (ManyToMany): Groups contain multiple users

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