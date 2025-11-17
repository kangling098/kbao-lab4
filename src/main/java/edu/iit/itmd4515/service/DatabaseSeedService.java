package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.*;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Startup Singleton EJB to seed the database with sample data.
 * This class demonstrates the use of EJB services and populates
 * the database with initial test data for the library management system.
 */
@Singleton
@Startup
public class DatabaseSeedService {
    
    private static final Logger LOG = Logger.getLogger(DatabaseSeedService.class.getName());
    
    @Inject
    public BookService bookService;
    
    @Inject
    public BorrowerService borrowerService;
    
    @Inject
    public LibrarianService librarianService;
    
    @Inject
    public LibraryService libraryService;
    
    @Inject
    public BookLoanService bookLoanService;
    
    @Inject
    public PublisherService publisherService;
    
<<<<<<< HEAD
    @Inject
    public UserService userService;
    
    @Inject
    public GroupService groupService;
    
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
    @PostConstruct
    public void seedDatabase() {
        LOG.info("Starting database seeding...");
        
        try {
            // Clear existing data
            clearExistingData();
            
            // Create sample data
            createSampleData();
            
            // Display all relationships
            displayAllRelationships();
            
            LOG.info("Database seeding completed successfully!");
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error during database seeding", e);
        }
    }
    
    private void clearExistingData() {
        LOG.info("Clearing existing data...");
        
        // Delete in reverse order to respect foreign key constraints
<<<<<<< HEAD
        // Clear security data first (many-to-many relationships)
        List<User> users = userService.findAll();
        for (User user : users) {
            user.setGroups(null);
            userService.update(user);
        }
        
        List<Group> groups = groupService.findAll();
        for (Group group : groups) {
            group.setUsers(null);
            groupService.update(group);
        }
        
        // Clear business data
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
        List<BookLoan> loans = bookLoanService.findAll();
        for (BookLoan loan : loans) {
            bookLoanService.delete(loan);
        }
        
        List<Book> books = bookService.findAll();
        for (Book book : books) {
            bookService.delete(book);
        }
        
        List<Borrower> borrowers = borrowerService.findAll();
        for (Borrower borrower : borrowers) {
            borrowerService.delete(borrower);
        }
        
        List<Librarian> librarians = librarianService.findAll();
        for (Librarian librarian : librarians) {
            librarianService.delete(librarian);
        }
        
        List<Library> libraries = libraryService.findAll();
        for (Library library : libraries) {
            libraryService.delete(library);
        }
        
        List<Publisher> publishers = publisherService.findAll();
        for (Publisher publisher : publishers) {
            publisherService.delete(publisher);
        }
        
<<<<<<< HEAD
        // Clear security entities
        for (User user : users) {
            userService.delete(user);
        }
        
        for (Group group : groups) {
            groupService.delete(group);
        }
        
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
        LOG.info("Existing data cleared");
    }
    
    private void createSampleData() {
        LOG.info("Creating sample data...");
        
<<<<<<< HEAD
        // Create Security Groups
        groupService.createDefaultGroups();
        
        LOG.info("Created default security groups successfully");
        
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
        // Create Publishers
        Publisher techPublisher = new Publisher("Tech Books Publishing", "123 Tech Street", "San Francisco", "USA");
        techPublisher.setEmail("info@techbooks.com");
        publisherService.create(techPublisher);
        
        Publisher eduPublisher = new Publisher("Education Press", "456 Education Ave", "Boston", "USA");
        eduPublisher.setEmail("contact@educationpress.com");
        publisherService.create(eduPublisher);
        
        // Create Libraries
        Library mainLibrary = new Library("Main Public Library", "100 Library Way", "Chicago", "IL", "60601",
                                         LocalTime.of(9, 0), LocalTime.of(21, 0), 500);
        mainLibrary.setEmail("main@chicagolibrary.org");
        mainLibrary.setPhoneNumber("(312) 555-0100");
        libraryService.create(mainLibrary);
        
        Library branchLibrary = new Library("West Side Branch", "200 West Street", "Chicago", "IL", "60602",
                                           LocalTime.of(10, 0), LocalTime.of(18, 0), 200);
        branchLibrary.setEmail("westside@chicagolibrary.org");
        branchLibrary.setPhoneNumber("(312) 555-0200");
        libraryService.create(branchLibrary);
        
        // Create Librarians
        Librarian headLibrarian = new Librarian("Sarah", "Johnson", "HEAD001", "Head Librarian", LocalDate.now().minusYears(5));
<<<<<<< HEAD
        headLibrarian.setEmail("sarah.johnson@chicagolibrary.org");
        headLibrarian.setPhoneNumber("(312) 555-0199");
        headLibrarian.setLibrary(mainLibrary);
        headLibrarian.setEmployed(true);
        headLibrarian.setSalary(65000.0);
        headLibrarian.setDepartment("Administration");
        librarianService.create(headLibrarian);
        
        Librarian branchLibrarian = new Librarian("Michael", "Chen", "LIB002", "Reference Librarian", LocalDate.now().minusYears(2));
        branchLibrarian.setEmail("michael.chen@chicagolibrary.org");
        branchLibrarian.setPhoneNumber("(312) 555-0299");
        branchLibrarian.setLibrary(branchLibrary);
        branchLibrarian.setEmployed(true);
        branchLibrarian.setSalary(48000.0);
        branchLibrarian.setDepartment("Reference");
=======
        headLibrarian.setLibrary(mainLibrary);
        headLibrarian.setEmployed(true);
        headLibrarian.setSalary(65000.0);
        librarianService.create(headLibrarian);
        
        Librarian branchLibrarian = new Librarian("Michael", "Chen", "LIB002", "Reference Librarian", LocalDate.now().minusYears(2));
        branchLibrarian.setLibrary(branchLibrary);
        branchLibrarian.setEmployed(true);
        branchLibrarian.setSalary(48000.0);
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
        librarianService.create(branchLibrarian);
        
        // Create Books
        Book javaBook = new Book("Java Programming Fundamentals", "Dr. Alice Smith", "978-0-123456-78-9");
        javaBook.setPublicationDate(LocalDate.of(2023, 6, 15));
        javaBook.setPageCount(450);
        javaBook.setPrice(59.99);
        javaBook.setIsAvailable(true);
        javaBook.setPublisher(techPublisher);
        bookService.create(javaBook);
        
        Book databaseBook = new Book("Database Design and Implementation", "Prof. Bob Wilson", "978-0-987654-32-1");
        databaseBook.setPublicationDate(LocalDate.of(2023, 3, 20));
        databaseBook.setPageCount(380);
        databaseBook.setPrice(54.99);
        databaseBook.setIsAvailable(true);
        databaseBook.setPublisher(techPublisher);
        bookService.create(databaseBook);
        
        Book webBook = new Book("Web Development with Modern Frameworks", "Dr. Carol Davis", "978-0-567890-12-3");
        webBook.setPublicationDate(LocalDate.of(2023, 9, 10));
        webBook.setPageCount(520);
        webBook.setPrice(69.99);
        webBook.setIsAvailable(true);
        webBook.setPublisher(eduPublisher);
        bookService.create(webBook);
        
        // Create Borrowers
        Borrower studentBorrower = new Borrower("John", "Doe", "john.doe@student.edu", "(555) 123-4567");
        studentBorrower.setBirthDate(LocalDate.of(2000, 5, 15));
        studentBorrower.setAddress("123 Student Lane");
        studentBorrower.setCity("Chicago");
        studentBorrower.setState("IL");
        studentBorrower.setZipCode("60603");
        studentBorrower.setMembershipActive(true);
        borrowerService.create(studentBorrower);
        
        Borrower teacherBorrower = new Borrower("Jane", "Smith", "jane.smith@school.edu", "(555) 987-6543");
        teacherBorrower.setBirthDate(LocalDate.of(1985, 8, 22));
        teacherBorrower.setAddress("456 Teacher Avenue");
        teacherBorrower.setCity("Chicago");
        teacherBorrower.setState("IL");
        teacherBorrower.setZipCode("60604");
        teacherBorrower.setMembershipActive(true);
        borrowerService.create(teacherBorrower);
        
        Borrower parentBorrower = new Borrower("Robert", "Johnson", "robert.johnson@email.com", "(555) 456-7890");
        parentBorrower.setBirthDate(LocalDate.of(1978, 12, 3));
        parentBorrower.setAddress("789 Parent Street");
        parentBorrower.setCity("Chicago");
        parentBorrower.setState("IL");
        parentBorrower.setZipCode("60605");
        parentBorrower.setMembershipActive(true);
        borrowerService.create(parentBorrower);
        
<<<<<<< HEAD
        // Create Security Users and Groups
        createSecurityData(headLibrarian, branchLibrarian, studentBorrower, teacherBorrower, parentBorrower);
        
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
        // Create Book Loans
        BookLoan loan1 = new BookLoan(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), studentBorrower);
        loan1.setBook(javaBook);
        loan1.setLibrary(mainLibrary);
        loan1.setProcessedBy(headLibrarian);
        bookLoanService.create(loan1);
        
        BookLoan loan2 = new BookLoan(LocalDate.now().minusDays(14), LocalDate.now(), teacherBorrower);
        loan2.setBook(databaseBook);
        loan2.setLibrary(branchLibrary);
        loan2.setProcessedBy(branchLibrarian);
        bookLoanService.create(loan2);
        
        BookLoan loan3 = new BookLoan(LocalDate.now().minusDays(3), LocalDate.now().plusDays(11), parentBorrower);
        loan3.setBook(webBook);
        loan3.setLibrary(mainLibrary);
        loan3.setProcessedBy(headLibrarian);
        bookLoanService.create(loan3);
        
        LOG.info("Sample data created successfully");
    }
    
    private void displayAllRelationships() {
        LOG.info("=== DEMONSTRATING ALL RELATIONSHIPS ===");
        
        // Display Publishers and their Books
        LOG.info("\n--- PUBLISHERS AND THEIR BOOKS ---");
        List<Publisher> publishers = publisherService.findAll();
        for (Publisher publisher : publishers) {
            LOG.log(Level.INFO, "Publisher: {0} ({1}, {2})", 
                   new Object[]{publisher.getName(), publisher.getCity(), publisher.getCountry()});
            List<Book> publisherBooks = bookService.findByPublisher(publisher.getId());
            for (Book book : publisherBooks) {
                LOG.log(Level.INFO, "  Book: {0} by {1}", new Object[]{book.getTitle(), book.getAuthor()});
            }
        }
        
        // Display Libraries and their Staff
        LOG.info("\n--- LIBRARIES AND THEIR STAFF ---");
        List<Library> libraries = libraryService.findAll();
        for (Library library : libraries) {
            LOG.log(Level.INFO, "Library: {0} ({1}, {2})", 
                   new Object[]{library.getName(), library.getCity(), library.getState()});
            List<Librarian> librarians = librarianService.findByLibrary(library.getId());
            for (Librarian librarian : librarians) {
                LOG.log(Level.INFO, "  Librarian: {0} {1} - {2}", 
                       new Object[]{librarian.getFirstName(), librarian.getLastName(), librarian.getPosition()});
            }
        }
        
        // Display Borrowers and their Loans
        LOG.info("\n--- BORROWERS AND THEIR LOANS ---");
        List<Borrower> borrowers = borrowerService.findAll();
        for (Borrower borrower : borrowers) {
            LOG.log(Level.INFO, "Borrower: {0} {1} ({2})", 
                   new Object[]{borrower.getFirstName(), borrower.getLastName(), borrower.getEmail()});
            List<BookLoan> loans = bookLoanService.findByBorrower(borrower.getId());
            for (BookLoan loan : loans) {
                LOG.log(Level.INFO, "  Loan: \"{0}\" due {1} (Status: {2})", 
                       new Object[]{loan.getBook().getTitle(), loan.getDueDate(), 
                                  loan.getReturnDate() == null ? "Active" : "Returned"});
            }
        }
        
        // Display Active Loans by Library
        LOG.info("\n--- ACTIVE LOANS BY LIBRARY ---");
        for (Library library : libraries) {
            long activeLoans = libraryService.countActiveLoans(library.getId());
            long totalLoans = libraryService.countTotalLoans(library.getId());
            LOG.log(Level.INFO, "{0}: {1} active loans out of {2} total loans", 
                   new Object[]{library.getName(), activeLoans, totalLoans});
        }
        
        // Display Overdue Loans
        LOG.info("\n--- OVERDUE LOANS ---");
        List<BookLoan> overdueLoans = bookLoanService.findOverdueLoans();
        if (overdueLoans.isEmpty()) {
            LOG.info("No overdue loans");
        } else {
            for (BookLoan loan : overdueLoans) {
                LOG.log(Level.INFO, "Overdue: \"{0}\" borrowed by {1} (Due: {2})", 
                       new Object[]{loan.getBook().getTitle(), loan.getBorrowerName(), loan.getDueDate()});
            }
        }
        
        LOG.info("\n=== RELATIONSHIP DEMONSTRATION COMPLETE ===");
    }
<<<<<<< HEAD
    
    private void createSecurityData(Librarian headLibrarian, Librarian branchLibrarian, 
                                   Borrower studentBorrower, Borrower teacherBorrower, Borrower parentBorrower) {
        LOG.info("Creating security data...");
        
        // Get or ensure the groups exist
        Group adminGroup = groupService.findByGroupName("ADMIN");
        if (adminGroup == null) {
            LOG.log(Level.WARNING, "ADMIN group not found, creating it");
            adminGroup = new Group("ADMIN", "System Administrators with full access");
            adminGroup = groupService.create(adminGroup);
        }
        
        Group librarianGroup = groupService.findByGroupName("LIBRARIAN");
        if (librarianGroup == null) {
            LOG.log(Level.WARNING, "LIBRARIAN group not found, creating it");
            librarianGroup = new Group("LIBRARIAN", "Librarians with book management access");
            librarianGroup = groupService.create(librarianGroup);
        }
        
        Group userGroup = groupService.findByGroupName("USER");
        if (userGroup == null) {
            LOG.log(Level.WARNING, "USER group not found, creating it");
            userGroup = new Group("USER", "Regular users with borrowing privileges");
            userGroup = groupService.create(userGroup);
        }
        
        // Create admin user for head librarian
        User adminUser = new User("admin", "admin123", "admin@library.edu");
        adminUser = userService.create(adminUser);
        adminUser.addGroup(adminGroup);
        userService.update(adminUser);
        LOG.log(Level.INFO, "Created admin user: {0}", adminUser.getUsername());
        
        // Create librarian user for branch librarian
        User librarianUser = new User("librarian", "lib123", "librarian@library.edu");
        librarianUser = userService.create(librarianUser);
        librarianUser.addGroup(librarianGroup);
        userService.update(librarianUser);
        LOG.log(Level.INFO, "Created librarian user: {0}", librarianUser.getUsername());
        
        // Create borrower users
        User studentUser = new User("student", "student123", studentBorrower.getEmail());
        studentUser.setBorrower(studentBorrower);
        studentUser = userService.create(studentUser);
        studentUser.addGroup(userGroup);
        userService.update(studentUser);
        LOG.log(Level.INFO, "Created student user: {0}", studentUser.getUsername());
        
        User teacherUser = new User("teacher", "teacher123", teacherBorrower.getEmail());
        teacherUser.setBorrower(teacherBorrower);
        teacherUser = userService.create(teacherUser);
        teacherUser.addGroup(userGroup);
        userService.update(teacherUser);
        LOG.log(Level.INFO, "Created teacher user: {0}", teacherUser.getUsername());
        
        User parentUser = new User("parent", "parent123", parentBorrower.getEmail());
        parentUser.setBorrower(parentBorrower);
        parentUser = userService.create(parentUser);
        parentUser.addGroup(userGroup);
        userService.update(parentUser);
        LOG.log(Level.INFO, "Created parent user: {0}", parentUser.getUsername());
        
        LOG.info("Security data created successfully");
    }
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
}