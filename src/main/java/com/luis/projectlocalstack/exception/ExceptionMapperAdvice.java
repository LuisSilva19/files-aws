package com.luis.projectlocalstack.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionMapperAdvice {

    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionResponseWrapper handleServiceException(ServiceException e) {
        log.error("ServiceException", e);
        return exceptionResponseBuilder.build(e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ExceptionResponseWrapper handle(RuntimeException e) {
        log.error("RuntimeException", e);
        return ExceptionResponseWrapper.builder().message(e.getMessage()).build();
    }
}
