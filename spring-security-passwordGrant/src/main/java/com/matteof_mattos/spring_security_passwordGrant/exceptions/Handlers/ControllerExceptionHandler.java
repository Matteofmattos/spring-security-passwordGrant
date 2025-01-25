package com.matteof_mattos.spring_security_passwordGrant.exceptions.Handlers;

import com.matteof_mattos.spring_security_passwordGrant.dto.CustomError;
import com.matteof_mattos.spring_security_passwordGrant.dto.CustomErrorValidation;
import com.matteof_mattos.spring_security_passwordGrant.dto.FieldMessage;
import com.matteof_mattos.spring_security_passwordGrant.exceptions.DatabaseException;
import com.matteof_mattos.spring_security_passwordGrant.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFoundException(ResourceNotFoundException exc, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        CustomError customError = new CustomError(Instant.now(), status.value(),
                exc.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(customError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomError> DataBaseException(DatabaseException exc, HttpServletRequest request){

        HttpStatus conflict = HttpStatus.CONFLICT;

        CustomError customError = new CustomError(Instant.now(), conflict.value(), exc.getMessage(), request.getRequestURI());

        return ResponseEntity.status(conflict).body(customError);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorValidation> methodArgumentNotValidExceptions(MethodArgumentNotValidException exc, HttpServletRequest request){

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        List<FieldMessage> fieldMessageList = new ArrayList<>();

        exc.getBindingResult().getFieldErrors().forEach(fieldError -> {
            fieldMessageList.add(new FieldMessage(fieldError.getField(),fieldError.getDefaultMessage()));
        });


        return ResponseEntity.status(status).body(new CustomErrorValidation(Instant.now(),
                status.value(),
                "# Dados inválidos.",
                request.getRequestURI(),fieldMessageList));
    }
}
