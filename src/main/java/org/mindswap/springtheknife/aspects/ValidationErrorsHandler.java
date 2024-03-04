package org.mindswap.springtheknife.aspects;

import jakarta.validation.ConstraintViolationException;
import org.mindswap.springtheknife.utils.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ValidationErrorsHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String,String> handleArgumentNotValid(MethodArgumentNotValidException ex){

        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;

    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(Exception ex) {

        return new ResponseEntity<>(Message.VALID_DATE, HttpStatus.BAD_REQUEST);
    }
}
