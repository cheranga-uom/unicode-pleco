package com.ciperlabs.unicodepleco.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RestUserIDController {
    @RequestMapping({"/user", "/me"})
    public Map<String, String> user(Principal principal) {
        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();
//            logger.info("details = " + details);  // id, email, name, link etc.
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", details.get("name"));
            return map;
        }
        return null;
    }

    @GetMapping("/test")
    @ResponseBody
    public int uploadForm(@RequestParam(name = "name", required = true) int name,Principal principal){

        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();
            System.out.println("details = " + details);  // id, email, name, link etc.
            Map<String, String> map = new LinkedHashMap<>();
        }
        return name;
    }
}
