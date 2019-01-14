package com.ciperlabs.unicodepleco.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UIcontroller {


    Logger logger = LoggerFactory.getLogger(UIcontroller.class);

    @GetMapping("/")
    public String index() {
        return "index.html";
    }


    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        logger.error(name);
        return "greeting.html";
    }

    @GetMapping("/header")
    public String header() {

        return "master/header.html";
    }

    //    @RequestMapping("/test2")
//    @SuppressWarnings("unchecked")
//    public Map<String, String> user2(Principal principal) {
//        if (principal != null) {
//            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
//            Authentication authentication = oAuth2Authentication.getUserAuthentication();
//            Map<String, String> details = new LinkedHashMap<>();
//            details = (Map<String, String>) authentication.getDetails();
//            logger.info("details = " + details);  // id, email, name, link etc.
//            Map<String, String> map = new LinkedHashMap<>();
//            map.put("email", details.get("email"));
//            return map;
//        }
//        return null;
//    }

}
