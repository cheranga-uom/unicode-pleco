package com.ciperlabs.unicodepleco.controller;


import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.model.UserRole;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RestUserIDController {

    @Autowired
    private UserRepository userRepository;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RestUserIDController.class);
    @RequestMapping({"/user", "/me"})
    public Map<String, String> user(Principal principal) {
        if (principal != null) {

            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, LinkedHashMap<String,LinkedHashMap<String,String>>> details = new LinkedHashMap<>();
            details = (Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>>) authentication.getDetails();
            logger.info("details = " + details);  // id, email, name, link etc.

            String name = details.get("name") + "";
            Long id = Long.valueOf(details.get("id")+"");
            String profilePictureUrl = details.get("picture").get("data").get("url");
            String email = details.get("email")+"";

            Map<String, String> map = new LinkedHashMap<>();

            map.put("name",name );
            map.put("picture",profilePictureUrl);
            map.put("email",email);

            User userModel = new User();
            if (userRepository.existsById(id)){
                userModel = userRepository.getOne(id);
            }
            else{
                userModel.setEmail(email);
                userModel.setId(id);
                userModel.setName(name);
                userModel.setProfilePictureurl(profilePictureUrl);
                userModel.setRole(UserRole.USER);
                userRepository.save(userModel);
            }
            map.put("role",userModel.getRole()+"");

            return map;
        }
        return null;
    }

}
