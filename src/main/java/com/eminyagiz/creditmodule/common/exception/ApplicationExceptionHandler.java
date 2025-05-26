package com.eminyagiz.creditmodule.common.exception;

import com.eminyagiz.creditmodule.model.dto.APIErrorResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Date;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIErrorResponse> handleValidationException(Exception validationException) {
        log.error(validationException.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIErrorResponse(validationException.getMessage(), "001", Date.from(Instant.now())));
    }

    @ExceptionHandler({AuthorizationTokenException.class})
    public ResponseEntity<APIErrorResponse> handleException(AuthorizationTokenException authorizationTokenException) {
        log.error(authorizationTokenException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIErrorResponse(authorizationTokenException.getMessage(), "002", Date.from(Instant.now())));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<APIErrorResponse> handleNotFoundExceptions(EntityNotFoundException validationException) {
        log.error(validationException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIErrorResponse(validationException.getMessage(), "003", Date.from(Instant.now())));
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<APIErrorResponse> handleEntityExistsException(EntityExistsException entityExistsException) {
        log.error(entityExistsException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIErrorResponse(entityExistsException.getMessage(), "004", Date.from(Instant.now())));
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<APIErrorResponse> handleException(AuthenticationException authenticationException) {
        log.error(authenticationException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new APIErrorResponse(authenticationException.getMessage(), "005", Date.from(Instant.now())));
    }

    @ExceptionHandler({CustomerNotEnoughLimitForLoanException.class, IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<APIErrorResponse> handleCustomerNotEnoughLimitForLoanException(RuntimeException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIErrorResponse(exception.getMessage(), "006", Date.from(Instant.now())));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIErrorResponse(ex.getMessage(), "007", Date.from(Instant.now())));
    }

}
