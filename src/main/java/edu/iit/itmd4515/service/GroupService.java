package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.Group;
import edu.iit.itmd4515.domain.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for Group entity operations.
 * Provides business logic for group/role management.
 */
@Stateless
public class GroupService extends AbstractService<Group> {
    
    private static final Logger LOG = Logger.getLogger(GroupService.class.getName());
    
    public GroupService() {
        super(Group.class);
    }
    
    /**
     * Find a group by group name.
     * @param groupName the group name to search for
     * @return the found group or null
     */
    public Group findByGroupName(String groupName) {
        LOG.log(Level.INFO, "Finding group by name: {0}", groupName);
        try {
            TypedQuery<Group> query = em.createNamedQuery("Group.findByGroupName", Group.class);
            query.setParameter("groupName", groupName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOG.log(Level.WARNING, "Group not found with name: {0}", groupName);
            return null;
        }
    }
    
    /**
     * Create a new group with validation.
     * @param group the group to create
     * @return the created group
     * @throws IllegalArgumentException if group name already exists
     */
    @Override
    public Group create(Group group) {
        LOG.log(Level.INFO, "Creating new group: {0}", group.getGroupName());
        
        // Check if group name already exists
        if (findByGroupName(group.getGroupName()) != null) {
            throw new IllegalArgumentException("Group name already exists: " + group.getGroupName());
        }
        
        return super.create(group);
    }
    
    /**
     * Add a user to a group.
     * @param groupName the group name
     * @param user the user to add
     * @return true if successful, false if group not found
     */
    public boolean addUserToGroup(String groupName, User user) {
        LOG.log(Level.INFO, "Adding user {0} to group {1}", new Object[]{user.getUsername(), groupName});
        Group group = findByGroupName(groupName);
        if (group != null) {
            group.addUser(user);
            update(group);
            return true;
        }
        return false;
    }
    
    /**
     * Remove a user from a group.
     * @param groupName the group name
     * @param user the user to remove
     * @return true if successful, false if group not found
     */
    public boolean removeUserFromGroup(String groupName, User user) {
        LOG.log(Level.INFO, "Removing user {0} from group {1}", new Object[]{user.getUsername(), groupName});
        Group group = findByGroupName(groupName);
        if (group != null) {
            group.removeUser(user);
            update(group);
            return true;
        }
        return false;
    }
    
    /**
     * Check if a group exists.
     * @param groupName the group name to check
     * @return true if exists, false otherwise
     */
    public boolean groupExists(String groupName) {
        return findByGroupName(groupName) != null;
    }
    
    /**
     * Create default security groups if they don't exist.
     * This method should be called during application initialization.
     */
    public void createDefaultGroups() {
        LOG.log(Level.INFO, "Creating default security groups");
        
        // Create ADMIN group
        if (!groupExists("ADMIN")) {
            Group adminGroup = new Group("ADMIN", "System Administrators with full access");
            create(adminGroup);
            LOG.log(Level.INFO, "Created ADMIN group");
        }
        
        // Create LIBRARIAN group
        if (!groupExists("LIBRARIAN")) {
            Group librarianGroup = new Group("LIBRARIAN", "Librarians with book management access");
            create(librarianGroup);
            LOG.log(Level.INFO, "Created LIBRARIAN group");
        }
        
        // Create USER group
        if (!groupExists("USER")) {
            Group userGroup = new Group("USER", "Regular users with borrowing privileges");
            create(userGroup);
            LOG.log(Level.INFO, "Created USER group");
        }
    }
}