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

    private StatisticsHandler statisticsHandler;

    public AdminUIController(UserRepository userRepository, StatisticsHandler statisticsHandler){
        this.userRepository = userRepository;
        this.statisticsHandler = statisticsHandler;
    }
    @GetMapping("/admin")
    public String getAdminHome(Model model, Principal principal){
//
//        User admin = adminFilter(principal);
//        if(admin == null){
//            return "redirect:/";
//        }
        Integer conversionsOfToday = statisticsHandler.conversionsForToday();
        Integer conversionsOfWeek = statisticsHandler.conversionsForWeek();
        Integer conversionsOfMonth = statisticsHandler.conversionsOfTheMonth();
        Integer conversionsOfYear = statisticsHandler.conversionsOfTheYear();
        Long totalConversions = statisticsHandler.totalConversions();

        Integer usersJoinedToday = statisticsHandler.usersJoinedToday();
        Integer usersForWeek = statisticsHandler.usersForWeek();
        Integer usersForMonth = statisticsHandler.usersForWeek();
        Integer usersForYear = statisticsHandler.usersForYear();
        Long totalUsers = statisticsHandler.totalUsers();

        logger.info("conversions count for today,week,month,year,total : " + conversionsOfToday + " , " + conversionsOfWeek+ " , " +
                conversionsOfMonth+ " , "+conversionsOfYear+ " , "+totalConversions);
        logger.info("users count for today,week,month,year,total : "+ usersJoinedToday+ " , "+usersForWeek+ " , "+ usersForMonth
                + " , " +usersForYear+ " , "+ totalUsers );

        model.addAttribute("conversionsOfToday",conversionsOfToday);
        model.addAttribute("conversionsOfWeek",conversionsOfWeek);
        model.addAttribute("conversionsOfMonth",conversionsOfMonth);
        model.addAttribute("conversionsOfYear",conversionsOfYear);
        model.addAttribute("totalConversions",totalConversions);

        model.addAttribute("usersJoinedToday",usersJoinedToday);
        model.addAttribute("usersForWeek",usersForWeek);
        model.addAttribute("usersForMonth",usersForWeek);
        model.addAttribute("usersForYear",usersForYear);
        model.addAttribute("totalUsers",totalUsers);

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
