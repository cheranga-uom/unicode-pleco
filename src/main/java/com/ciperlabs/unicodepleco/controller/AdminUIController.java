package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.model.UserRole;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class AdminUIController {
    Logger logger = LoggerFactory.getLogger(AdminUIController.class);

    private UserRepository userRepository;

    private DetailsHandler detailsHandler;

    public AdminUIController(UserRepository userRepository, DetailsHandler detailsHandler){
        this.userRepository = userRepository;
        this.detailsHandler = detailsHandler;
    }
    @GetMapping("/admin")
    public String getAdminHome(Model mode, Principal principal){
//
//        User admin = adminFilter(principal);
//        if(admin == null){
//            return "redirect:/";
//        }

        logger.info("conversions count for this week : "+ detailsHandler.conversionsForWeek());

        return "admin/dashboard.html";
    }

    @GetMapping("/admin/files")
    public String getAdminFiles(Model model, Principal principal){

        User admin = adminFilter(principal);
        if(admin == null){
            return "redirect:/";
        }

        return "admin/files.html";
    }

    @GetMapping("/admin/profiles")
    public String getUserProfiles(Model model, Principal principal){

        User admin = adminFilter(principal);
        if(admin == null){
            return "redirect:/";
        }


        return "admin/profiles.html";
    }




    public User adminFilter(Principal principal) {
        if (principal != null) {

            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>> details = new LinkedHashMap<>();
            details = (Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>>) authentication.getDetails();

            Long id = Long.valueOf(details.get("id")+"");
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
