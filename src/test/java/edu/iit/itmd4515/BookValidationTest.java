package edu.iit.itmd4515;

import edu.iit.itmd4515.domain.Book;
import jakarta.validation.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class BookValidationTest {
    
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    
    @BeforeAll
    public static void setUpClass() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @AfterAll
    public static void tearDownClass() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }
    
    @Test
    @DisplayName("Test Valid Book - Sunny Day")
    public void testValidBook() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884");
        book.setPublicationDate(LocalDate.of(2008, 8, 1));
        book.setPageCount(464);
        book.setPrice(42.50);
        book.setIsAvailable(true);
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertTrue(violations.isEmpty(), "Valid book should have no violations");
    }
    
    @Test
    @DisplayName("Test Empty Title - Rainy Day")
    public void testEmptyTitle() {
        Book book = new Book("", "Robert C. Martin", "9780132350884");
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty(), "Book with empty title should have violations");
        
        boolean hasTitleViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("title") && 
                          v.getMessage().equals("Title is required"));
        assertTrue(hasTitleViolation, "Should have title required violation");
    }
    
    @Test
    @DisplayName("Test Invalid ISBN Format - Rainy Day")
    public void testInvalidIsbnFormat() {
        Book book = new Book("Clean Code", "Robert C. Martin", "INVALID123");
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty(), "Book with invalid ISBN should have violations");
        
        boolean hasIsbnViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("isbn") && 
                          v.getMessage().contains("ISBN must be 10-13 digits"));
        assertTrue(hasIsbnViolation, "Should have ISBN format violation");
    }
    
    @Test
    @DisplayName("Test Negative Page Count - Rainy Day")
    public void testNegativePageCount() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884");
        book.setPageCount(-100);
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty(), "Book with negative page count should have violations");
        
        boolean hasPageCountViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("pageCount") && 
                          v.getMessage().contains("must be non-negative"));
        assertTrue(hasPageCountViolation, "Should have page count violation");
    }
    
    @Test
    @DisplayName("Test Future Publication Date - Rainy Day")
    public void testFuturePublicationDate() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884");
        book.setPublicationDate(LocalDate.now().plusDays(30)); // Future date
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty(), "Book with future publication date should have violations");
        
        boolean hasDateViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("publicationDate") && 
                          v.getMessage().contains("must be in the past or present"));
        assertTrue(hasDateViolation, "Should have publication date violation");
    }
    
    @Test
    @DisplayName("Test Zero Price - Rainy Day")
    public void testZeroPrice() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884");
        book.setPrice(0.0);
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty(), "Book with zero price should have violations");
        
        boolean hasPriceViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("price") && 
                          v.getMessage().contains("must be greater than 0"));
        assertTrue(hasPriceViolation, "Should have price violation");
    }
    
    @Test
    @DisplayName("Test Title Too Long - Rainy Day")
    public void testTitleTooLong() {
        String longTitle = "A".repeat(250); // Title longer than 200 characters
        Book book = new Book(longTitle, "Robert C. Martin", "9780132350884");
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty(), "Book with too long title should have violations");
        
        boolean hasTitleLengthViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("title") && 
                          v.getMessage().contains("must not exceed 200 characters"));
        assertTrue(hasTitleLengthViolation, "Should have title length violation");
    }
    
    @Test
    @DisplayName("Test Null Availability - Rainy Day")
    public void testNullAvailability() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884");
        book.setIsAvailable(null);
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty(), "Book with null availability should have violations");
        
        boolean hasAvailabilityViolation = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("isAvailable") && 
                          v.getMessage().contains("Availability status is required"));
        assertTrue(hasAvailabilityViolation, "Should have availability violation");
    }
    
    @Test
    @DisplayName("Test Multiple Violations - Rainy Day")
    public void testMultipleViolations() {
        Book book = new Book("", "", "INVALID");
        book.setPageCount(-50);
        book.setPrice(-10.0);
        book.setPublicationDate(LocalDate.now().plusDays(30));
        book.setIsAvailable(null);
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty(), "Book with multiple errors should have violations");
        assertTrue(violations.size() >= 6, "Should have at least 6 violations");
        
        // Check for specific violations
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("author")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("isbn")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("publisher")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("pageCount")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    }
}