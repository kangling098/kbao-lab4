package edu.iit.itmd4515.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "libraries")
@NamedQuery(name = "Library.findAll", query = "SELECT l FROM Library l")
public class Library {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Library name is required")
    @Size(max = 100, message = "Library name must not exceed 100 characters")
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
    
    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State must not exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String state;
    
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "ZIP code must be valid")
    @Size(max = 10, message = "ZIP code must not exceed 10 characters")
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$|^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "Phone number must be valid")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(unique = true, length = 100)
    private String email;
    
    @NotNull(message = "Opening time is required")
    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;
    
    @NotNull(message = "Closing time is required")
    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 999999, message = "Capacity must not exceed 999999")
    @Column(nullable = false)
    private Integer capacity;
    
    @NotNull(message = "Active status is required")
    @Column(nullable = false)
    private Boolean active = true;
    
    // Relationships
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookLoan> bookLoans = new ArrayList<>();
    
    // Constructors
    public Library() {
    }
    
    public Library(String name, String address, String city, String state, String zipCode, 
                   LocalTime openingTime, LocalTime closingTime, Integer capacity) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.capacity = capacity;
    }
    
    // Helper methods for bidirectional relationship
    public void addBookLoan(BookLoan bookLoan) {
        if (!this.bookLoans.contains(bookLoan)) {
            this.bookLoans.add(bookLoan);
            bookLoan.setLibrary(this);
        }
    }
    
    public void removeBookLoan(BookLoan bookLoan) {
        if (this.bookLoans.contains(bookLoan)) {
            this.bookLoans.remove(bookLoan);
            bookLoan.setLibrary(null);
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
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
    
    public LocalTime getOpeningTime() {
        return openingTime;
    }
    
    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }
    
    public LocalTime getClosingTime() {
        return closingTime;
    }
    
    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public List<BookLoan> getBookLoans() {
        return bookLoans;
    }
    
    public void setBookLoans(List<BookLoan> bookLoans) {
        this.bookLoans = bookLoans;
    }
    
    // equals and hashCode based on name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return Objects.equals(name, library.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", active=" + active +
                ", bookLoansCount=" + bookLoans.size() +
                '}';
    }
}