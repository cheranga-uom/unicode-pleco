package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.model.UserRole;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller("/admin/user")
public class AdminUserContrller {

    private final Logger logger = LoggerFactory.getLogger(AdminUserContrller.class);
    private final UserRepository userRepository;

    public AdminUserContrller(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @RequestMapping("/all")
    public List<User> getAllUsers(){

        return userRepository.findAll();
    }

}
