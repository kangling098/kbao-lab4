package edu.iit.itmd4515.service;

import edu.iit.itmd4515.domain.Book;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stateless EJB service for Book entity operations.
 * Provides CRUD operations and business logic for Book management.
 */
@Stateless
public class BookService extends AbstractService<Book> {
    
    private static final Logger LOG = Logger.getLogger(BookService.class.getName());
    
    public BookService() {
        super(Book.class);
    }
    
    /**
     * Find books by title.
     * @param title the title to search for
     * @return list of matching books
     */
    public List<Book> findByTitle(String title) {
        LOG.log(Level.INFO, "Finding books by title: {0}", title);
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.title LIKE :title", Book.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }
    
    /**
     * Find books by author name.
     * @param authorName the author name to search for
     * @return list of matching books
     */
    public List<Book> findByAuthor(String authorName) {
        LOG.log(Level.INFO, "Finding books by author: {0}", authorName);
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.author LIKE :author", Book.class);
        query.setParameter("author", "%" + authorName + "%");
        return query.getResultList();
    }
    
    /**
     * Find books by ISBN.
     * @param isbn the ISBN to search for
     * @return the book or null
     */
    public Book findByIsbn(String isbn) {
        LOG.log(Level.INFO, "Finding book by ISBN: {0}", isbn);
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
        query.setParameter("isbn", isbn);
        List<Book> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Find available books.
     * @return list of available books
     */
    public List<Book> findAvailableBooks() {
        LOG.log(Level.INFO, "Finding available books");
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isAvailable = true", Book.class);
        return query.getResultList();
    }
    
    /**
     * Find books by publisher.
     * @param publisherId the publisher ID
     * @return list of books from the publisher
     */
    public List<Book> findByPublisher(Long publisherId) {
        LOG.log(Level.INFO, "Finding books by publisher ID: {0}", publisherId);
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.publisher.id = :publisherId", Book.class);
        query.setParameter("publisherId", publisherId);
        return query.getResultList();
    }
}