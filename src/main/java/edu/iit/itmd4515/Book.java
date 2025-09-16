package edu.iit.itmd4515;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "books")
@NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b")
@NamedQuery(name = "Book.findByIsbn", query = "SELECT b FROM Book b WHERE b.isbn = :isbn")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String title;
    
    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String author;
    
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "\\d{10,13}", message = "ISBN must be 10-13 digits")
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;
    
    @NotBlank(message = "Publisher is required")
    @Size(max = 100, message = "Publisher must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String publisher;
    
    @PastOrPresent(message = "Publication date must be in the past or present")
    @Column(name = "publication_date")
    private LocalDate publicationDate;
    
    @Min(value = 0, message = "Page count must be non-negative")
    @Max(value = 9999, message = "Page count must not exceed 9999")
    @Column(name = "page_count")
    private Integer pageCount;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 6, fraction = 2, message = "Price must have at most 6 integer digits and 2 fraction digits")
    private Double price;
    
    @NotNull(message = "Availability status is required")
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;
    
    @Future(message = "Due date must be in the future")
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    // Constructors
    public Book() {
    }
    
    public Book(String title, String author, String isbn, String publisher) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
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
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    // equals and hashCode based on business key (ISBN)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
    
    // toString for formatted output
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationDate=" + publicationDate +
                ", pageCount=" + pageCount +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", dueDate=" + dueDate +
                '}';
    }
}