package com.javadeveloper.springboot.exception;

/**
 * Custom Exception für Person nicht gefunden
 * 
 * Wird von PersonNotFoundExceptionMapper automatisch
 * zu einem 404 HTTP Response umgewandelt
 */
public class PersonNotFoundException extends RuntimeException {

    private final Long personId;

    public PersonNotFoundException(Long id) {
        super("Person not found with id: " + id);
        this.personId = id;
    }

    public Long getPersonId() {
        return personId;
    }
}
