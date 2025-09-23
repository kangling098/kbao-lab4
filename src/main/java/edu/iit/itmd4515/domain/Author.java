package edu.iit.itmd4515.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "authors")
@NamedQuery(name = "Author.findAll", query = "SELECT a FROM Author a")
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Past(message = "Birth date must be in the past")
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(unique = true, length = 100)
    private String email;
    
    @Size(max = 500, message = "Biography must not exceed 500 characters")
    @Column(length = 500)
    private String biography;
    
    @NotBlank(message = "Nationality is required")
    @Size(max = 50, message = "Nationality must not exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String nationality;
    
    // Relationships
    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();
    
    // Constructors
    public Author() {
    }
    
    public Author(String firstName, String lastName, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }
    
    // Helper methods for bidirectional relationship
    public void addBook(Book book) {
        if (!this.books.contains(book)) {
            this.books.add(book);
            if (!book.getAuthors().contains(this)) {
                book.getAuthors().add(this);
            }
        }
    }
    
    public void removeBook(Book book) {
        if (this.books.contains(book)) {
            this.books.remove(book);
            if (book.getAuthors().contains(this)) {
                book.getAuthors().remove(this);
            }
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getBiography() {
        return biography;
    }
    
    public void setBiography(String biography) {
        this.biography = biography;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public List<Book> getBooks() {
        return books;
    }
    
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    // equals and hashCode based on email (if available) or full name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        if (email != null) {
            return Objects.equals(email, author.email);
        }
        return Objects.equals(firstName, author.firstName) && 
               Objects.equals(lastName, author.lastName);
    }
    
    @Override
    public int hashCode() {
        if (email != null) {
            return Objects.hash(email);
        }
        return Objects.hash(firstName, lastName);
    }
    
    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", nationality='" + nationality + '\'' +
                ", booksCount=" + books.size() +
                '}';
    }
}