package edu.iit.itmd4515;

import edu.iit.itmd4515.domain.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for NetBeans to run the JPA project.
 * This class demonstrates the JPA relationships implemented in Lab 5.
 * Uses direct JDBC connection instead of JNDI for standalone execution.
 */
public class Main {
    
    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {
        LOG.info("Starting JPA Lab 5 - Relationships Demo");
        
        // Create EntityManagerFactory using standalone persistence unit
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("itmd4515StandalonePU");
        EntityManager em = emf.createEntityManager();
        
        try {
            // Demonstrate relationships
            demonstrateRelationships(em);
            
            LOG.info("JPA Lab 5 - Relationships Demo completed successfully!");
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error during demo", e);
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
    
    private static void demonstrateRelationships(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            // Create a publisher
            Publisher publisher = new Publisher("Demo Publisher", "123 Main St", "Chicago", "USA");
            publisher.setEmail("contact@demopublisher.com");
            em.persist(publisher);
            
            // Create authors
            Author author1 = new Author("John", "Doe", "USA");
            author1.setEmail("john.doe@example.com");
            em.persist(author1);
            
            Author author2 = new Author("Jane", "Smith", "UK");
            author2.setEmail("jane.smith@example.com");
            em.persist(author2);
            
            // Create a book
            Book book = new Book("JPA Relationships Guide", "John Doe", "9781234567890");
            book.setPublicationDate(LocalDate.of(2024, 1, 15));
            book.setPageCount(250);
            book.setPrice(29.99);
            book.setIsAvailable(true);
            
            // Establish relationships
            book.setPublisher(publisher);
            book.addAuthor(author1);
            book.addAuthor(author2);
            
            em.persist(book);
            
            // Create a library
            Library library = new Library("Demo Library", "456 Oak Ave", "Chicago", "IL", "60601",
                                         LocalTime.of(9, 0), LocalTime.of(18, 0), 200);
            library.setEmail("info@demolibrary.org");
            em.persist(library);
            
            // Create a book loan
            BookLoan loan = new BookLoan(LocalDate.now(), LocalDate.now().plusDays(14), "Student Name");
            loan.setBorrowerEmail("student@example.com");
            loan.setBook(book);
            loan.setLibrary(library);
            
            em.persist(loan);
            
            tx.commit();
            
            // Query and display results
            displayResults(em);
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    
    private static void displayResults(EntityManager em) {
        LOG.info("=== DEMONSTRATION RESULTS ===");
        
        // Display books with their relationships
        TypedQuery<Book> bookQuery = em.createQuery("SELECT b FROM Book b", Book.class);
        List<Book> books = bookQuery.getResultList();
        
        for (Book book : books) {
            LOG.log(Level.INFO, "\n--- Book: {0} ---", book.getTitle());
            LOG.log(Level.INFO, "ISBN: {0}", book.getIsbn());
            LOG.log(Level.INFO, "Author: {0}", book.getAuthor());
            
            if (book.getPublisher() != null) {
                LOG.log(Level.INFO, "Publisher: {0}", book.getPublisher().getName());
            }
            
            LOG.log(Level.INFO, "Authors count: {0}", book.getAuthors().size());
            for (Author author : book.getAuthors()) {
                LOG.log(Level.INFO, "  - {0} {1}", new Object[]{author.getFirstName(), author.getLastName()});
            }
            
            LOG.log(Level.INFO, "Book loans count: {0}", book.getBookLoans().size());
        }
        
        // Display publishers with their books
        TypedQuery<Publisher> publisherQuery = em.createQuery("SELECT p FROM Publisher p", Publisher.class);
        List<Publisher> publishers = publisherQuery.getResultList();
        
        for (Publisher publisher : publishers) {
            LOG.log(Level.INFO, "\n--- Publisher: {0} ---", publisher.getName());
            LOG.log(Level.INFO, "Books published: {0}", publisher.getBooks().size());
            for (Book pubBook : publisher.getBooks()) {
                LOG.log(Level.INFO, "  - {0}", pubBook.getTitle());
            }
        }
        
        // Display libraries with their loans
        TypedQuery<Library> libraryQuery = em.createQuery("SELECT l FROM Library l", Library.class);
        List<Library> libraries = libraryQuery.getResultList();
        
        for (Library library : libraries) {
            LOG.log(Level.INFO, "\n--- Library: {0} ---", library.getName());
            LOG.log(Level.INFO, "Location: {0}, {1}", new Object[]{library.getCity(), library.getState()});
            LOG.log(Level.INFO, "Book loans: {0}", library.getBookLoans().size());
            for (BookLoan loan : library.getBookLoans()) {
                LOG.log(Level.INFO, "  - {0} by {1} (Due: {2})", 
                       new Object[]{loan.getBook().getTitle(), loan.getBorrowerName(), loan.getDueDate()});
            }
        }
        
        LOG.info("\n=== RELATIONSHIPS DEMONSTRATION COMPLETE ===");
    }
}