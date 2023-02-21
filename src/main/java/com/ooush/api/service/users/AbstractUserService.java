package com.ooush.api.service.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AbstractUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUserService.class);

    static UserDetails getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                return (UserDetails) authentication.getPrincipal();
            }
            catch(ClassCastException e) {
                LOGGER.error("Failed to cast authentication principal to UserDetails: {}", authentication.getPrincipal());
            }
        }
        return null;
    }

}
