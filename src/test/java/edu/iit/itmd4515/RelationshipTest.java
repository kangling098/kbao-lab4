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
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273560");
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
    
    @Test
    @DisplayName("Test Bidirectional ManyToMany Relationship - Book to Author")
    public void testBookAuthorBidirectionalRelationship() {
        LOG.info("Testing bidirectional ManyToMany relationship between Book and Author...");
        
        // Create authors
        Author author1 = new Author("F. Scott", "Fitzgerald", "USA");
        author1.setBirthDate(LocalDate.of(1896, 9, 24));
        author1.setEmail("f.scott.fitzgerald@example.com");
        
        Author author2 = new Author("Ernest", "Hemingway", "USA");
        author2.setBirthDate(LocalDate.of(1899, 7, 21));
        author2.setEmail("ernest.hemingway@example.com");
        
        // Create books
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273561");
        book1.setPublicationDate(LocalDate.of(1925, 4, 10));
        book1.setPageCount(180);
        book1.setPrice(14.99);
        book1.setIsAvailable(true);
        
        Book book2 = new Book("The Old Man and the Sea", "Ernest Hemingway", "9780684801224");
        book2.setPublicationDate(LocalDate.of(1952, 9, 1));
        book2.setPageCount(127);
        book2.setPrice(12.99);
        book2.setIsAvailable(true);
        
        // Establish bidirectional relationships using helper methods
        book1.addAuthor(author1);  // Fitzgerald wrote The Great Gatsby
        book2.addAuthor(author2);  // Hemingway wrote The Old Man and the Sea
        
        // Also demonstrate that we can add multiple authors to a book
        Author coAuthor = new Author("Co", "Author", "UK");
        coAuthor.setEmail("co.author@example.com");
        book1.addAuthor(coAuthor);  // Add a co-author to The Great Gatsby
        
        tx.begin();
        em.persist(author1);
        em.persist(author2);
        em.persist(coAuthor);
        em.persist(book1);
        em.persist(book2);
        tx.commit();
        
        assertNotNull(book1.getId(), "Book1 ID should not be null after persistence");
        assertNotNull(book2.getId(), "Book2 ID should not be null after persistence");
        assertNotNull(author1.getId(), "Author1 ID should not be null after persistence");
        assertNotNull(author2.getId(), "Author2 ID should not be null after persistence");
        
        // Clear persistence context to ensure we're reading from database
        em.clear();
        
        // Verify the bidirectional relationship from Book side
        Book foundBook1 = em.find(Book.class, book1.getId());
        assertNotNull(foundBook1.getAuthors(), "Book should have authors");
        assertEquals(2, foundBook1.getAuthors().size(), "Book1 should have 2 authors");
        
        // Verify the bidirectional relationship from Author side
        Author foundAuthor1 = em.find(Author.class, author1.getId());
        assertNotNull(foundAuthor1.getBooks(), "Author should have books");
        assertEquals(1, foundAuthor1.getBooks().size(), "Author1 should have 1 book");
        assertEquals("The Great Gatsby", foundAuthor1.getBooks().get(0).getTitle(), 
                    "Author's book title should match");
        
        // Test removing relationship
        tx.begin();
        Book bookToUpdate = em.find(Book.class, book1.getId());
        Author authorToRemove = bookToUpdate.getAuthors().stream()
            .filter(a -> a.getEmail().equals("co.author@example.com"))
            .findFirst().orElse(null);
        
        if (authorToRemove != null) {
            bookToUpdate.removeAuthor(authorToRemove);
        }
        tx.commit();
        
        // Verify the relationship was removed
        em.clear();
        Book updatedBook = em.find(Book.class, book1.getId());
        assertEquals(1, updatedBook.getAuthors().size(), "Book1 should now have 1 author");
        
        Author updatedAuthor = em.find(Author.class, coAuthor.getId());
        assertEquals(0, updatedAuthor.getBooks().size(), "Co-author should have 0 books");
        
        LOG.log(Level.INFO, "Book '{0}' has {1} authors", 
                new Object[]{foundBook1.getTitle(), foundBook1.getAuthors().size()});
        LOG.log(Level.INFO, "Author '{0}' has written {1} books", 
                new Object[]{foundAuthor1.getFullName(), foundAuthor1.getBooks().size()});
    }
    
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
    
    @Test
    @DisplayName("Test Bidirectional OneToMany Relationship - Library to BookLoan")
    public void testLibraryBookLoanBidirectionalRelationship() {
        LOG.info("Testing bidirectional OneToMany relationship between Library and BookLoan...");
        
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
        
        // Create book loans
        BookLoan loan1 = new BookLoan(LocalDate.now(), LocalDate.now().plusDays(14), "John Doe");
        loan1.setBorrowerEmail("john.doe@example.com");
        loan1.setBorrowerPhone("555-123-4567");
        
        BookLoan loan2 = new BookLoan(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), "Jane Smith");
        loan2.setBorrowerEmail("jane.smith@example.com");
        loan2.setBorrowerPhone("555-987-6543");
        
        // Establish relationships
        library.addBookLoan(loan1);
        library.addBookLoan(loan2);
        book.addBookLoan(loan1);
        book.addBookLoan(loan2);
        
        tx.begin();
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
}