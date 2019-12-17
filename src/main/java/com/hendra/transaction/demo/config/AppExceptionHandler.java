package com.hendra.transaction.demo.config;

import com.hendra.transaction.demo.exception.NoContentException;
import com.hendra.transaction.demo.exception.NotFoundException;
import com.hendra.transaction.demo.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler
{
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity noContent()
    {
        return new ResponseEntity(new Response("204", HttpStatus.NO_CONTENT.toString()), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFound()
    {
        return new ResponseEntity(new Response("404", HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
    }
}
