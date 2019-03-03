package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.VisitorCount;
import com.ciperlabs.unicodepleco.repository.VisitorCountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class VisitorController {

    Logger logger = LoggerFactory.getLogger(VisitorController.class);
    @Autowired
    private VisitorCountRepository visitorCountRepository;

    @RequestMapping("/visitor")
    @ResponseBody
    public Map getVisitors(@RequestParam("newVisitor") boolean newVisitor) {

        Map visitorData = new LinkedHashMap();

        if (newVisitor) {

            VisitorCount visitorCount = new VisitorCount();
            visitorCountRepository.save(visitorCount);

            logger.info("Visitor count increased");
            visitorData.put("visitorCount", visitorCountRepository.count());

        } else {
            logger.info("Visitor count not increased");

            visitorData.put("visitorCount", visitorCountRepository.count());
        }

        return visitorData;
    }
}
