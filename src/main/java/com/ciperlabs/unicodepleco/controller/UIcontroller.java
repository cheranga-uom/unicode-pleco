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
    public String index(Model model) {
        return "user/index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "user/about";
    }

    @GetMapping("/report_issue")
    public String reportIssue(Model model) {
        return "user/reportIssue";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        logger.error(name);
        return "greeting.html";
    }


}
