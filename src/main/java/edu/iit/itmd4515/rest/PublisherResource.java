package edu.iit.itmd4515.rest;

import edu.iit.itmd4515.domain.Publisher;
import edu.iit.itmd4515.service.PublisherService;
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
 * REST resource for Publisher entity operations.
 * Provides CRUD operations for publisher management.
 */
@Path("/publishers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PublisherResource {
    
    private static final Logger LOG = Logger.getLogger(PublisherResource.class.getName());
    
    @EJB
    private PublisherService publisherService;
    
    @Context
    private UriInfo uriInfo;
    
    /**
     * Get all publishers.
     * @return List of all publishers
     */
    @GET
    @PermitAll
    public Response getAllPublishers() {
        LOG.log(Level.INFO, "Getting all publishers");
        try {
            List<Publisher> publishers = publisherService.findAll();
            return Response.ok(publishers).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting all publishers", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving publishers: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get publisher by ID.
     * @param id The publisher ID
     * @return The publisher with the specified ID
     */
    @GET
    @Path("/{id}")
    @PermitAll
    public Response getPublisherById(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Getting publisher by ID: {0}", id);
        try {
            Optional<Publisher> publisherOpt = Optional.ofNullable(publisherService.findById(id));
            
            if (publisherOpt.isPresent()) {
                return Response.ok(publisherOpt.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Publisher not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting publisher by ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving publisher: " + e.getMessage()).build();
        }
    }
    
    /**
     * Create a new publisher.
     * @param publisher The publisher to create
     * @return Response with the created publisher
     */
    @POST
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response createPublisher(Publisher publisher) {
        LOG.log(Level.INFO, "Creating new publisher: {0}", publisher);
        try {
            publisherService.create(publisher);
            URI createdUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(publisher.getId())).build();
            return Response.created(createdUri).entity(publisher).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error creating publisher", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating publisher: " + e.getMessage()).build();
        }
    }
    
    /**
     * Update an existing publisher.
     * @param id The publisher ID
     * @param publisher The updated publisher data
     * @return Response with the updated publisher
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response updatePublisher(@PathParam("id") Long id, Publisher publisher) {
        LOG.log(Level.INFO, "Updating publisher with ID: {0}", id);
        try {
            Optional<Publisher> existingPublisherOpt = Optional.ofNullable(publisherService.findById(id));
            
            if (existingPublisherOpt.isPresent()) {
                publisher.setId(id); // Ensure the ID matches the path parameter
                publisherService.update(publisher);
                return Response.ok(publisher).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Publisher not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating publisher with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating publisher: " + e.getMessage()).build();
        }
    }
    
    /**
     * Delete a publisher by ID.
     * @param id The publisher ID
     * @return Response confirming deletion
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response deletePublisher(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting publisher with ID: {0}", id);
        try {
            Optional<Publisher> existingPublisherOpt = Optional.ofNullable(publisherService.findById(id));
            
            if (existingPublisherOpt.isPresent()) {
                publisherService.delete(existingPublisherOpt.get());
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Publisher not found with ID: " + id).build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error deleting publisher with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting publisher: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search publishers by name.
     * @param name The name to search for
     * @return List of publishers with matching name
     */
    @GET
    @Path("/search/name/{name}")
    @PermitAll
    public Response findByName(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Searching publishers by name: {0}", name);
        try {
            List<Publisher> publishers = publisherService.findByName(name);
            return Response.ok(publishers).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching publishers by name: " + name, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching publishers: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search publishers by city.
     * @param city The city to search for
     * @return List of publishers in the specified city
     */
    @GET
    @Path("/search/city/{city}")
    @PermitAll
    public Response findByCity(@PathParam("city") String city) {
        LOG.log(Level.INFO, "Searching publishers by city: {0}", city);
        try {
            List<Publisher> publishers = publisherService.findByCity(city);
            return Response.ok(publishers).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching publishers by city: " + city, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching publishers: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search publishers by country.
     * @param country The country to search for
     * @return List of publishers in the specified country
     */
    @GET
    @Path("/search/country/{country}")
    @PermitAll
    public Response findByCountry(@PathParam("country") String country) {
        LOG.log(Level.INFO, "Searching publishers by country: {0}", country);
        try {
            List<Publisher> publishers = publisherService.findByCountry(country);
            return Response.ok(publishers).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching publishers by country: " + country, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching publishers: " + e.getMessage()).build();
        }
    }
}