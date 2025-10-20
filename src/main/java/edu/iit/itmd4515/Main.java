package edu.iit.itmd4515;

import edu.iit.itmd4515.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for NetBeans to run the EJB Lab 6 project.
 * This class demonstrates the EJB service layer using JPA persistence.
 */
public class Main {
    
    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {
        LOG.info("Starting EJB Lab 6 - Service Layer Demo");
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            // Create EntityManagerFactory
            emf = Persistence.createEntityManagerFactory("itmd4515StandalonePU");
            em = emf.createEntityManager();
            
            // Create and demonstrate services
            demonstrateServices(em);
            
            LOG.info("EJB Lab 6 - Service Layer Demo completed successfully!");
            
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
    
    private static void demonstrateServices(EntityManager em) {
        LOG.info("=== DEMONSTRATING EJB SERVICE LAYER ===");
        
        // Create services with injected EntityManager
        BookService bookService = new BookService();
        bookService.em = em;
        
        BorrowerService borrowerService = new BorrowerService();
        borrowerService.em = em;
        
        LibraryService libraryService = new LibraryService();
        libraryService.em = em;
        
        BookLoanService bookLoanService = new BookLoanService();
        bookLoanService.em = em;
        
        PublisherService publisherService = new PublisherService();
        publisherService.em = em;
        
        LibrarianService librarianService = new LibrarianService();
        librarianService.em = em;
        
        // Create database seeder and run it
        DatabaseSeedService seedService = new DatabaseSeedService();
        seedService.bookService = bookService;
        seedService.borrowerService = borrowerService;
        seedService.libraryService = libraryService;
        seedService.bookLoanService = bookLoanService;
        seedService.publisherService = publisherService;
        seedService.librarianService = librarianService;
        
        // Run database seeding
        seedService.seedDatabase();
        
        // Demonstrate service operations
        LOG.info("\n--- Service Layer Operations ---");
        
        // Count entities
        LOG.log(Level.INFO, "Total books: {0}", bookService.count());
        LOG.log(Level.INFO, "Total borrowers: {0}", borrowerService.count());
        LOG.log(Level.INFO, "Total libraries: {0}", libraryService.count());
        LOG.log(Level.INFO, "Total book loans: {0}", bookLoanService.count());
        
        // Find available books
        LOG.info("\n--- Available Books ---");
        bookService.findAvailableBooks().forEach(book -> {
            LOG.log(Level.INFO, "Available: {0} by {1} (${2})", 
                   new Object[]{book.getTitle(), book.getAuthor(), book.getPrice()});
        });
        
        // Find active borrowers
        LOG.info("\n--- Active Borrowers ---");
        borrowerService.findActiveBorrowers().forEach(borrower -> {
            LOG.log(Level.INFO, "Active: {0} {1} ({2})", 
                   new Object[]{borrower.getFirstName(), borrower.getLastName(), borrower.getEmail()});
        });
        
        // Find overdue loans
        LOG.info("\n--- Overdue Loans ---");
        var overdueLoans = bookLoanService.findOverdueLoans();
        if (overdueLoans.isEmpty()) {
            LOG.info("No overdue loans found");
        } else {
            overdueLoans.forEach(loan -> {
                LOG.log(Level.INFO, "Overdue: \"{0}\" by {1} (Due: {2})", 
                       new Object[]{loan.getBook().getTitle(), loan.getBorrowerName(), loan.getDueDate()});
            });
        }
        
        // Demonstrate library statistics
        LOG.info("\n--- Library Statistics ---");
        libraryService.findAll().forEach(library -> {
            long activeLoans = libraryService.countActiveLoans(library.getId());
            long totalLoans = libraryService.countTotalLoans(library.getId());
            LOG.log(Level.INFO, "{0}: {1} active loans, {2} total loans", 
                   new Object[]{library.getName(), activeLoans, totalLoans});
        });
        
        LOG.info("\n=== SERVICE LAYER DEMONSTRATION COMPLETE ===");
    }
}