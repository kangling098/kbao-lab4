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
    
<<<<<<< HEAD
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$|^\\(\\d{3}\\) \\d{3}-\\d{4}$|^\\d{10}$", message = "Phone must be in format XXX-XXX-XXXX, (XXX) XXX-XXXX, or XXXXXXXXXX")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
    @Past(message = "Birth date must be in the past")
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
<<<<<<< HEAD
=======
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
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
    @Size(max = 200, message = "Address must not exceed 200 characters")
    @Column(length = 200)
    private String address;
    
<<<<<<< HEAD
=======
    @NotBlank(message = "City is required")
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Column(length = 50)
    private String city;
    
<<<<<<< HEAD
    @Size(max = 2, message = "State must be 2 characters")
    @Pattern(regexp = "^[A-Z]{2}$", message = "State must be 2 uppercase letters")
    @Column(length = 2)
    private String state;
    
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Zip code must be in format XXXXX or XXXXX-XXXX")
    @Size(max = 10, message = "Zip code must not exceed 10 characters")
    @Column(name = "zip_code", length = 10)
    private String zipCode;
    
    @Column(name = "membership_active", nullable = false)
    private Boolean membershipActive = true;
    
    @OneToOne(mappedBy = "borrower", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;
    
=======
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
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
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
    
<<<<<<< HEAD
=======
    public int getActiveLoansCount() {
        return (int) bookLoans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .count();
    }
    
    public boolean hasOverdueBooks() {
        return bookLoans.stream()
                .anyMatch(BookLoan::isOverdue);
    }
    
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
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
    
<<<<<<< HEAD
=======
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
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
    
<<<<<<< HEAD
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
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
    
<<<<<<< HEAD
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
=======
    public LocalDate getMembershipDate() {
        return membershipDate;
    }
    
    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
    }
    
    public List<BookLoan> getBookLoans() {
        return bookLoans;
    }
    
    public void setBookLoans(List<BookLoan> bookLoans) {
        this.bookLoans = bookLoans;
    }
    
<<<<<<< HEAD
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
=======
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
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
<<<<<<< HEAD
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", bookLoans=" + bookLoans.size() +
=======
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                ", membershipActive=" + membershipActive +
                ", activeLoans=" + getActiveLoansCount() +
                ", hasOverdue=" + hasOverdueBooks() +
>>>>>>> 967182f8513ce6efcafc871e8e037746cd98c5b9
                '}';
    }
}