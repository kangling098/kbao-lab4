package edu.iit.itmd4515.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "publishers")
@NamedQuery(name = "Publisher.findAll", query = "SELECT p FROM Publisher p")
public class Publisher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Publisher name is required")
    @Size(max = 100, message = "Publisher name must not exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must not exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String address;
    
    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String city;
    
    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country must not exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String country;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(unique = true, length = 100)
    private String email;
    
    @Past(message = "Founded date must be in the past")
    @Column(name = "founded_date")
    private LocalDate foundedDate;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;
    
    @NotNull(message = "Active status is required")
    @Column(nullable = false)
    private Boolean active = true;
    
    // Relationships
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();
    
    // Constructors
    public Publisher() {
    }
    
    public Publisher(String name, String address, String city, String country) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
    }
    
    // Helper methods for bidirectional relationship
    public void addBook(Book book) {
        if (!this.books.contains(book)) {
            this.books.add(book);
<<<<<<< HEAD
            // Set the publisher on the book side, but avoid infinite recursion
            if (book.getPublisher() != this) {
                book.setPublisher(this);
            }
=======
            book.setPublisher(this);
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
        }
    }
    
    public void removeBook(Book book) {
        if (this.books.contains(book)) {
            this.books.remove(book);
<<<<<<< HEAD
            // Remove the publisher reference from the book
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
            book.setPublisher(null);
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDate getFoundedDate() {
        return foundedDate;
    }
    
    public void setFoundedDate(LocalDate foundedDate) {
        this.foundedDate = foundedDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public List<Book> getBooks() {
        return books;
    }
    
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    // equals and hashCode based on name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return Objects.equals(name, publisher.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", active=" + active +
                ", booksCount=" + books.size() +
                '}';
    }
}