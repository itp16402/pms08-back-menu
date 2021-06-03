package org.pms.sammenu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
public class AssignedUserException extends RuntimeException {

    public AssignedUserException(String message) {
        super(message);
    }

    public AssignedUserException(String message, Exception e) {
        super(message, e);
    }
}
