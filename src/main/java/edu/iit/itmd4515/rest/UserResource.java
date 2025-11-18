package edu.iit.itmd4515.rest;

import edu.iit.itmd4515.domain.User;
import edu.iit.itmd4515.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST resource for User entity management.
 * Provides CRUD operations for User entities through RESTful web services.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UserResource {
    
    private static final Logger LOG = Logger.getLogger(UserResource.class.getName());
    
    @EJB
    private UserService userService;
    
    /**
     * Get all users.
     * @return List of all users
     */
    @GET
    public Response getAllUsers() {
        LOG.log(Level.INFO, "Getting all users");
        try {
            List<User> users = userService.findAll();
            return Response.ok(users).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting all users", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving users: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get a user by ID.
     * @param id User ID
     * @return User entity
     */
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Getting user by ID: {0}", id);
        try {
            User user = userService.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + id).build();
            }
            return Response.ok(user).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting user by ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving user: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get a user by username.
     * @param username Username
     * @return User entity
     */
    @GET
    @Path("/username/{username}")
    public Response getUserByUsername(@PathParam("username") String username) {
        LOG.log(Level.INFO, "Getting user by username: {0}", username);
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with username: " + username).build();
            }
            return Response.ok(user).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting user by username: " + username, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving user: " + e.getMessage()).build();
        }
    }
    
    /**
     * Create a new user.
     * @param user User entity to create
     * @return Created user entity
     */
    @POST
    public Response createUser(User user) {
        LOG.log(Level.INFO, "Creating new user: {0}", user.getUsername());
        try {
            User createdUser = userService.create(user);
            return Response.status(Response.Status.CREATED).entity(createdUser).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error creating user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating user: " + e.getMessage()).build();
        }
    }
    
    /**
     * Update an existing user.
     * @param id User ID
     * @param user Updated user entity
     * @return Updated user entity
     */
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User user) {
        LOG.log(Level.INFO, "Updating user with ID: {0}", id);
        try {
            User existingUser = userService.findById(id);
            if (existingUser == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + id).build();
            }
            
            // Update user fields except password (for security)
            existingUser.setEmail(user.getEmail());
            existingUser.setIsActive(user.getIsActive());
            
            User updatedUser = userService.update(existingUser);
            return Response.ok(updatedUser).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating user with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating user: " + e.getMessage()).build();
        }
    }
    
    /**
     * Delete a user by ID.
     * @param id User ID
     * @return Response indicating success or failure
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting user with ID: {0}", id);
        try {
            User user = userService.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + id).build();
            }
            
            userService.delete(user);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error deleting user with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting user: " + e.getMessage()).build();
        }
    }
}