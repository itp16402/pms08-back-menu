package org.pms.sammenu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizationFailedException extends RuntimeException {

    public AuthorizationFailedException(String message) {
        super(message);
    }

    public AuthorizationFailedException(String message, Exception e) {
        super(message, e);
    }
}
