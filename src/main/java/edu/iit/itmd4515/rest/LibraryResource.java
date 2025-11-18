package edu.iit.itmd4515.rest;

import edu.iit.itmd4515.domain.Library;
import edu.iit.itmd4515.service.LibraryService;
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
 * REST resource for Library entity operations.
 * Provides CRUD operations for library branch management.
 */
@Path("/libraries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LibraryResource {
    
    private static final Logger LOG = Logger.getLogger(LibraryResource.class.getName());
    
    @EJB
    private LibraryService libraryService;
    
    @Context
    private UriInfo uriInfo;
    
    /**
     * Get all libraries.
     * @return List of all libraries
     */
    @GET
    @RolesAllowed("ADMIN")
    public Response getAllLibraries() {
        LOG.log(Level.INFO, "Getting all libraries");
        try {
            List<Library> libraries = libraryService.findAll();
            return Response.ok(libraries).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting all libraries", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving libraries: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get library by ID.
     * @param id The library ID
     * @return The library with the specified ID
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getLibraryById(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Getting library by ID: {0}", id);
        try {
            Optional<Library> libraryOpt = Optional.ofNullable(libraryService.findById(id));
            
            if (libraryOpt.isPresent()) {
                return Response.ok(libraryOpt.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Library not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting library by ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving library: " + e.getMessage()).build();
        }
    }
    
    /**
     * Create a new library.
     * @param library The library to create
     * @return Response with the created library
     */
    @POST
    @RolesAllowed("ADMIN")
    public Response createLibrary(Library library) {
        LOG.log(Level.INFO, "Creating new library: {0}", library);
        try {
            libraryService.create(library);
            URI createdUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(library.getId())).build();
            return Response.created(createdUri).entity(library).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error creating library", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating library: " + e.getMessage()).build();
        }
    }
    
    /**
     * Update an existing library.
     * @param id The library ID
     * @param library The updated library data
     * @return Response with the updated library
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateLibrary(@PathParam("id") Long id, Library library) {
        LOG.log(Level.INFO, "Updating library with ID: {0}", id);
        try {
            Optional<Library> existingLibraryOpt = Optional.ofNullable(libraryService.findById(id));
            
            if (existingLibraryOpt.isPresent()) {
                library.setId(id); // Ensure the ID matches the path parameter
                libraryService.update(library);
                return Response.ok(library).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Library not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating library with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating library: " + e.getMessage()).build();
        }
    }
    
    /**
     * Delete a library by ID.
     * @param id The library ID
     * @return Response confirming deletion
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteLibrary(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting library with ID: {0}", id);
        try {
            Optional<Library> existingLibraryOpt = Optional.ofNullable(libraryService.findById(id));
            
            if (existingLibraryOpt.isPresent()) {
                libraryService.delete(existingLibraryOpt.get());
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Library not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error deleting library with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting library: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search libraries by name.
     * @param name The name to search for
     * @return List of libraries with matching name
     */
    @GET
    @Path("/search/name/{name}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findByName(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Searching libraries by name: {0}", name);
        try {
            List<Library> libraries = libraryService.findByName(name);
            return Response.ok(libraries).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching libraries by name: " + name, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching libraries: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search libraries by city.
     * @param city The city to search for
     * @return List of libraries in the specified city
     */
    @GET
    @Path("/search/city/{city}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findByCity(@PathParam("city") String city) {
        LOG.log(Level.INFO, "Searching libraries by city: {0}", city);
        try {
            List<Library> libraries = libraryService.findByCity(city);
            return Response.ok(libraries).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching libraries by city: " + city, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching libraries: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search libraries by state.
     * @param state The state to search for
     * @return List of libraries in the specified state
     */
    @GET
    @Path("/search/state/{state}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response findByState(@PathParam("state") String state) {
        LOG.log(Level.INFO, "Searching libraries by state: {0}", state);
        try {
            List<Library> libraries = libraryService.findByState(state);
            return Response.ok(libraries).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching libraries by state: " + state, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching libraries: " + e.getMessage()).build();
        }
    }
}