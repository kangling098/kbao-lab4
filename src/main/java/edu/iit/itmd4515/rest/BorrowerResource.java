package edu.iit.itmd4515.rest;

import edu.iit.itmd4515.domain.Borrower;
import edu.iit.itmd4515.service.BorrowerService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST resource for Borrower entity operations.
 * Provides CRUD operations for borrower management.
 */
@Path("/borrowers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BorrowerResource {
    
    private static final Logger LOG = Logger.getLogger(BorrowerResource.class.getName());
    
    @EJB
    private BorrowerService borrowerService;
    
    @Context
    private UriInfo uriInfo;
    
    /**
     * Get all borrowers.
     * @return List of all borrowers
     */
    @GET
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getAllBorrowers() {
        LOG.log(Level.INFO, "Getting all borrowers");
        try {
            List<Borrower> borrowers = borrowerService.findAll();
            return Response.ok(borrowers).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting all borrowers", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving borrowers: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get borrower by ID.
     * @param id The borrower ID
     * @return The borrower with the specified ID
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getBorrowerById(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Getting borrower by ID: {0}", id);
        try {
            Optional<Borrower> borrowerOpt = Optional.ofNullable(borrowerService.findById(id));
            
            if (borrowerOpt.isPresent()) {
                return Response.ok(borrowerOpt.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Borrower not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting borrower by ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving borrower: " + e.getMessage()).build();
        }
    }
    
    /**
     * Create a new borrower.
     * @param borrower The borrower to create
     * @return Response with the created borrower
     */
    @POST
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response createBorrower(Borrower borrower) {
        LOG.log(Level.INFO, "Creating new borrower: {0}", borrower);
        try {
            borrowerService.create(borrower);
            URI createdUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(borrower.getId())).build();
            return Response.created(createdUri).entity(borrower).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error creating borrower", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating borrower: " + e.getMessage()).build();
        }
    }
    
    /**
     * Update an existing borrower.
     * @param id The borrower ID
     * @param borrower The updated borrower data
     * @return Response with the updated borrower
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response updateBorrower(@PathParam("id") Long id, Borrower borrower) {
        LOG.log(Level.INFO, "Updating borrower with ID: {0}", id);
        try {
            Optional<Borrower> existingBorrowerOpt = Optional.ofNullable(borrowerService.findById(id));
            
            if (existingBorrowerOpt.isPresent()) {
                borrower.setId(id); // Ensure the ID matches the path parameter
                borrowerService.update(borrower);
                return Response.ok(borrower).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Borrower not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating borrower with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating borrower: " + e.getMessage()).build();
        }
    }
    
    /**
     * Delete a borrower by ID.
     * @param id The borrower ID
     * @return Response confirming deletion
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response deleteBorrower(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting borrower with ID: {0}", id);
        try {
            Optional<Borrower> existingBorrowerOpt = Optional.ofNullable(borrowerService.findById(id));
            
            if (existingBorrowerOpt.isPresent()) {
                borrowerService.delete(existingBorrowerOpt.get());
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Borrower not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error deleting borrower with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting borrower: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search borrowers by last name.
     * @param lastName The last name to search for
     * @return List of borrowers with matching last name
     */
    @GET
    @Path("/search/lastName/{lastName}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findByLastName(@PathParam("lastName") String lastName) {
        LOG.log(Level.INFO, "Searching borrowers by last name: {0}", lastName);
        try {
            List<Borrower> borrowers = borrowerService.findByLastName(lastName);
            return Response.ok(borrowers).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching borrowers by last name: " + lastName, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching borrowers: " + e.getMessage()).build();
        }
    }
    
    /**
     * Find borrower by email.
     * @param email The email to search for
     * @return The borrower with the specified email
     */
    @GET
    @Path("/search/email/{email}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findByEmail(@PathParam("email") String email) {
        LOG.log(Level.INFO, "Searching borrower by email: {0}", email);
        try {
            Borrower borrower = borrowerService.findByEmail(email);
            if (borrower != null) {
                return Response.ok(borrower).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Borrower not found with email: " + email).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching borrower by email: " + email, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching borrower: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get active borrowers.
     * @return List of active borrowers
     */
    @GET
    @Path("/active")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findActiveBorrowers() {
        LOG.log(Level.INFO, "Getting active borrowers");
        try {
            List<Borrower> borrowers = borrowerService.findActiveBorrowers();
            return Response.ok(borrowers).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting active borrowers", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving active borrowers: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get borrowers with overdue books.
     * @return List of borrowers with overdue books
     */
    @GET
    @Path("/overdue")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findBorrowersWithOverdueBooks() {
        LOG.log(Level.INFO, "Getting borrowers with overdue books");
        try {
            List<Borrower> borrowers = borrowerService.findBorrowersWithOverdueBooks();
            return Response.ok(borrowers).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting borrowers with overdue books", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving borrowers with overdue books: " + e.getMessage()).build();
        }
    }
}