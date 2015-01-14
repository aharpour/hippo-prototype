package com.tdclighthouse.prototype.utils.exceptions;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RuntimeIOException(IOException e) {
        super(e.getMessage(), e);
    }
}
