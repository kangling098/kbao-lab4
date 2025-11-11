package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.BookLoan;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stateless EJB service for BookLoan entity operations.
 * Provides CRUD operations and business logic for BookLoan management.
 */
@Stateless
public class BookLoanService extends AbstractService<BookLoan> {
    
    private static final Logger LOG = Logger.getLogger(BookLoanService.class.getName());
    
    public BookLoanService() {
        super(BookLoan.class);
    }
    
    /**
     * Find active loans (not returned).
     * @return list of active loans
     */
    public List<BookLoan> findActiveLoans() {
        LOG.log(Level.INFO, "Finding active loans");
        TypedQuery<BookLoan> query = em.createQuery("SELECT bl FROM BookLoan bl WHERE bl.returnDate IS NULL", BookLoan.class);
        return query.getResultList();
    }
    
    /**
     * Find overdue loans.
     * @return list of overdue loans
     */
    public List<BookLoan> findOverdueLoans() {
        LOG.log(Level.INFO, "Finding overdue loans");
        TypedQuery<BookLoan> query = em.createQuery(
            "SELECT bl FROM BookLoan bl WHERE bl.returnDate IS NULL AND bl.dueDate < :today", BookLoan.class);
        query.setParameter("today", LocalDate.now());
        return query.getResultList();
    }
    
    /**
     * Find loans by borrower.
     * @param borrowerId the borrower ID
     * @return list of loans for the borrower
     */
    public List<BookLoan> findByBorrower(Long borrowerId) {
        LOG.log(Level.INFO, "Finding loans by borrower ID: {0}", borrowerId);
        TypedQuery<BookLoan> query = em.createQuery("SELECT bl FROM BookLoan bl WHERE bl.borrower.id = :borrowerId", BookLoan.class);
        query.setParameter("borrowerId", borrowerId);
        return query.getResultList();
    }
    
    /**
     * Find loans by book.
     * @param bookId the book ID
     * @return list of loans for the book
     */
    public List<BookLoan> findByBook(Long bookId) {
        LOG.log(Level.INFO, "Finding loans by book ID: {0}", bookId);
        TypedQuery<BookLoan> query = em.createQuery("SELECT bl FROM BookLoan bl WHERE bl.book.id = :bookId", BookLoan.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }
    
    /**
     * Find loans by library.
     * @param libraryId the library ID
     * @return list of loans at the library
     */
    public List<BookLoan> findByLibrary(Long libraryId) {
        LOG.log(Level.INFO, "Finding loans by library ID: {0}", libraryId);
        TypedQuery<BookLoan> query = em.createQuery("SELECT bl FROM BookLoan bl WHERE bl.library.id = :libraryId", BookLoan.class);
        query.setParameter("libraryId", libraryId);
        return query.getResultList();
    }
    
    /**
     * Find active loans by borrower name.
     * @param borrowerName the borrower name
     * @return list of active loans for the borrower
     */
    public List<BookLoan> findActiveLoansByUser(String borrowerName) {
        LOG.log(Level.INFO, "Finding active loans by borrower name: {0}", borrowerName);
        TypedQuery<BookLoan> query = em.createQuery(
            "SELECT bl FROM BookLoan bl WHERE bl.borrowerName = :borrowerName AND bl.returnDate IS NULL", BookLoan.class);
        query.setParameter("borrowerName", borrowerName);
        return query.getResultList();
    }
    
    /**
     * Process a book return.
     * @param loanId the loan ID
     * @return the updated loan
     */
    @Override
    public BookLoan update(BookLoan loan) {
        LOG.log(Level.INFO, "Processing book return for loan ID: {0}", loan.getId());
        if (loan.getReturnDate() == null) {
            loan.setReturnDate(LocalDate.now());
        }
        // The overdue status is calculated by the entity's isOverdue() method
        return super.update(loan);
    }
}