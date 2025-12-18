package com.javadeveloper.springboot.exception;

import com.javadeveloper.springboot.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * JAX-RS Exception Mapper
 * 
 * Wandelt PersonNotFoundException automatisch in einen
 * strukturierten 404 Response um
 * 
 * @Provider - JAX-RS erkennt diese Klasse als Exception Handler
 */
@Provider
public class PersonNotFoundExceptionMapper implements ExceptionMapper<PersonNotFoundException> {

    @Override
    public Response toResponse(PersonNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
                Response.Status.NOT_FOUND.getStatusCode(),
                exception.getMessage(),
                System.currentTimeMillis()
        );

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}
