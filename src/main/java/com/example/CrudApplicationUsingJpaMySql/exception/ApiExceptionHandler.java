package com.example.CrudApplicationUsingJpaMySql.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends Exception{
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,WebRequest request) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
        for(FieldError fieldError: fieldErrors){
            sb.append(fieldError.getDefaultMessage());
            sb.append(" ");
        }
        ApiException validationError =
                new ApiException("Validation failed",sb.toString(),HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(validationError,HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(ConstraintViolationException.class)
    public  ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,WebRequest request) {
        List<String> errorMessages = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ApiException validationError =
                new ApiException("Validation failed",errorMessages.toString() , HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(validationError,HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiException> handleApiException(
            EmailAlreadyExistsException ex) {
        ApiException response =
                new ApiException("User already exists for this email",
                        "Enter a new email address",HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiException> handleApiException(
            UsernameAlreadyExistsException ex) {
        ApiException response =
                new ApiException("This username is already in use",
                        "Please choose another one.",HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAndPasswordDoNotMatch.class)
    public ResponseEntity<ApiException> handleApiException(
            EmailAndPasswordDoNotMatch ex) {
        ApiException response =
                new ApiException("Email and password does not match",
                        "Enter correct email and password",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ApiException> handleApiException(
            NotAuthorizedException ex) {
        ApiException response =
                new ApiException("You are not authorized for this page",null,HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(ExpirationNotValidException.class)
    public ResponseEntity<ApiException> handleApiException(
            ExpirationNotValidException ex) {
        ApiException response =
                new ApiException("This link is expired, go to resent email page.",null,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ApiException> handleApiException(
            EmailNotVerifiedException ex) {
        ApiException response =
                new ApiException("To access this page, first you must verify your email.",null,HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UserNotActivatedException.class)
    public ResponseEntity<ApiException> handleApiException(
            UserNotActivatedException ex) {
        ApiException response =
                new ApiException("Your account is not activated.","You must set your password first.",HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordsDontMatchException.class)
    public ResponseEntity<ApiException> handleApiException(
            PasswordsDontMatchException ex) {
        ApiException response =
                new ApiException("Passwords do not match",
                        "Confirm password must be same as password", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CodeDoesNotMatchException.class)
    public ResponseEntity<ApiException> handleApiException(
            CodeDoesNotMatchException ex) {
        ApiException response =
                new ApiException("Code is wrong or you already used this code to set your password.",
                        "Make sure you write the right code from your email you recieved.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailDoesNotExistException.class)
    public ResponseEntity<ApiException> handleApiException(
            EmailDoesNotExistException ex) {
        ApiException response =
                new ApiException("No registration found for this email",
                        "Make sure you typed your email correctly", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
