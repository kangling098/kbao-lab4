package edu.iit.itmd4515.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "book_loans")
@NamedQuery(name = "BookLoan.findAll", query = "SELECT bl FROM BookLoan bl")
@NamedQuery(name = "BookLoan.findActiveLoans", query = "SELECT bl FROM BookLoan bl WHERE bl.returnDate IS NULL")
public class BookLoan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Loan date is required")
    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;
    
    @NotNull(message = "Due date is required")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    @NotBlank(message = "Borrower name is required")
    @Size(max = 100, message = "Borrower name must not exceed 100 characters")
    @Column(name = "borrower_name", nullable = false, length = 100)
    private String borrowerName;
    
    @Email(message = "Borrower email must be valid")
    @Size(max = 100, message = "Borrower email must not exceed 100 characters")
    @Column(name = "borrower_email", length = 100)
    private String borrowerEmail;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "borrower_phone", length = 20)
    private String borrowerPhone;
    
    @DecimalMin(value = "0.0", message = "Fine amount must be non-negative")
    @Digits(integer = 6, fraction = 2, message = "Fine amount must have at most 6 integer digits and 2 fraction digits")
    @Column(name = "fine_amount")
    private Double fineAmount = 0.0;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(length = 500)
    private String notes;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private Borrower borrower;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by_id")
    private Librarian processedBy;
    
    // Constructors
    public BookLoan() {
    }
    
    public BookLoan(LocalDate loanDate, LocalDate dueDate, String borrowerName) {
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.borrowerName = borrowerName;
    }
    
    public BookLoan(LocalDate loanDate, LocalDate dueDate, Borrower borrower) {
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.borrower = borrower;
        this.borrowerName = borrower.getFullName();
        this.borrowerEmail = borrower.getEmail();
        this.borrowerPhone = borrower.getPhoneNumber();
    }
    
    // Business methods
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
    }
    
    public void returnBook() {
        this.returnDate = LocalDate.now();
        if (isOverdue()) {
            this.fineAmount = getDaysOverdue() * 1.0; // $1 per day
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getLoanDate() {
        return loanDate;
    }
    
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getBorrowerName() {
        return borrowerName;
    }
    
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
    
    public String getBorrowerEmail() {
        return borrowerEmail;
    }
    
    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }
    
    public String getBorrowerPhone() {
        return borrowerPhone;
    }
    
    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
    }
    
    public Double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public Library getLibrary() {
        return library;
    }
    
    public void setLibrary(Library library) {
        this.library = library;
    }
    
    public Borrower getBorrower() {
        return borrower;
    }
    
    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }
    
    public Librarian getProcessedBy() {
        return processedBy;
    }
    
    public void setProcessedBy(Librarian processedBy) {
        this.processedBy = processedBy;
    }
    
    // equals and hashCode based on book, library, loanDate, and borrowerName
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookLoan bookLoan = (BookLoan) o;
        return Objects.equals(book, bookLoan.book) &&
               Objects.equals(library, bookLoan.library) &&
               Objects.equals(loanDate, bookLoan.loanDate) &&
               Objects.equals(borrowerName, bookLoan.borrowerName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(book, library, loanDate, borrowerName);
    }
    
    @Override
    public String toString() {
        return "BookLoan{" +
                "id=" + id +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", borrowerName='" + borrowerName + '\'' +
                ", borrowerEmail='" + borrowerEmail + '\'' +
                ", bookTitle='" + (book != null ? book.getTitle() : "null") + '\'' +
                ", libraryName='" + (library != null ? library.getName() : "null") + '\'' +
                ", borrower='" + (borrower != null ? borrower.getFullName() : "null") + '\'' +
                ", processedBy='" + (processedBy != null ? processedBy.getFullName() : "null") + '\'' +
                ", overdue=" + isOverdue() +
                ", fineAmount=" + fineAmount +
                '}';
    }
}