package namankhurana.zorvyn_technical_assignment.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 NOT FOUND
    @ExceptionHandler({
            UserNotFoundException.class,
            ResourceNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex,
                                                        HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    // 409 CONFLICT
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(Exception ex,
                                                        HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    // 403 FORBIDDEN
    @ExceptionHandler(ForbiddenResourceException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(Exception ex,
                                                         HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    // 400 BAD REQUEST
    @ExceptionHandler({
            BadRequestException.class,
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex,
                                                          HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    // VALIDATION ERROR
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return buildResponse(errorMessage, HttpStatus.BAD_REQUEST, request);
    }

    // INVALID JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String message = "Invalid request body";

        return buildResponse(message, HttpStatus.BAD_REQUEST, request);
    }

    // 405 METHOD NOT ALLOWED
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        String message = "HTTP method not supported for this endpoint";

        return buildResponse(message, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        String message = "API endpoint not found: " + request.getRequestURI();

        return buildResponse(message, HttpStatus.NOT_FOUND, request);
    }

    //JWT EXCEPTIONS

    @ExceptionHandler({
            BadCredentialsException.class,
            AccountStatusException.class,
            AccessDeniedException.class,
            SignatureException.class,
            ExpiredJwtException.class,
            JwtException.class,
            AuthenticationException.class
    })
    public ResponseEntity<ErrorResponse> handleSecurityException(
            Exception exception,
            HttpServletRequest request) {

        exception.printStackTrace(); // send to logging system in production

        HttpStatus status;
        String message;

        if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            message = "Invalid username or password";
        }
        else if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            message = "Account is locked or disabled";
        }
        else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            message = "You do not have permission to access this resource";
        }
        else if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED;
            message = "JWT token has expired";
        }
        else if (exception instanceof SignatureException) {
            status = HttpStatus.UNAUTHORIZED;
            message = "Invalid JWT signature";
        }
        else if (exception instanceof JwtException) {
            status = HttpStatus.UNAUTHORIZED;
            message = "Invalid JWT token";
        }
        else if (exception instanceof AuthenticationException) {
            status = HttpStatus.UNAUTHORIZED;
            message = "Authentication failed";
        }
        else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Security error occurred";
        }

        return buildResponse(message, status, request);
    }



    // GENERAL EXCEPTION
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex,
                                                       HttpServletRequest request) {
        return buildResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }













    private ResponseEntity<ErrorResponse> buildResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, status);
    }


}
