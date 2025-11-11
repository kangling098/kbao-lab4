package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.Librarian;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stateless EJB service for Librarian entity operations.
 * Provides CRUD operations and business logic for Librarian management.
 */
@Stateless
public class LibrarianService extends AbstractService<Librarian> {
    
    private static final Logger LOG = Logger.getLogger(LibrarianService.class.getName());
    
    public LibrarianService() {
        super(Librarian.class);
    }
    
    /**
     * Find librarians by last name.
     * @param lastName the last name to search for
     * @return list of matching librarians
     */
    public List<Librarian> findByLastName(String lastName) {
        LOG.log(Level.INFO, "Finding librarians by last name: {0}", lastName);
        TypedQuery<Librarian> query = em.createQuery("SELECT l FROM Librarian l WHERE l.lastName LIKE :lastName", Librarian.class);
        query.setParameter("lastName", "%" + lastName + "%");
        return query.getResultList();
    }
    
    /**
     * Find librarians by employee ID.
     * @param employeeId the employee ID to search for
     * @return the librarian or null
     */
    public Librarian findByEmployeeId(String employeeId) {
        LOG.log(Level.INFO, "Finding librarian by employee ID: {0}", employeeId);
        TypedQuery<Librarian> query = em.createQuery("SELECT l FROM Librarian l WHERE l.employeeId = :employeeId", Librarian.class);
        query.setParameter("employeeId", employeeId);
        List<Librarian> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Find active librarians.
     * @return list of active librarians
     */
    public List<Librarian> findActiveLibrarians() {
        LOG.log(Level.INFO, "Finding active librarians");
        TypedQuery<Librarian> query = em.createQuery("SELECT l FROM Librarian l WHERE l.employed = true", Librarian.class);
        return query.getResultList();
    }
    
    /**
     * Find librarians by library.
     * @param libraryId the library ID
     * @return list of librarians at the library
     */
    public List<Librarian> findByLibrary(Long libraryId) {
        LOG.log(Level.INFO, "Finding librarians by library ID: {0}", libraryId);
        TypedQuery<Librarian> query = em.createQuery("SELECT l FROM Librarian l WHERE l.library.id = :libraryId", Librarian.class);
        query.setParameter("libraryId", libraryId);
        return query.getResultList();
    }
    
    /**
     * Count processed loans for a librarian.
     * @param librarianId the librarian ID
     * @return number of processed loans
     */
    public long countProcessedLoans(Long librarianId) {
        LOG.log(Level.INFO, "Counting processed loans for librarian ID: {0}", librarianId);
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(bl) FROM BookLoan bl WHERE bl.librarian.id = :librarianId", Long.class);
        query.setParameter("librarianId", librarianId);
        return query.getSingleResult();
    }
}