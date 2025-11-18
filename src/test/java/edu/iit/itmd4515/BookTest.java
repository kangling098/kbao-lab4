package edu.iit.itmd4515;

import edu.iit.itmd4515.domain.Book;
import edu.iit.itmd4515.domain.Publisher;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookTest {
    
    private static final Logger LOG = Logger.getLogger(BookTest.class.getName());
    
    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;
    
    @BeforeAll
    public static void setUpClass() {
        LOG.info("Creating EntityManagerFactory...");
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
    @DisplayName("Test Create Book - em.persist")
    public void testCreateBook() {
        LOG.info("Testing Create Book operation...");
        
        // Create a publisher first
        Publisher publisher = new Publisher("Prentice Hall " + System.currentTimeMillis(), "One Lake Street", "Upper Saddle River", "USA");
        publisher.setActive(true);
        
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884");
        book.setPublicationDate(LocalDate.of(2008, 8, 1));
        book.setPageCount(464);
        book.setPrice(42.50);
        book.setIsAvailable(true);
        book.setPublisher(publisher);
        
        tx.begin();
        em.persist(publisher);
        em.persist(book);
        tx.commit();
        
        assertNotNull(book.getId(), "Book ID should not be null after persistence");
        LOG.log(Level.INFO, "Created book with ID: {0}", book.getId());
        
        // Verify the book was actually created
        Book foundBook = em.find(Book.class, book.getId());
        assertNotNull(foundBook, "Book should be found after creation");
        assertEquals("Clean Code", foundBook.getTitle(), "Book title should match");
        assertEquals("Robert C. Martin", foundBook.getAuthor(), "Book author should match");
        assertNotNull(foundBook.getPublisher(), "Book should have a publisher");
    }
    
    @Test
    @DisplayName("Test Read Book - em.find")
    public void testReadBook() {
        LOG.info("Testing Read Book operation...");
        
        // First create a publisher and book to read
        Publisher publisher = new Publisher("Addison-Wesley Professional " + System.currentTimeMillis(), "75 Arlington St", "Boston", "USA");
        publisher.setActive(true);
        
        Book book = new Book("Design Patterns", "Gang of Four", "9780201633610");
        book.setPublicationDate(LocalDate.of(1994, 10, 31));
        book.setPageCount(395);
        book.setPrice(54.99);
        book.setPublisher(publisher);
        
        tx.begin();
        em.persist(publisher);
        em.persist(book);
        tx.commit();
        
        // Clear persistence context to ensure we're reading from database
        em.clear();
        
        // Read the book
        Book foundBook = em.find(Book.class, book.getId());
        
        assertNotNull(foundBook, "Book should be found");
        assertEquals("Design Patterns", foundBook.getTitle(), "Book title should match");
        assertEquals("Gang of Four", foundBook.getAuthor(), "Book author should match");
        assertEquals("9780201633610", foundBook.getIsbn(), "Book ISBN should match");
        assertNotNull(foundBook.getPublisher(), "Book should have a publisher");
        
        LOG.log(Level.INFO, "Found book: {0}", foundBook);
    }
    
    @Test
    @DisplayName("Test Update Book - entity mutators within transaction")
    public void testUpdateBook() {
        LOG.info("Testing Update Book operation...");
        
        // Create a publisher and book first
        Publisher publisher = new Publisher("Addison-Wesley Professional " + (System.currentTimeMillis() + 1), "75 Arlington St", "Boston", "USA");
        publisher.setActive(true);
        
        Book book = new Book("Refactoring", "Martin Fowler", "9780201485677");
        book.setPublicationDate(LocalDate.of(1999, 7, 8));
        book.setPageCount(464);
        book.setPrice(44.99);
        book.setIsAvailable(true);
        book.setPublisher(publisher);
        
        tx.begin();
        em.persist(publisher);
        em.persist(book);
        tx.commit();
        
        Long bookId = book.getId();
        
        // Update the book
        tx.begin();
        Book bookToUpdate = em.find(Book.class, bookId);
        bookToUpdate.setPrice(49.99);
        bookToUpdate.setIsAvailable(false);
        bookToUpdate.setDueDate(LocalDate.now().plusDays(14));
        tx.commit();
        
        // Verify the update
        em.clear();
        Book updatedBook = em.find(Book.class, bookId);
        
        assertEquals(49.99, updatedBook.getPrice(), 0.01, "Price should be updated");
        assertFalse(updatedBook.getIsAvailable(), "Availability should be updated to false");
        assertNotNull(updatedBook.getDueDate(), "Due date should be set");
        assertNotNull(updatedBook.getPublisher(), "Book should have a publisher");
        
        LOG.log(Level.INFO, "Updated book: {0}", updatedBook);
    }
    
    @Test
    @DisplayName("Test Delete Book - em.remove")
    public void testDeleteBook() {
        LOG.info("Testing Delete Book operation...");
        
        // Create a publisher and book to delete
        Publisher publisher = new Publisher("Addison-Wesley Professional " + (System.currentTimeMillis() + 2), "75 Arlington St", "Boston", "USA");
        publisher.setActive(true);
        
        Book book = new Book("The Pragmatic Programmer", "Andrew Hunt", "9780201616224");
        book.setPublicationDate(LocalDate.of(1999, 10, 30));
        book.setPageCount(352);
        book.setPrice(42.95);
        book.setPublisher(publisher);
        
        tx.begin();
        em.persist(publisher);
        em.persist(book);
        tx.commit();
        
        Long bookId = book.getId();
        
        // Delete the book
        tx.begin();
        Book bookToDelete = em.find(Book.class, bookId);
        em.remove(bookToDelete);
        tx.commit();
        
        // Verify the deletion
        em.clear();
        Book deletedBook = em.find(Book.class, bookId);
        
        assertNull(deletedBook, "Book should be null after deletion");
        LOG.log(Level.INFO, "Deleted book with ID: {0}", bookId);
    }
    
    @Test
    @DisplayName("Test Find All Books - Named Query")
    public void testFindAllBooks() {
        LOG.info("Testing Find All Books operation...");
        
        // Create a publisher
        Publisher publisher = new Publisher("Test Publisher " + (System.currentTimeMillis() + 3), "123 Test St", "Test City", "USA");
        publisher.setActive(true);
        
        // Create some test books
        Book book1 = new Book("Book 1", "Author 1", "1111111111");
        book1.setPublisher(publisher);
        
        Book book2 = new Book("Book 2", "Author 2", "2222222222");
        book2.setPublisher(publisher);
        
        Book book3 = new Book("Book 3", "Author 3", "3333333333");
        book3.setPublisher(publisher);
        
        tx.begin();
        em.persist(publisher);
        em.persist(book1);
        em.persist(book2);
        em.persist(book3);
        tx.commit();
        
        // Find all books using named query
        TypedQuery<Book> query = em.createNamedQuery("Book.findAll", Book.class);
        List<Book> allBooks = query.getResultList();
        
        assertFalse(allBooks.isEmpty(), "Book list should not be empty");
        assertTrue(allBooks.size() >= 3, "Should have at least 3 books");
        
        LOG.log(Level.INFO, "Found {0} books in total", allBooks.size());
        allBooks.forEach(book -> LOG.log(Level.INFO, "Book: {0}", book));
    }
    
    @Test
    @DisplayName("Test Find Book by ISBN - Named Query")
    public void testFindByIsbn() {
        LOG.info("Testing Find Book by ISBN operation...");
        
        String isbn = "1234567890";
        Publisher publisher = new Publisher("Test Publisher " + (System.currentTimeMillis() + 4), "123 Test St", "Test City", "USA");
        publisher.setActive(true);
        
        Book book = new Book("Test Book", "Test Author", isbn);
        book.setPublisher(publisher);
        
        tx.begin();
        em.persist(publisher);
        em.persist(book);
        tx.commit();
        
        // Find by ISBN using named query
        TypedQuery<Book> query = em.createNamedQuery("Book.findByIsbn", Book.class);
        query.setParameter("isbn", isbn);
        Book foundBook = query.getSingleResult();
        
        assertNotNull(foundBook, "Book should be found by ISBN");
        assertEquals(isbn, foundBook.getIsbn(), "ISBN should be match");
        assertEquals("Test Book", foundBook.getTitle(), "Title should match");
        assertNotNull(foundBook.getPublisher(), "Book should have a publisher");
        
        LOG.log(Level.INFO, "Found book by ISBN: {0}", foundBook);
    }
}