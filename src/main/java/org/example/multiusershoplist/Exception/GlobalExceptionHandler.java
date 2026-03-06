package org.example.multiusershoplist.Exception;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ NullPointerException.class,NoSuchMethodException.class, InvocationTargetException.class, IllegalAccessException.class, HibernateException.class, DataIntegrityViolationException.class, HttpRequestMethodNotSupportedException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleNotUniqDataException(Exception e) {
        logger.error(e.getMessage());
        return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class,UsernameNotFoundException.class})
    public ResponseEntity<String> handleException(Exception e) {
        logger.error(e.getMessage());
        return  new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class, SignatureException.class, SignatureException.class})
    public ResponseEntity<String> handleUnauthorizedException(Exception e) {
        logger.error(e.getMessage());
        return  new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }









}