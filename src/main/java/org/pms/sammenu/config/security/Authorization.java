package org.pms.sammenu.config.security;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.exceptions.AuthorizationFailedException;
import org.springframework.util.ObjectUtils;

import java.security.Principal;

@Slf4j
public class Authorization {

    public static void authorizeRequest(String username, Principal principal){

        if(ObjectUtils.isEmpty(principal) || !principal.getName().equals(username))
            throw new AuthorizationFailedException("Unauthorized for this action");

        log.info("username: {} , principal: {}", username, principal.getName());
    }
}
