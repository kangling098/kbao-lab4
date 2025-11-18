package edu.iit.itmd4515.rest;

import edu.iit.itmd4515.domain.Book;
import edu.iit.itmd4515.service.BookService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST resource for Book entity management.
 * Provides CRUD operations for Book entities through RESTful web services.
 */
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    
    private static final Logger LOG = Logger.getLogger(BookResource.class.getName());
    
    @EJB
    private BookService bookService;
    
    /**
     * Get all books.
     * @return List of all books
     */
    @GET
    @PermitAll
    public Response getAllBooks() {
        LOG.log(Level.INFO, "Getting all books");
        try {
            List<Book> books = bookService.findAll();
            return Response.ok(books).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting all books", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving books: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get a book by ID.
     * @param id Book ID
     * @return Book entity
     */
    @GET
    @Path("/{id}")
    @PermitAll
    public Response getBookById(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Getting book by ID: {0}", id);
        try {
            Book book = bookService.findById(id);
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found with ID: " + id).build();
            }
            return Response.ok(book).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting book by ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving book: " + e.getMessage()).build();
        }
    }
    
    /**
     * Create a new book.
     * @param book Book entity to create
     * @return Created book entity
     */
    @POST
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response createBook(Book book) {
        LOG.log(Level.INFO, "Creating new book: {0}", book);
        try {
            // Set availability to true by default
            book.setIsAvailable(true);
            
            Book createdBook = bookService.create(book);
            return Response.status(Response.Status.CREATED).entity(createdBook).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error creating book", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating book: " + e.getMessage()).build();
        }
    }
    
    /**
     * Update an existing book.
     * @param id Book ID
     * @param book Updated book entity
     * @return Updated book entity
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response updateBook(@PathParam("id") Long id, Book book) {
        LOG.log(Level.INFO, "Updating book with ID: {0}", id);
        try {
            Book existingBook = bookService.findById(id);
            if (existingBook == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found with ID: " + id).build();
            }
            
            // Update book fields
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setPublicationDate(book.getPublicationDate());
            existingBook.setPageCount(book.getPageCount());
            existingBook.setPrice(book.getPrice());
            existingBook.setIsAvailable(book.getIsAvailable());
            
            Book updatedBook = bookService.update(existingBook);
            return Response.ok(updatedBook).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating book with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating book: " + e.getMessage()).build();
        }
    }
    
    /**
     * Delete a book by ID.
     * @param id Book ID
     * @return Response indicating success or failure
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response deleteBook(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting book with ID: {0}", id);
        try {
            Book book = bookService.findById(id);
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found with ID: " + id).build();
            }
            
            bookService.delete(book);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error deleting book with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting book: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search books by title.
     * @param title Title to search for
     * @return List of matching books
     */
    @GET
    @Path("/search/title/{title}")
    @PermitAll
    public Response searchBooksByTitle(@PathParam("title") String title) {
        LOG.log(Level.INFO, "Searching books by title: {0}", title);
        try {
            List<Book> books = bookService.findByTitle(title);
            return Response.ok(books).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching books by title: " + title, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching books: " + e.getMessage()).build();
        }
    }
    
    /**
     * Search books by author.
     * @param author Author name to search for
     * @return List of matching books
     */
    @GET
    @Path("/search/author/{author}")
    @PermitAll
    public Response searchBooksByAuthor(@PathParam("author") String author) {
        LOG.log(Level.INFO, "Searching books by author: {0}", author);
        try {
            List<Book> books = bookService.findByAuthor(author);
            return Response.ok(books).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error searching books by author: " + author, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching books: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get available books.
     * @return List of available books
     */
    @GET
    @Path("/available")
    @PermitAll
    public Response getAvailableBooks() {
        LOG.log(Level.INFO, "Getting available books");
        try {
            List<Book> books = bookService.findAvailableBooks();
            return Response.ok(books).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting available books", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving available books: " + e.getMessage()).build();
        }
    }
}