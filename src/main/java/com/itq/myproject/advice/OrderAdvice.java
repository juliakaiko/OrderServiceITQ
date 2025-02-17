package com.itq.myproject.advice;

import com.itq.myproject.annotation.MyExceptionHandler;
import com.itq.myproject.util.ErrorItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice(annotations = MyExceptionHandler.class)
public class OrderAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorItem> handleException(Exception e) {
        ErrorItem error = generateMessage(e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    public ErrorItem generateMessage(Exception e){
        ErrorItem error = new ErrorItem();
        error.setTimestamp(formatDate());
        error.setMessage(e.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return error;
    }

    public String formatDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String data = dateTimeFormatter.format( LocalDateTime.now() );
        return data;
    }
}
