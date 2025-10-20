package edu.iit.itmd4515.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base service class providing common CRUD operations.
 * This class follows the template method pattern to provide standard
 * database operations for all entities.
 */
public abstract class AbstractService<T> {
    
    private static final Logger LOG = Logger.getLogger(AbstractService.class.getName());
    
    @PersistenceContext
    public EntityManager em;
    
    private final Class<T> entityClass;
    
    public AbstractService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    /**
     * Create (persist) a new entity.
     * @param entity the entity to create
     * @return the created entity
     */
    @Transactional
    public T create(T entity) {
        LOG.log(Level.INFO, "Creating entity: {0}", entity);
        em.persist(entity);
        return entity;
    }
    
    /**
     * Find an entity by its ID.
     * @param id the entity ID
     * @return the found entity or null
     */
    public T findById(Long id) {
        LOG.log(Level.INFO, "Finding entity by ID: {0}", id);
        return em.find(entityClass, id);
    }
    
    /**
     * Find all entities of this type.
     * @return list of all entities
     */
    public List<T> findAll() {
        LOG.log(Level.INFO, "Finding all entities of type: {0}", entityClass.getSimpleName());
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }
    
    /**
     * Update an existing entity.
     * @param entity the entity to update
     * @return the updated entity
     */
    @Transactional
    public T update(T entity) {
        LOG.log(Level.INFO, "Updating entity: {0}", entity);
        return em.merge(entity);
    }
    
    /**
     * Delete an entity.
     * @param entity the entity to delete
     */
    @Transactional
    public void delete(T entity) {
        LOG.log(Level.INFO, "Deleting entity: {0}", entity);
        em.remove(em.merge(entity));
    }
    
    /**
     * Count all entities of this type.
     * @return the count
     */
    public long count() {
        LOG.log(Level.INFO, "Counting entities of type: {0}", entityClass.getSimpleName());
        return em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }
}