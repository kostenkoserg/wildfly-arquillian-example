package com.fasto.datamanager.exception;

/**
 *
 * @author kostenko
 */
public class PlayerRegistrationException extends Exception {
    
    public enum CODE {
        EMAIL_EXIST,  
        ALIAS_EXIST
    }
    
    private final CODE code;
    
    public PlayerRegistrationException(CODE errorCode, String message) {
        super(message);
        this.code = errorCode;
    }

    public CODE getCode() {
        return code;
    }
}
