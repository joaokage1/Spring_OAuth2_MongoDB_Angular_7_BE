package com.toddy.ws.services.exception;

import java.io.Serializable;

public class ObjectNotEnabledException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public ObjectNotEnabledException(String message){
        super(message);
    }

}
