package com.toddy.ws.services.exception;

import java.io.Serializable;

public class ObjectAlreadyExistsException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public ObjectAlreadyExistsException(String message){
        super(message);
    }

}
