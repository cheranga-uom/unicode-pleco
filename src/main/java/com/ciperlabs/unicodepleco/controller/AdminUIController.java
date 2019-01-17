package com.ciperlabs.unicodepleco.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUIController {
    Logger logger = LoggerFactory.getLogger(AdminUIController.class);


    @GetMapping("/admin")
    public String getAdminHome(Model model){

        return "admin/dashboard.html";
    }

    @GetMapping("/admin/files")
    public String getAdminFiles(Model model){

        return "admin/files.html";
    }

    @GetMapping("/admin/profiles")
    public String getUserProfiles(Model model){

        return "admin/profiles.html";
    }

    @GetMapping("/test")
    public String getContentPage(Model model){
        return "admin/content.html";
    }



}
