package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.Borrower;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stateless EJB service for Borrower entity operations.
 * Provides CRUD operations and business logic for Borrower management.
 */
@Stateless
public class BorrowerService extends AbstractService<Borrower> {
    
    private static final Logger LOG = Logger.getLogger(BorrowerService.class.getName());
    
    public BorrowerService() {
        super(Borrower.class);
    }
    
    /**
     * Find borrowers by last name.
     * @param lastName the last name to search for
     * @return list of matching borrowers
     */
    public List<Borrower> findByLastName(String lastName) {
        LOG.log(Level.INFO, "Finding borrowers by last name: {0}", lastName);
        TypedQuery<Borrower> query = em.createQuery("SELECT b FROM Borrower b WHERE b.lastName LIKE :lastName", Borrower.class);
        query.setParameter("lastName", "%" + lastName + "%");
        return query.getResultList();
    }
    
    /**
     * Find borrowers by email.
     * @param email the email to search for
     * @return the borrower or null
     */
    public Borrower findByEmail(String email) {
        LOG.log(Level.INFO, "Finding borrower by email: {0}", email);
        TypedQuery<Borrower> query = em.createQuery("SELECT b FROM Borrower b WHERE b.email = :email", Borrower.class);
        query.setParameter("email", email);
        List<Borrower> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Find active borrowers.
     * @return list of active borrowers
     */
    public List<Borrower> findActiveBorrowers() {
        LOG.log(Level.INFO, "Finding active borrowers");
        TypedQuery<Borrower> query = em.createQuery("SELECT b FROM Borrower b WHERE b.membershipActive = true", Borrower.class);
        return query.getResultList();
    }
    
    /**
     * Find borrowers with overdue books.
     * @return list of borrowers with overdue books
     */
    public List<Borrower> findBorrowersWithOverdueBooks() {
        LOG.log(Level.INFO, "Finding borrowers with overdue books");
        TypedQuery<Borrower> query = em.createQuery(
            "SELECT DISTINCT b FROM Borrower b JOIN b.bookLoans bl WHERE bl.returnDate IS NULL AND bl.dueDate < CURRENT_DATE", Borrower.class);
        return query.getResultList();
    }
    
    /**
     * Count active loans for a borrower.
     * @param borrowerId the borrower ID
     * @return number of active loans
     */
    public long countActiveLoans(Long borrowerId) {
        LOG.log(Level.INFO, "Counting active loans for borrower ID: {0}", borrowerId);
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(bl) FROM BookLoan bl WHERE bl.borrower.id = :borrowerId AND bl.returnDate IS NULL", Long.class);
        query.setParameter("borrowerId", borrowerId);
        return query.getSingleResult();
    }
}