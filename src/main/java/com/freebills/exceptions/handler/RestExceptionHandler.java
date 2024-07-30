package com.freebills.exceptions.handler;

import com.freebills.exceptions.AccountNotFoundException;
import com.freebills.exceptions.CategoryNotFoundException;
import com.freebills.exceptions.CreditCardNotFoundException;
import com.freebills.exceptions.InvalidCredentialsException;
import com.freebills.exceptions.PermissionDeniedException;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.exceptions.TransferNotFoundException;
import com.freebills.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.naming.NoPermissionException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TransactionNotFoundException.class)
    public ExceptionFilters transactionNotFoundException(final TransactionNotFoundException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(NOT_FOUND.value())
                .title("Transaction not found!")
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ExceptionFilters handleUserNotFound(final UserNotFoundException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(NOT_FOUND.value())
                .title("User not found!")
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CreditCardNotFoundException.class)
    public ExceptionFilters handleCreditCardNotFound(final CreditCardNotFoundException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(NOT_FOUND.value())
                .title("credit card not found!")
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TransferNotFoundException.class)
    public ExceptionFilters handleTransferNotFound(final TransferNotFoundException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(NOT_FOUND.value())
                .title("Transfer not found!")
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ExceptionFilters handleCategoryNotFound(final CategoryNotFoundException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(NOT_FOUND.value())
                .title("category not found!")
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    public ExceptionFilters handleAccountNotFound(final AccountNotFoundException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(NOT_FOUND.value())
                .title("account not found!")
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(DateTimeParseException.class)
    public ExceptionFilters handleDateTime(final DateTimeParseException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(NOT_FOUND.value())
                .title("Invalid date!")
                .build();
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ExceptionFilters handleInvalidCretentials(final InvalidCredentialsException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(UNAUTHORIZED.value())
                .title("Invalid Credentials")
                .build();
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ExceptionFilters maxFileSizeExceeded(final MaxUploadSizeExceededException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(NOT_ACCEPTABLE.value())
                .title("File size exception")
                .build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public ExceptionFilters nullPointer(final NullPointerException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(INTERNAL_SERVER_ERROR.value())
                .title("Nullpointer")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ExceptionFilters dataIntegrationViolation(final DataIntegrityViolationException ex) {
        String result = null;

        final Pattern pattern = Pattern.compile("Key (.*)");
        final Matcher comparator = pattern.matcher(ex.getCause().getCause().getMessage());

        if (comparator.find()) {
            result = comparator.group(1);
        }

        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(result)
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("Data Violation")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ExceptionFilters missingServletRequestPartException(final MissingServletRequestPartException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("Image not found!")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ExceptionFilters constraintViolationException(ConstraintViolationException ex) {

        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getMessage().concat(";"));
        }

        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(message.toString())
                .devMsg(ex.getClass().getName())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Invalid Arguments!")
                .build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileNotFoundException.class)
    public ExceptionFilters fileNotFoundException(FileNotFoundException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("Data violation")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(NoPermissionException.class)
    public ExceptionFilters noPermissionException(NoPermissionException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("NoPermissionException")
                .build();
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(PermissionDeniedException.class)
    public ExceptionFilters permissionDenied(final PermissionDeniedException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(UNAUTHORIZED.value())
                .title("Permission Denied!")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ExceptionFilters methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("MethodArgumentTypeMismatchException")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ExceptionFilters emptyResultDataAccessException(EmptyResultDataAccessException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("EmptyResultDataAccessException")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ExceptionFilters httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("HttpRequestMethodNotSupportedException")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionFilters methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("MethodArgumentNotValidException")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionFilters illegalArgumentException(IllegalArgumentException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .devMsg(ex.getClass().getName())
                .status(BAD_REQUEST.value())
                .title("IllegalArgumentException")
                .build();
    }
}

