package edu.iit.itmd4515.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "borrowers")
@NamedQuery(name = "Borrower.findAll", query = "SELECT b FROM Borrower b")
@NamedQuery(name = "Borrower.findByEmail", query = "SELECT b FROM Borrower b WHERE b.email = :email")
public class Borrower {
    
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
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must not exceed 200 characters")
    @Column(length = 200)
    private String address;
    
    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Column(length = 50)
    private String city;
    
    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State must not exceed 50 characters")
    @Column(length = 50)
    private String state;
    
    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "ZIP code must be valid")
    @Column(name = "zip_code", length = 10)
    private String zipCode;
    
    @NotNull(message = "Membership status is required")
    @Column(name = "membership_active", nullable = false)
    private Boolean membershipActive = true;
    
    @PastOrPresent(message = "Membership date must be in the past or present")
    @Column(name = "membership_date")
    private LocalDate membershipDate = LocalDate.now();
    
    // Relationships
    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookLoan> bookLoans = new ArrayList<>();
    
    // Constructors
    public Borrower() {
    }
    
    public Borrower(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    // Helper methods for relationship management
    public void addBookLoan(BookLoan bookLoan) {
        if (!this.bookLoans.contains(bookLoan)) {
            this.bookLoans.add(bookLoan);
            bookLoan.setBorrower(this);
        }
    }
    
    public void removeBookLoan(BookLoan bookLoan) {
        if (this.bookLoans.contains(bookLoan)) {
            this.bookLoans.remove(bookLoan);
            bookLoan.setBorrower(null);
        }
    }
    
    public int getActiveLoansCount() {
        return (int) bookLoans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .count();
    }
    
    public boolean hasOverdueBooks() {
        return bookLoans.stream()
                .anyMatch(BookLoan::isOverdue);
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
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
    
    public Boolean getMembershipActive() {
        return membershipActive;
    }
    
    public void setMembershipActive(Boolean membershipActive) {
        this.membershipActive = membershipActive;
    }
    
    public LocalDate getMembershipDate() {
        return membershipDate;
    }
    
    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }
    
    public List<BookLoan> getBookLoans() {
        return bookLoans;
    }
    
    public void setBookLoans(List<BookLoan> bookLoans) {
        this.bookLoans = bookLoans;
    }
    
    // equals and hashCode based on email
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrower borrower = (Borrower) o;
        return Objects.equals(email, borrower.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
    
    @Override
    public String toString() {
        return "Borrower{" +
                "id=" + id +
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                ", membershipActive=" + membershipActive +
                ", activeLoans=" + getActiveLoansCount() +
                ", hasOverdue=" + hasOverdueBooks() +
                '}';
    }
}