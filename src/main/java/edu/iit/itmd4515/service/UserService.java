package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for User entity operations.
 * Provides business logic for user management including authentication.
 */
@Stateless
public class UserService extends AbstractService<User> {
    
    private static final Logger LOG = Logger.getLogger(UserService.class.getName());
    
    public UserService() {
        super(User.class);
    }
    
    /**
     * Find a user by username.
     * @param username the username to search for
     * @return the found user or null
     */
    public User findByUsername(String username) {
        LOG.log(Level.INFO, "Finding user by username: {0}", username);
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByUsername", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOG.log(Level.WARNING, "User not found with username: {0}", username);
            return null;
        }
    }
    
    /**
     * Authenticate a user with username and password.
     * @param username the username
     * @param password the password
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        LOG.log(Level.INFO, "Authenticating user: {0}", username);
        User user = findByUsername(username);
        if (user != null && user.getIsActive()) {
            // NOTE: This method is kept for compatibility but should not be used for actual authentication
            // Jakarta EE Security handles authentication through the security framework
            LOG.log(Level.WARNING, "Direct authentication attempted for user: {0}. Use Jakarta EE Security instead.", username);
            return false;
        }
        return false;
    }
    
    /**
     * Check if a user has a specific role.
     * @param username the username
     * @param roleName the role name to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(String username, String roleName) {
        LOG.log(Level.INFO, "Checking if user {0} has role {1}", new Object[]{username, roleName});
        User user = findByUsername(username);
        if (user != null) {
            return user.hasRole(roleName);
        }
        return false;
    }
    
    /**
     * Create a new user with validation.
     * @param user the user to create
     * @return the created user
     * @throws IllegalArgumentException if username already exists
     */
    @Override
    public User create(User user) {
        LOG.log(Level.INFO, "Creating new user: {0}", user.getUsername());
        
        // Check if username already exists
        if (findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        // Set default active status
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        
        return super.create(user);
    }
    
    /**
     * Deactivate a user account.
     * @param username the username to deactivate
     * @return true if successful, false if user not found
     */
    public boolean deactivateUser(String username) {
        LOG.log(Level.INFO, "Deactivating user: {0}", username);
        User user = findByUsername(username);
        if (user != null) {
            user.setIsActive(false);
            update(user);
            return true;
        }
        return false;
    }
    
    /**
     * Activate a user account.
     * @param username the username to activate
     * @return true if successful, false if user not found
     */
    public boolean activateUser(String username) {
        LOG.log(Level.INFO, "Activating user: {0}", username);
        User user = findByUsername(username);
        if (user != null) {
            user.setIsActive(true);
            update(user);
            return true;
        }
        return false;
    }
}