package com.example.springbootrevision.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class DefaultExceptionHandler {

  @ExceptionHandler(ResourceNotFound.class)
  public ResponseEntity<ApiError> handleException(ResourceNotFound e,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
    return new ResponseEntity<>(new ApiError(
        e.getMessage(),
        request.getRequestURI(),
        HttpStatus.NOT_FOUND.value(),
        LocalDateTime.now()
    ), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ApiError> handleException(DuplicateResourceException e,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
    return new ResponseEntity<>(new ApiError(
        e.getMessage(),
        request.getRequestURI(),
        HttpStatus.CONFLICT.value(),
        LocalDateTime.now()
    ), HttpStatus.CONFLICT);
  }


  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleException(AuthenticationException e,
                                                  HttpServletRequest request) {
    return new ResponseEntity<>(new ApiError(
        e.getMessage(),
        request.getRequestURI(),
        HttpStatus.UNAUTHORIZED.value(),
        LocalDateTime.now()
    ), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException e,
                                                  HttpServletRequest request) {
    return new ResponseEntity<>(new ApiError(
        e.getMessage(),
        request.getRequestURI(),
        HttpStatus.FORBIDDEN.value(),
        LocalDateTime.now()
    ), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                             WebRequest request) {
    var bindingErrors = ex.getBindingResult().getAllErrors()
        .stream()
        .map(err -> Map.of(((FieldError) err).getField(), Objects.requireNonNull(err.getDefaultMessage())))
        .map(Map::entrySet)
        .flatMap(Set::stream)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    return new ResponseEntity<>(new ApiError(
        bindingErrors,
        request.getContextPath(),
        HttpStatus.BAD_REQUEST.value(),
        LocalDateTime.now()
    ), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleException(Exception e,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
    return new ResponseEntity<>(new ApiError(
        e.getMessage(),
        request.getRequestURI(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        LocalDateTime.now()
    ), HttpStatus.INTERNAL_SERVER_ERROR);
  }


}
