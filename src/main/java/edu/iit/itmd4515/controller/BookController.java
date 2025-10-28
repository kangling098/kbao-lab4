package edu.iit.itmd4515.controller;

import edu.iit.itmd4515.domain.Book;
import edu.iit.itmd4515.service.BookService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JSF Backing Bean for Book entity management.
 * Handles CRUD operations for Book entities through JSF pages.
 * 
 * @author Lab 7 - JSF Implementation
 */
@Named("bookController")
@RequestScoped
public class BookController {
    
    private static final Logger LOG = Logger.getLogger(BookController.class.getName());
    
    @EJB
    private BookService bookService;
    
    // Form fields for book creation
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author must not exceed 100 characters")
    private String author;
    
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "\\d{10,13}", message = "ISBN must be 10-13 digits")
    private String isbn;
    
    private LocalDate publicationDate;
    private Integer pageCount;
    private Double price;
    
    // Success message for confirmation page
    private String successMessage;
    
    // Created book for display
    private Book createdBook;
    
    /**
     * Initialize the controller.
     */
    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "Initializing BookController");
    }
    
    /**
     * Action method to create a new book.
     * @return navigation outcome
     */
    public String createBook() {
        LOG.log(Level.INFO, "Creating new book: {0} by {1}", new Object[]{title, author});
        
        try {
            // Create new book entity
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setPublicationDate(publicationDate);
            book.setPageCount(pageCount);
            book.setPrice(price);
            book.setIsAvailable(true);
            
            // Persist the book using EJB service
            bookService.create(book);
            
            // Store the created book for confirmation page
            this.createdBook = book;
            this.successMessage = "Book successfully created with ID: " + book.getId();
            
            LOG.log(Level.INFO, "Book created successfully with ID: {0}", book.getId());
            
            // Navigate to confirmation page
            return "confirmation?faces-redirect=true";
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error creating book", e);
            // JSF will automatically display validation messages
            return null; // Stay on the same page
        }
    }
    
    /**
     * Reset form fields.
     */
    public void reset() {
        LOG.log(Level.INFO, "Resetting form fields");
        title = null;
        author = null;
        isbn = null;
        publicationDate = null;
        pageCount = null;
        price = null;
        createdBook = null;
        successMessage = null;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
    
    public Integer getPageCount() {
        return pageCount;
    }
    
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Book getCreatedBook() {
        return createdBook;
    }
    
    public void setCreatedBook(Book createdBook) {
        this.createdBook = createdBook;
    }
    
    public String getSuccessMessage() {
        return successMessage;
    }
    
    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}