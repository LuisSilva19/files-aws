package com.luis.projectlocalstack.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {
    private final String label;
    private final Object[] details;

    public ServiceException(final String label, final Object... details) {
        super(label);
        this.label = label;
        this.details = details;
    }

    public ServiceException(final String label) {
        super(label);
        this.label = label;
        this.details = new Object[0];
    }
}
