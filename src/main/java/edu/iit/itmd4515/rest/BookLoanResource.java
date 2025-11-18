package edu.iit.itmd4515.rest;

import edu.iit.itmd4515.domain.BookLoan;
import edu.iit.itmd4515.service.BookLoanService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST resource for BookLoan entity management.
 * Provides CRUD operations for BookLoan entities through RESTful web services.
 */
@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookLoanResource {
    
    private static final Logger LOG = Logger.getLogger(BookLoanResource.class.getName());
    
    @EJB
    private BookLoanService bookLoanService;
    
    /**
     * Get all book loans.
     * @return List of all book loans
     */
    @GET
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getAllBookLoans() {
        LOG.log(Level.INFO, "Getting all book loans");
        try {
            List<BookLoan> loans = bookLoanService.findAll();
            return Response.ok(loans).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting all book loans", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving book loans: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get a book loan by ID.
     * @param id Book loan ID
     * @return Book loan entity
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getBookLoanById(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Getting book loan by ID: {0}", id);
        try {
            BookLoan loan = bookLoanService.findById(id);
            if (loan == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book loan not found with ID: " + id).build();
            }
            return Response.ok(loan).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting book loan by ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving book loan: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get active book loans.
     * @return List of active book loans
     */
    @GET
    @Path("/active")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getActiveBookLoans() {
        LOG.log(Level.INFO, "Getting active book loans");
        try {
            List<BookLoan> loans = bookLoanService.findActiveLoans();
            return Response.ok(loans).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting active book loans", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving active book loans: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get overdue book loans.
     * @return List of overdue book loans
     */
    @GET
    @Path("/overdue")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getOverdueBookLoans() {
        LOG.log(Level.INFO, "Getting overdue book loans");
        try {
            List<BookLoan> loans = bookLoanService.findOverdueLoans();
            return Response.ok(loans).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting overdue book loans", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving overdue book loans: " + e.getMessage()).build();
        }
    }
    
    /**
     * Create a new book loan.
     * @param loan Book loan entity to create
     * @return Created book loan entity
     */
    @POST
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response createBookLoan(BookLoan loan) {
        LOG.log(Level.INFO, "Creating new book loan");
        try {
            BookLoan createdLoan = bookLoanService.create(loan);
            return Response.status(Response.Status.CREATED).entity(createdLoan).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error creating book loan", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating book loan: " + e.getMessage()).build();
        }
    }
    
    /**
     * Update an existing book loan.
     * @param id Book loan ID
     * @param loan Updated book loan entity
     * @return Updated book loan entity
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response updateBookLoan(@PathParam("id") Long id, BookLoan loan) {
        LOG.log(Level.INFO, "Updating book loan with ID: {0}", id);
        try {
            BookLoan existingLoan = bookLoanService.findById(id);
            if (existingLoan == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book loan not found with ID: " + id).build();
            }
            
            // Update loan fields
            existingLoan.setLoanDate(loan.getLoanDate());
            existingLoan.setDueDate(loan.getDueDate());
            existingLoan.setReturnDate(loan.getReturnDate());
            existingLoan.setBorrowerName(loan.getBorrowerName());
            existingLoan.setBorrowerEmail(loan.getBorrowerEmail());
            existingLoan.setBorrowerPhone(loan.getBorrowerPhone());
            existingLoan.setFineAmount(loan.getFineAmount());
            existingLoan.setNotes(loan.getNotes());
            
            BookLoan updatedLoan = bookLoanService.update(existingLoan);
            return Response.ok(updatedLoan).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating book loan with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating book loan: " + e.getMessage()).build();
        }
    }
    
    /**
     * Return a book (update return date).
     * @param id Book loan ID
     * @return Updated book loan entity
     */
    @PUT
    @Path("/{id}/return")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response returnBook(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Returning book with loan ID: {0}", id);
        try {
            BookLoan loan = bookLoanService.findById(id);
            if (loan == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book loan not found with ID: " + id).build();
            }
            
            // Process book return
            loan.returnBook();
            BookLoan updatedLoan = bookLoanService.update(loan);
            return Response.ok(updatedLoan).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error returning book with loan ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error returning book: " + e.getMessage()).build();
        }
    }
    
    /**
     * Delete a book loan by ID.
     * @param id Book loan ID
     * @return Response indicating success or failure
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response deleteBookLoan(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting book loan with ID: {0}", id);
        try {
            BookLoan loan = bookLoanService.findById(id);
            if (loan == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Book loan not found with ID: " + id).build();
            }
            
            bookLoanService.delete(loan);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error deleting book loan with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting book loan: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get loans by borrower ID.
     * @param borrowerId Borrower ID
     * @return List of loans for the borrower
     */
    @GET
    @Path("/borrower/{borrowerId}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getLoansByBorrower(@PathParam("borrowerId") Long borrowerId) {
        LOG.log(Level.INFO, "Getting loans by borrower ID: {0}", borrowerId);
        try {
            List<BookLoan> loans = bookLoanService.findByBorrower(borrowerId);
            return Response.ok(loans).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting loans by borrower ID: " + borrowerId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving loans by borrower: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get loans by book ID.
     * @param bookId Book ID
     * @return List of loans for the book
     */
    @GET
    @Path("/book/{bookId}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getLoansByBook(@PathParam("bookId") Long bookId) {
        LOG.log(Level.INFO, "Getting loans by book ID: {0}", bookId);
        try {
            List<BookLoan> loans = bookLoanService.findByBook(bookId);
            return Response.ok(loans).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting loans by book ID: " + bookId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving loans by book: " + e.getMessage()).build();
        }
    }
    
    /**
     * Get loans by library ID.
     * @param libraryId Library ID
     * @return List of loans at the library
     */
    @GET
    @Path("/library/{libraryId}")
    @RolesAllowed({"ADMIN", "LIBRARIAN"})
    public Response getLoansByLibrary(@PathParam("libraryId") Long libraryId) {
        LOG.log(Level.INFO, "Getting loans by library ID: {0}", libraryId);
        try {
            List<BookLoan> loans = bookLoanService.findByLibrary(libraryId);
            return Response.ok(loans).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting loans by library ID: " + libraryId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving loans by library: " + e.getMessage()).build();
        }
    }
}