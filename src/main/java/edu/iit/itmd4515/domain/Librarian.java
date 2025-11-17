package edu.iit.itmd4515.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "librarians")
@NamedQuery(name = "Librarian.findAll", query = "SELECT l FROM Librarian l")
@NamedQuery(name = "Librarian.findByEmail", query = "SELECT l FROM Librarian l WHERE l.email = :email")
@NamedQuery(name = "Librarian.findActive", query = "SELECT l FROM Librarian l WHERE l.employed = true")
public class Librarian {
    
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
    
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(unique = true, length = 100)
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$|^\\([0-9]{3}\\) [0-9]{3}-[0-9]{4}$|^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "Phone number must be valid")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @NotBlank(message = "Employee ID is required")
    @Size(max = 20, message = "Employee ID must not exceed 20 characters")
    @Column(name = "employee_id", unique = true, nullable = false, length = 20)
    private String employeeId;
    
    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String position;
    
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date must be in the past or present")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @DecimalMin(value = "0.0", message = "Salary must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "Salary must have at most 8 integer digits and 2 fraction digits")
    @Column(precision = 10, scale = 2)
    private Double salary;
    
    @NotNull(message = "Employment status is required")
    @Column(nullable = false)
    private Boolean employed = true;
    
    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String department;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;
    
    @OneToMany(mappedBy = "processedBy", cascade = CascadeType.ALL)
    private List<BookLoan> processedLoans = new ArrayList<>();
    
    // Constructors
    public Librarian() {
    }
    
    public Librarian(String firstName, String lastName, String employeeId, String position, LocalDate hireDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.position = position;
        this.hireDate = hireDate;
    }
    
    // Helper methods for relationship management
    public void addProcessedLoan(BookLoan bookLoan) {
        if (!this.processedLoans.contains(bookLoan)) {
            this.processedLoans.add(bookLoan);
            bookLoan.setProcessedBy(this);
        }
    }
    
    public void removeProcessedLoan(BookLoan bookLoan) {
        if (this.processedLoans.contains(bookLoan)) {
            this.processedLoans.remove(bookLoan);
            bookLoan.setProcessedBy(null);
        }
    }
    
    public int getProcessedLoansCount() {
        return processedLoans.size();
    }
    
    public int getActiveProcessedLoansCount() {
        return (int) processedLoans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .count();
    }
    
    public double getYearsOfService() {
        return LocalDate.now().until(hireDate, java.time.temporal.ChronoUnit.YEARS) * -1.0;
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
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public Double getSalary() {
        return salary;
    }
    
    public void setSalary(Double salary) {
        this.salary = salary;
    }
    
    public Boolean getEmployed() {
        return employed;
    }
    
    public void setEmployed(Boolean employed) {
        this.employed = employed;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Library getLibrary() {
        return library;
    }
    
    public void setLibrary(Library library) {
        this.library = library;
    }
    
    public List<BookLoan> getProcessedLoans() {
        return processedLoans;
    }
    
    public void setProcessedLoans(List<BookLoan> processedLoans) {
        this.processedLoans = processedLoans;
    }
    
    // equals and hashCode based on employeeId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Librarian librarian = (Librarian) o;
        return Objects.equals(employeeId, librarian.employeeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
    
    @Override
    public String toString() {
        return "Librarian{" +
                "id=" + id +
                ", name='" + getFullName() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", library='" + (library != null ? library.getName() : "null") + '\'' +
                ", employed=" + employed +
                ", yearsOfService=" + getYearsOfService() +
                ", processedLoans=" + getProcessedLoansCount() +
                '}';
    }
}