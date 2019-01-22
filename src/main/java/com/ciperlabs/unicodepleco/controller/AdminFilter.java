package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.model.UserRole;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminFilter {

    private static final Logger logger = LoggerFactory.getLogger(AdminFilter.class);

    public static User filter(Principal principal, UserRepository userRepository){

        if (principal != null) {

            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();

            Long id = Long.valueOf(details.get("id"));
            logger.info("User id : " + id);
            User user = userRepository.getOne(id);
            if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.SUPER_ADMIN){
                return user;
            }

            return null;
        }
        return null;
    }
}
