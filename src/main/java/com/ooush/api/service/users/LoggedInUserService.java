package com.ooush.api.service.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ooush.api.entity.Users;
import com.ooush.api.repository.UserRepository;

@Service("LoggedInUserService")
@Transactional
public class LoggedInUserService extends AbstractUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggedInUserService.class);

    @Autowired
    private UserRepository userRepository;

    public Users getCurrentLoggedInUser() {
        UserDetails userDetails = getLoggedInUserDetails();
        return userDetails == null ? null : userRepository.findByUserName(userDetails.getUsername());
    }

}
