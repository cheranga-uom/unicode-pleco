package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.Issue;
import com.ciperlabs.unicodepleco.model.IssueType;
import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.repository.IssueRepository;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import com.ciperlabs.unicodepleco.service.storage.StorageService;
import com.ciperlabs.unicodepleco.service.storage.StoredFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class IssueController {

    private final Logger logger = LoggerFactory.getLogger(IssueController.class);
    private StorageService storageService;
    private IssueRepository issueRepository;
    private UserRepository userRepository;

    @Autowired
    public IssueController(StorageService storageService, IssueRepository issueRepository, UserRepository userRepository) {
        this.storageService = storageService;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/issue/submit")
    @ResponseBody
    public Map submitIssue(@RequestParam(name = "file", required = false) MultipartFile multipartFile,
                           @RequestParam(name = "issueType", required = true) String issueType,
                           @RequestParam(name = "comment", required = true) String comment, Principal principal) {

        StoredFile uploadedDocument = new StoredFile();
        Issue issue = new Issue();
        Map<String, String> map = new LinkedHashMap<>();

        if (!multipartFile.isEmpty()) {

            if (issueType.equalsIgnoreCase(IssueType.SUGGESTION + "")) {

                uploadedDocument = storageService.store(multipartFile, "issue/suggestion/");

                issue.setIssueType(IssueType.SUGGESTION);
            } else if (issueType.equalsIgnoreCase(IssueType.ISSUE + "")) {

                uploadedDocument = storageService.store(multipartFile, "issue/issue/");
                issue.setIssueType(IssueType.ISSUE);
            }
        } else {
            logger.info("No Input Files are detected");
        }
        issue.setComment(comment);
        issue.setFileName(uploadedDocument.getFileName());
        issue.setFilePath(uploadedDocument.getPath());

        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();

            Long userId = Long.valueOf(details.get("id") + "");
            User user = userRepository.getOne(userId);

            issue.setUser(user);
            issueRepository.save(issue);


            map.put("status", "success");
        } else {
            map.put("status", "notLoggedIn");
        }
        return map;

    }

}
