package com.tdclighthouse.prototype.utils;

import javax.jcr.RepositoryException;

public class RuntimeRepositoryException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public RuntimeRepositoryException(RepositoryException e) {
        super(e);
    }

}
