package com.internship.deltasmartsoftware.config;

import com.internship.deltasmartsoftware.exceptions.BadRequestException;
import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.payload.requests.ApiErrorRequest;
import com.internship.deltasmartsoftware.payload.responses.Response;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Response<Object>> badCredentialsException(){
        return Response.unauthorized("error.unauthorized");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<Object>> resourceNotFoundException(){
        return Response.notFound("error.notFound");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<Object>> illegalArgumentException(){
        return Response.badRequest("error.badRequest");
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<Object>> resourceNotFoundException(BadRequestException e){
        return Response.badRequest(e.getMessage());
    }
}
