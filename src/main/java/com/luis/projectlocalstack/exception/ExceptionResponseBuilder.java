package com.luis.projectlocalstack.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExceptionResponseBuilder {

    private final MessageSource messageSource;

    public ExceptionResponseWrapper build(ServiceException exception) {
        return ExceptionResponseWrapper.builder()
                .message(messageSource.getMessage(
                        exception.getLabel(), exception.getDetails(), exception.getLabel(), Locale.getDefault()))
                .timestamp(Calendar.getInstance().getTime())
                .build();
    }
}
