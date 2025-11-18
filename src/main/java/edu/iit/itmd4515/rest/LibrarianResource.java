package edu.iit.itmd4515.rest;

import edu.iit.itmd4515.domain.Librarian;
import edu.iit.itmd4515.service.LibrarianService;
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
 * REST resource for Librarian entity operations.
 * Provides CRUD operations for librarian management.
 */
@Path("/librarians")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LibrarianResource {
    
    private static final Logger LOG = Logger.getLogger(LibrarianResource.class.getName());
    
    @EJB
    private LibrarianService librarianService;
    
    @Context
    private UriInfo uriInfo;
    
    /**
     * Get all librarians.
     * @return List of all librarians
     */
    @GET
    @RolesAllowed("ADMIN")
    public Response getAllLibrarians() {
        LOG.log(Level.INFO, "Getting all librarians");
        try {
            List<Librarian> librarians = librarianService.findAll();
            return Response.ok(librarians).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting all librarians", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving librarians: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get librarian by ID.
     * @param id The librarian ID
     * @return The librarian with the specified ID
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getLibrarianById(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Getting librarian by ID: {0}", id);
        try {
            Optional<Librarian> librarianOpt = Optional.ofNullable(librarianService.findById(id));
            
            if (librarianOpt.isPresent()) {
                return Response.ok(librarianOpt.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Librarian not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting librarian by ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving librarian: " + e.getMessage()).build();
        }
    }
    
    /**
     * Create a new librarian.
     * @param librarian The librarian to create
     * @return Response with the created librarian
     */
    @POST
    @RolesAllowed("ADMIN")
    public Response createLibrarian(Librarian librarian) {
        LOG.log(Level.INFO, "Creating new librarian: {0}", librarian);
        try {
            librarianService.create(librarian);
            URI createdUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(librarian.getId())).build();
            return Response.created(createdUri).entity(librarian).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error creating librarian", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating librarian: " + e.getMessage()).build();
        }
    }
    
    /**
     * Update an existing librarian.
     * @param id The librarian ID
     * @param librarian The updated librarian data
     * @return Response with the updated librarian
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateLibrarian(@PathParam("id") Long id, Librarian librarian) {
        LOG.log(Level.INFO, "Updating librarian with ID: {0}", id);
        try {
            Optional<Librarian> existingLibrarianOpt = Optional.ofNullable(librarianService.findById(id));
            
            if (existingLibrarianOpt.isPresent()) {
                librarian.setId(id); // Ensure the ID matches the path parameter
                librarianService.update(librarian);
                return Response.ok(librarian).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Librarian not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating librarian with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating librarian: " + e.getMessage()).build();
        }
    }
    
    /**
     * Delete a librarian by ID.
     * @param id The librarian ID
     * @return Response confirming deletion
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteLibrarian(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting librarian with ID: {0}", id);
        try {
            Optional<Librarian> existingLibrarianOpt = Optional.ofNullable(librarianService.findById(id));
            
            if (existingLibrarianOpt.isPresent()) {
                librarianService.delete(existingLibrarianOpt.get());
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Librarian not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error deleting librarian with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting librarian: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search librarians by last name.
     * @param lastName The last name to search for
     * @return List of librarians with matching last name
     */
    @GET
    @Path("/search/lastName/{lastName}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findByLastName(@PathParam("lastName") String lastName) {
        LOG.log(Level.INFO, "Searching librarians by last name: {0}", lastName);
        try {
            List<Librarian> librarians = librarianService.findByLastName(lastName);
            return Response.ok(librarians).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching librarians by last name: " + lastName, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching librarians: " + e.getMessage()).build();
        }
    }
    
    /**
     * Find librarian by employee ID.
     * @param employeeId The employee ID to search for
     * @return The librarian with the specified employee ID
     */
    @GET
    @Path("/search/employeeId/{employeeId}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findByEmployeeId(@PathParam("employeeId") String employeeId) {
        LOG.log(Level.INFO, "Searching librarian by employee ID: {0}", employeeId);
        try {
            Librarian librarian = librarianService.findByEmployeeId(employeeId);
            if (librarian != null) {
                return Response.ok(librarian).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Librarian not found with employee ID: " + employeeId).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching librarian by employee ID: " + employeeId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching librarian: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get active librarians.
     * @return List of active librarians
     */
    @GET
    @Path("/active")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findActiveLibrarians() {
        LOG.log(Level.INFO, "Getting active librarians");
        try {
            List<Librarian> librarians = librarianService.findActiveLibrarians();
            return Response.ok(librarians).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting active librarians", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving active librarians: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get librarians by library ID.
     * @param libraryId The library ID
     * @return List of librarians at the library
     */
    @GET
    @Path("/library/{libraryId}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findByLibrary(@PathParam("libraryId") Long libraryId) {
        LOG.log(Level.INFO, "Searching librarians by library ID: {0}", libraryId);
        try {
            List<Librarian> librarians = librarianService.findByLibrary(libraryId);
            return Response.ok(librarians).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching librarians by library ID: " + libraryId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching librarians: " + e.getMessage()).build();
        }
    }
}