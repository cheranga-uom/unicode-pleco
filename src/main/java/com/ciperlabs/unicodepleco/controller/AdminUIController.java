package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.repository.IssueRepository;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminUIController {
    Logger logger = LoggerFactory.getLogger(AdminUIController.class);

    private UserRepository userRepository;

    private StatisticsHandler statisticsHandler;

    private IssueRepository issueRepository;

    public AdminUIController(UserRepository userRepository, StatisticsHandler statisticsHandler, IssueRepository issueRepository) {
        this.userRepository = userRepository;
        this.statisticsHandler = statisticsHandler;
        this.issueRepository = issueRepository;
    }
    @GetMapping("/admin")
    public String getAdminHome(Model model, Principal principal){

//        User admin = AdminFilter.filter(principal, userRepository);
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

        User admin = AdminFilter.filter(principal, userRepository);
        if(admin == null){
            return "redirect:/";
        }

        return "admin/files.html";
    }

    @GetMapping("admin/issues")
    public String getIssues(Model model, Principal principal) {
        User admin = AdminFilter.filter(principal, userRepository);
        if (admin == null) {
            return "redirect:/";
        }
        List issues = issueRepository.findAll();

        logger.info("issues : " + issues);
        model.addAttribute("issues", issues);

        return "admin/issues.html";

    }

//    @GetMapping("/admin/users")
//    public String getUserProfiles(Model model, Principal principal){
//
//        User admin = AdminFilter.filter(principal,userRepository);
//        if(admin == null){
//            return "redirect:/";
//        }
//
//
//        return "admin/users.html";
//    }


}
