package com.nastinka_krd.user_banking_service.exception;

import com.nastinka_krd.user_banking_service.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;

@RestControllerAdvice(basePackages = "com.nastinka_krd.user_banking_service.controller")
public class HandlerExceptions {

    @ExceptionHandler(value = {DataIsNotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataIsNotFoundException(@NonNull HttpServletRequest request, DataIsNotFoundException exception){
        return new ErrorResponse(request.getRequestURI(), exception.getMessage());
    }

    @ExceptionHandler(value = {NotEnoughMoneyException.class})
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleNotEnoughMoneyException(@NonNull HttpServletRequest request, NotEnoughMoneyException exception){
        return new ErrorResponse(request.getRequestURI(), exception.getMessage());
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(@NonNull HttpServletRequest request, ValidationException exception){
        return new ErrorResponse(request.getRequestURI(), exception.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(@NonNull HttpServletRequest request, Exception exception){
        return new ErrorResponse(request.getRequestURI(), exception.getMessage());
    }
}
