package com.timelink.time_link.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex,
                                        HttpServletRequest req) {
        ProblemDetail pd = ex.getBody();
        pd.setInstance(URI.create(req.getRequestURI()));
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleRequestObjectValidation(MethodArgumentNotValidException ex,
                                                       HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Validation Failed");
        pd.setDetail("One or more fields are invalid");
        pd.setInstance(URI.create(req.getRequestURI()));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        pd.setProperty("errors", errors);

        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleRequestVariableValidation(ConstraintViolationException ex,
                                                         HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Constraint violation");
        pd.setDetail("Invalid request parameters");
        pd.setInstance(URI.create(req.getRequestURI()));

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(
                v -> errors.put(v.getPropertyPath().toString(), v.getMessage())
        );
        pd.setProperty("errors", errors);

        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex,
                                                HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(500);
        pd.setTitle("Internal Server Error");
        pd.setDetail("An unexpected error occurred");
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("error", ex.getMessage());

        return pd;
    }
}
