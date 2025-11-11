package edu.iit.itmd4515.test;

import edu.iit.itmd4515.domain.Book;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class TestValidation {
    public static void main(String[] args) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Book book = new Book("", "", "INVALID");
        book.setPageCount(-50);
        book.setPrice(-10.0);
        book.setPublicationDate(LocalDate.now().plusDays(30));
        book.setIsAvailable(null);
        
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        
        System.out.println("Total violations: " + violations.size());
        for (ConstraintViolation<Book> violation : violations) {
            System.out.println("Field: " + violation.getPropertyPath() + 
                             ", Message: " + violation.getMessage() +
                             ", Invalid value: " + violation.getInvalidValue());
        }
    }
}