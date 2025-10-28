package edu.iit.itmd4515;

import jakarta.persistence.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple database connection test for Lab7
 */
public class DatabaseConnectionTest {
    
    private static final Logger LOG = Logger.getLogger(DatabaseConnectionTest.class.getName());
    
    public static void main(String[] args) {
        LOG.info("=== Lab7 Database Connection Test ===");
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            // Test persistence unit creation
            LOG.info("Creating EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory("itmd4515StandalonePU");
            LOG.info("✓ EntityManagerFactory created successfully");
            
            // Test EntityManager creation
            LOG.info("Creating EntityManager...");
            em = emf.createEntityManager();
            LOG.info("✓ EntityManager created successfully");
            
            // Test database connection
            LOG.info("Testing database connection...");
            em.getTransaction().begin();
            Query query = em.createNativeQuery("SELECT 1");
            Object result = query.getSingleResult();
            em.getTransaction().commit();
            LOG.info("✓ Database connection test successful: " + result);
            
            // Test entity operations
            LOG.info("Testing entity operations...");
            Long bookCount = em.createQuery("SELECT COUNT(b) FROM Book b", Long.class).getSingleResult();
            LOG.info("✓ Book count: " + bookCount);
            
            LOG.info("=== All Database Tests Passed! ===");
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Database test failed", e);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
}