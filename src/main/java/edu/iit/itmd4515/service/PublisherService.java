package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.Publisher;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stateless EJB service for Publisher entity operations.
 * Provides CRUD operations and business logic for Publisher management.
 */
@Stateless
public class PublisherService extends AbstractService<Publisher> {
    
    private static final Logger LOG = Logger.getLogger(PublisherService.class.getName());
    
    public PublisherService() {
        super(Publisher.class);
    }
    
    /**
     * Find publishers by name.
     * @param name the name to search for
     * @return list of matching publishers
     */
    public List<Publisher> findByName(String name) {
        LOG.log(Level.INFO, "Finding publishers by name: {0}", name);
        TypedQuery<Publisher> query = em.createQuery("SELECT p FROM Publisher p WHERE p.name LIKE :name", Publisher.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
    
    /**
     * Find publishers by city.
     * @param city the city to search for
     * @return list of matching publishers
     */
    public List<Publisher> findByCity(String city) {
        LOG.log(Level.INFO, "Finding publishers by city: {0}", city);
        TypedQuery<Publisher> query = em.createQuery("SELECT p FROM Publisher p WHERE p.city LIKE :city", Publisher.class);
        query.setParameter("city", "%" + city + "%");
        return query.getResultList();
    }
    
    /**
     * Find publishers by country.
     * @param country the country to search for
     * @return list of matching publishers
     */
    public List<Publisher> findByCountry(String country) {
        LOG.log(Level.INFO, "Finding publishers by country: {0}", country);
        TypedQuery<Publisher> query = em.createQuery("SELECT p FROM Publisher p WHERE p.country = :country", Publisher.class);
        query.setParameter("country", country);
        return query.getResultList();
    }
}