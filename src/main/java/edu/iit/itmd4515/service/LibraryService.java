package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.Library;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stateless EJB service for Library entity operations.
 * Provides CRUD operations and business logic for Library management.
 */
@Stateless
public class LibraryService extends AbstractService<Library> {
    
    private static final Logger LOG = Logger.getLogger(LibraryService.class.getName());
    
    public LibraryService() {
        super(Library.class);
    }
    
    /**
     * Find libraries by name.
     * @param name the name to search for
     * @return list of matching libraries
     */
    public List<Library> findByName(String name) {
        LOG.log(Level.INFO, "Finding libraries by name: {0}", name);
        TypedQuery<Library> query = em.createQuery("SELECT l FROM Library l WHERE l.name LIKE :name", Library.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
    
    /**
     * Find libraries by city.
     * @param city the city to search for
     * @return list of matching libraries
     */
    public List<Library> findByCity(String city) {
        LOG.log(Level.INFO, "Finding libraries by city: {0}", city);
        TypedQuery<Library> query = em.createQuery("SELECT l FROM Library l WHERE l.city LIKE :city", Library.class);
        query.setParameter("city", "%" + city + "%");
        return query.getResultList();
    }
    
    /**
     * Find libraries by state.
     * @param state the state to search for
     * @return list of matching libraries
     */
    public List<Library> findByState(String state) {
        LOG.log(Level.INFO, "Finding libraries by state: {0}", state);
        TypedQuery<Library> query = em.createQuery("SELECT l FROM Library l WHERE l.state = :state", Library.class);
        query.setParameter("state", state);
        return query.getResultList();
    }
    
    /**
     * Count active loans for a library.
     * @param libraryId the library ID
     * @return number of active loans
     */
    public long countActiveLoans(Long libraryId) {
        LOG.log(Level.INFO, "Counting active loans for library ID: {0}", libraryId);
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(bl) FROM BookLoan bl WHERE bl.library.id = :libraryId AND bl.returnDate IS NULL", Long.class);
        query.setParameter("libraryId", libraryId);
        return query.getSingleResult();
    }
    
    /**
     * Count total loans for a library.
     * @param libraryId the library ID
     * @return total number of loans
     */
    public long countTotalLoans(Long libraryId) {
        LOG.log(Level.INFO, "Counting total loans for library ID: {0}", libraryId);
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(bl) FROM BookLoan bl WHERE bl.library.id = :libraryId", Long.class);
        query.setParameter("libraryId", libraryId);
        return query.getSingleResult();
    }
}