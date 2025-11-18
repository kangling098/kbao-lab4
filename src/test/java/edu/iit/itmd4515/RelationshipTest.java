package edu.iit.itmd4515;

import edu.iit.itmd4515.domain.*;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelationshipTest {
    
    private static final Logger LOG = Logger.getLogger(RelationshipTest.class.getName());
    
    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;
    
    @BeforeAll
    public static void setUpClass() {
        LOG.info("Creating EntityManagerFactory for relationship tests...");
        emf = Persistence.createEntityManagerFactory("itmd4515testPU");
    }
    
    @AfterAll
    public static void tearDownClass() {
        LOG.info("Closing EntityManagerFactory...");
        if (emf != null) {
            emf.close();
        }
    }
    
    @BeforeEach
    public void setUp() {
        LOG.info("Setting up test environment...");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }
    
    @AfterEach
    public void tearDown() {
        LOG.info("Tearing down test environment...");
        if (em != null) {
            em.close();
        }
    }
    
    @Test
    @DisplayName("Test Unidirectional ManyToOne Relationship - Book to Publisher")
    public void testBookPublisherUnidirectionalRelationship() {
        LOG.info("Testing unidirectional ManyToOne relationship between Book and Publisher...");
        
        // Create a publisher
        Publisher publisher = new Publisher("Penguin Random House", "1745 Broadway", "New York", "USA");
        publisher.setEmail("contact@penguinrandomhouse.com");
        publisher.setFoundedDate(LocalDate.of(1927, 7, 1));
        
        // Create a book
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273561");
        book.setPublicationDate(LocalDate.of(1925, 4, 10));
        book.setPageCount(180);
        book.setPrice(14.99);
        book.setIsAvailable(true);
        
        // Establish relationship (unidirectional from Book to Publisher)
        book.setPublisher(publisher);
        
        tx.begin();
        em.persist(publisher);
        em.persist(book);
        tx.commit();
        
        assertNotNull(book.getId(), "Book ID should not be null after persistence");
        assertNotNull(publisher.getId(), "Publisher ID should not be null after persistence");
        
        // Verify the relationship
        Book foundBook = em.find(Book.class, book.getId());
        assertNotNull(foundBook.getPublisher(), "Book should have a publisher");
        assertEquals("Penguin Random House", foundBook.getPublisher().getName(), "Publisher name should match");
        assertEquals("New York", foundBook.getPublisher().getCity(), "Publisher city should match");
        
        LOG.log(Level.INFO, "Book '{0}' is published by '{1}'", 
                new Object[]{foundBook.getTitle(), foundBook.getPublisher().getName()});
    }
    
    /*
    @Test
    @DisplayName("Test Book-Publisher ManyToOne Relationship")
    public void testBookPublisherManyToOneRelationship() {
        LOG.info("Testing ManyToOne relationship between Book and Publisher...");
        
        // Create publishers
        Publisher techPublisher = new Publisher("Tech Books Publishing", "123 Tech St", "San Francisco", "USA");
        techPublisher.setActive(true);
        
        Publisher eduPublisher = new Publisher("Education Press", "456 Edu Ave", "Boston", "USA");
        eduPublisher.setActive(true);
        
        // Create books
        Book book1 = new Book("Java Programming Fundamentals", "Dr. Alice Smith", "978-0-123456-78-9");
        book1.setPublicationDate(LocalDate.of(2023, 6, 15));
        book1.setPageCount(450);
        book1.setPrice(59.99);
        book1.setIsAvailable(true);
        book1.setPublisher(techPublisher);
        
        Book book2 = new Book("Database Design and Implementation", "Prof. Bob Wilson", "978-0-987654-32-1");
        book2.setPublicationDate(LocalDate.of(2023, 3, 20));
        book2.setPageCount(380);
        book2.setPrice(54.99);
        book2.setIsAvailable(true);
        book2.setPublisher(techPublisher);
        
        Book book3 = new Book("Web Development with Modern Frameworks", "Dr. Carol Davis", "978-0-567890-12-3");
        book3.setPublicationDate(LocalDate.of(2023, 9, 10));
        book3.setPageCount(520);
        book3.setPrice(69.99);
        book3.setIsAvailable(true);
        book3.setPublisher(eduPublisher);
        
        tx.begin();
        em.persist(techPublisher);
        em.persist(eduPublisher);
        em.persist(book1);
        em.persist(book2);
        em.persist(book3);
        tx.commit();
        
        assertNotNull(book1.getId(), "Book1 ID should not be null after persistence");
        assertNotNull(book2.getId(), "Book2 ID should not be null after persistence");
        assertNotNull(book3.getId(), "Book3 ID should not be null after persistence");
        assertNotNull(techPublisher.getId(), "Tech Publisher ID should not be null after persistence");
        assertNotNull(eduPublisher.getId(), "Education Publisher ID should not be null after persistence");
        
        // Clear persistence context to ensure we're reading from database
        em.clear();
        
        // Verify the ManyToOne relationship from Book side
        Book foundBook1 = em.find(Book.class, book1.getId());
        assertNotNull(foundBook1.getPublisher(), "Book should have a publisher");
        assertEquals("Tech Books Publishing", foundBook1.getPublisher().getName(), 
                    "Book1 should be published by Tech Books Publishing");
        
        Book foundBook3 = em.find(Book.class, book3.getId());
        assertEquals("Education Press", foundBook3.getPublisher().getName(), 
                    "Book3 should be published by Education Press");
        
        // Verify the OneToMany relationship from Publisher side
        Publisher foundTechPublisher = em.find(Publisher.class, techPublisher.getId());
        assertNotNull(foundTechPublisher.getBooks(), "Publisher should have books");
        assertEquals(2, foundTechPublisher.getBooks().size(), "Tech publisher should have 2 books");
        
        // Test updating relationship
        tx.begin();
        Book bookToUpdate = em.find(Book.class, book3.getId());
        bookToUpdate.setPublisher(foundTechPublisher); // Move book3 to tech publisher
        tx.commit();
        
        // Verify the relationship was updated
        em.clear();
        Book updatedBook = em.find(Book.class, book3.getId());
        assertEquals("Tech Books Publishing", updatedBook.getPublisher().getName(), 
                    "Book3 should now be published by Tech Books Publishing");
        
        Publisher updatedTechPublisher = em.find(Publisher.class, techPublisher.getId());
        assertEquals(3, updatedTechPublisher.getBooks().size(), "Tech publisher should now have 3 books");
        LOG.log(Level.INFO, "Book '{0}' is published by '{1}'", 
                new Object[]{foundBook1.getTitle(), foundBook1.getPublisher().getName()});
        LOG.log(Level.INFO, "Publisher '{0}' has {1} books", 
                new Object[]{foundTechPublisher.getName(), foundTechPublisher.getBooks().size()});
    }
    */
    
    @Test
    @DisplayName("Test Bidirectional OneToMany Relationship - Publisher to Book")
    public void testPublisherBookBidirectionalRelationship() {
        LOG.info("Testing bidirectional OneToMany relationship between Publisher and Book...");
        
        // Create a publisher
        Publisher publisher = new Publisher("HarperCollins", "195 Broadway", "New York", "USA");
        publisher.setEmail("info@harpercollins.com");
        publisher.setFoundedDate(LocalDate.of(1989, 1, 1));
        
        // Create books
        Book book1 = new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084");
        book1.setPublicationDate(LocalDate.of(1960, 7, 11));
        book1.setPageCount(324);
        book1.setPrice(16.99);
        book1.setIsAvailable(true);
        
        Book book2 = new Book("1984", "George Orwell", "9780451524935");
        book2.setPublicationDate(LocalDate.of(1949, 6, 8));
        book2.setPageCount(328);
        book2.setPrice(15.99);
        book2.setIsAvailable(true);
        
        // Establish bidirectional relationships using helper methods
        publisher.addBook(book1);
        publisher.addBook(book2);
        
        tx.begin();
        em.persist(publisher);
        // Books are persisted automatically due to cascade
        tx.commit();
        
        assertNotNull(publisher.getId(), "Publisher ID should not be null after persistence");
        assertNotNull(book1.getId(), "Book1 ID should not be null after persistence");
        assertNotNull(book2.getId(), "Book2 ID should not be null after persistence");
        
        // Clear persistence context to ensure we're reading from database
        em.clear();
        
        // Verify the bidirectional relationship from Publisher side
        Publisher foundPublisher = em.find(Publisher.class, publisher.getId());
        assertNotNull(foundPublisher.getBooks(), "Publisher should have books");
        assertEquals(2, foundPublisher.getBooks().size(), "Publisher should have 2 books");
        
        // Verify the bidirectional relationship from Book side
        Book foundBook1 = em.find(Book.class, book1.getId());
        assertNotNull(foundBook1.getPublisher(), "Book should have a publisher");
        assertEquals("HarperCollins", foundBook1.getPublisher().getName(), "Publisher name should match");
        
        // Test removing relationship
        tx.begin();
        Publisher publisherToUpdate = em.find(Publisher.class, publisher.getId());
        Book bookToRemove = publisherToUpdate.getBooks().stream()
            .filter(b -> b.getTitle().equals("1984"))
            .findFirst().orElse(null);
        
        if (bookToRemove != null) {
            publisherToUpdate.removeBook(bookToRemove);
        }
        tx.commit();
        
        // Verify the relationship was removed
        em.clear();
        Publisher updatedPublisher = em.find(Publisher.class, publisher.getId());
        assertEquals(1, updatedPublisher.getBooks().size(), "Publisher should now have 1 book");
        
        // Note: Due to orphanRemoval=true, the removed book was deleted from the database
        // So we can't find it anymore, which is expected behavior
        Book deletedBook = em.find(Book.class, book2.getId());
        assertNull(deletedBook, "Book should have been deleted due to orphan removal");
        
        LOG.log(Level.INFO, "Publisher '{0}' has published {1} books", 
                new Object[]{foundPublisher.getName(), foundPublisher.getBooks().size()});
    }
    
    /*
    @Test
    @DisplayName("Test Bidirectional OneToMany Relationship - Library to BookLoan")
    public void testLibraryBookLoanBidirectionalRelationship() {
        LOG.info("Testing bidirectional OneToMany relationship between Library and BookLoan...");
        
        // Create borrowers
        Borrower borrower1 = new Borrower("John", "Doe", "john.doe@example.com", "555-123-4567");
        borrower1.setBirthDate(LocalDate.of(1990, 1, 1));
        borrower1.setAddress("123 Main St");
        borrower1.setCity("Chicago");
        borrower1.setState("IL");
        borrower1.setZipCode("60601");
        borrower1.setMembershipActive(true);
        borrower1.setMembershipDate(LocalDate.now());
        
        Borrower borrower2 = new Borrower("Jane", "Smith", "jane.smith@example.com", "555-987-6543");
        borrower2.setBirthDate(LocalDate.of(1992, 2, 2));
        borrower2.setAddress("456 Oak Ave");
        borrower2.setCity("Chicago");
        borrower2.setState("IL");
        borrower2.setZipCode("60602");
        borrower2.setMembershipActive(true);
        borrower2.setMembershipDate(LocalDate.now());
        
        // Create a library
        Library library = new Library("Chicago Public Library", "400 S State St", "Chicago", "IL", "60605",
                                      LocalTime.of(9, 0), LocalTime.of(17, 0), 500);
        library.setEmail("info@chicagopubliclibrary.org");
        library.setPhoneNumber("312-747-4300");
        
        // Create a book
        Book book = new Book("The Catcher in the Rye", "J.D. Salinger", "9780316769481");
        book.setPublicationDate(LocalDate.of(1951, 7, 16));
        book.setPageCount(277);
        book.setPrice(13.99);
        book.setIsAvailable(true);
        
        // Create book loans with proper borrower relationships
        BookLoan loan1 = new BookLoan(LocalDate.now(), LocalDate.now().plusDays(14), borrower1);
        
        BookLoan loan2 = new BookLoan(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), borrower2);
        
        // Establish relationships
        library.addBookLoan(loan1);
        library.addBookLoan(loan2);
        book.addBookLoan(loan1);
        book.addBookLoan(loan2);
        
        tx.begin();
        em.persist(borrower1);
        em.persist(borrower2);
        em.persist(library);
        em.persist(book);
        // BookLoans are persisted automatically due to cascade
        tx.commit();
        
        assertNotNull(library.getId(), "Library ID should not be null after persistence");
        assertNotNull(book.getId(), "Book ID should not be null after persistence");
        assertNotNull(loan1.getId(), "Loan1 ID should not be null after persistence");
        assertNotNull(loan2.getId(), "Loan2 ID should not be null after persistence");
        
        // Clear persistence context to ensure we're reading from database
        em.clear();
        
        // Verify the bidirectional relationship from Library side
        Library foundLibrary = em.find(Library.class, library.getId());
        assertNotNull(foundLibrary.getBookLoans(), "Library should have book loans");
        assertEquals(2, foundLibrary.getBookLoans().size(), "Library should have 2 book loans");
        
        // Verify the bidirectional relationship from BookLoan side
        BookLoan foundLoan1 = em.find(BookLoan.class, loan1.getId());
        assertNotNull(foundLoan1.getLibrary(), "BookLoan should have a library");
        assertEquals("Chicago Public Library", foundLoan1.getLibrary().getName(), "Library name should match");
        
        // Verify the relationship with Book
        assertNotNull(foundLoan1.getBook(), "BookLoan should have a book");
        assertEquals("The Catcher in the Rye", foundLoan1.getBook().getTitle(), "Book title should match");
        
        // Test overdue functionality
        assertFalse(foundLoan1.isOverdue(), "Active loan should not be overdue");
        
        BookLoan overdueLoan = foundLibrary.getBookLoans().stream()
            .filter(loan -> loan.getDueDate().equals(LocalDate.now().plusDays(7)))
            .findFirst().orElse(null);
        
        if (overdueLoan != null) {
            // Simulate making the loan overdue by setting due date to past
            tx.begin();
            BookLoan loanToUpdate = em.find(BookLoan.class, overdueLoan.getId());
            loanToUpdate.setDueDate(LocalDate.now().minusDays(1));
            tx.commit();
            
            em.clear();
            BookLoan updatedOverdueLoan = em.find(BookLoan.class, overdueLoan.getId());
            assertTrue(updatedOverdueLoan.isOverdue(), "Loan should be overdue");
            assertEquals(1, updatedOverdueLoan.getDaysOverdue(), "Should be 1 day overdue");
        }
        
        LOG.log(Level.INFO, "Library '{0}' has {1} active book loans", 
                new Object[]{foundLibrary.getName(), foundLibrary.getBookLoans().size()});
    }
    */
}