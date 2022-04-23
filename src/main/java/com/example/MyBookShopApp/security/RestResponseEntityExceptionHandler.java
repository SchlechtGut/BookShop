package com.example.MyBookShopApp.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.logging.Logger;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { UsernameNotFoundException.class })
    protected ResponseEntity<Void> handle(UsernameNotFoundException ex, WebRequest request) {
        Logger.getLogger("RestResponseEntityExceptionHandler").info("Caught UsernameNotFoundException");
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
    }
}
