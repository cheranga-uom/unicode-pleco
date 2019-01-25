package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.Issue;
import com.ciperlabs.unicodepleco.model.IssueStatus;
import com.ciperlabs.unicodepleco.model.IssueType;
import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.repository.ConversionRepository;
import com.ciperlabs.unicodepleco.repository.IssueRepository;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import com.ciperlabs.unicodepleco.service.storage.StorageService;
import com.ciperlabs.unicodepleco.service.storage.StoredFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IssueController {

    private final Logger logger = LoggerFactory.getLogger(IssueController.class);
    private StorageService storageService;
    private IssueRepository issueRepository;
    private UserRepository userRepository;
    private AdminFileController adminFileController;
    private ConversionRepository conversionRepository;

    @Autowired
    public IssueController(StorageService storageService, IssueRepository issueRepository, UserRepository userRepository,
                           AdminFileController adminFileController, ConversionRepository conversionRepository) {
        this.storageService = storageService;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.adminFileController = adminFileController;
        this.conversionRepository = conversionRepository;
    }

    @PostMapping("/issue/submit")
    @ResponseBody
    public Map submitIssue(@RequestParam(name = "file", required = false) MultipartFile multipartFile,
                           @RequestParam(name = "issueType", required = true) String issueType,
                           @RequestParam(name = "comment", required = true) String comment, Principal principal) {

        StoredFile uploadedDocument = new StoredFile();
        Issue issue = new Issue();
        Map<String, String> map = new LinkedHashMap<>();

        if (multipartFile == null) {

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

            issue.setIssueStatus(IssueStatus.NEW);

            issue.setUser(user);
            issueRepository.save(issue);


            map.put("status", "success");
        } else {
            map.put("status", "notLoggedIn");
        }
        return map;

    }

    @PostMapping("/admin/issues/new")
    @ResponseBody
    public List getNewIssues(Principal principal) {

        User admin = AdminFilter.filter(principal, userRepository);
        if (admin == null) {
            return null;
        }
        return issueRepository.findAllByIssueStatusEquals(IssueStatus.NEW);
    }

    @PostMapping("/admin/issues/all")
    @ResponseBody
    public List<Issue> getAllIssues(Principal principal) {

        User admin = AdminFilter.filter(principal, userRepository);
        if (admin == null) {
            return null;
        }

        return issueRepository.findAll();
    }

    @PostMapping("/admin/issues/resolve")
    @ResponseBody
    public Map resolveIssue(@RequestParam("issueId") Integer issueId, Principal principal) {

        Map map = new LinkedHashMap();
        User admin = AdminFilter.filter(principal, userRepository);
        if (admin == null) {
            map.put("status", "accessDenied");
            return map;
        }
        if (issueRepository.existsById(issueId)) {
            Issue issue = issueRepository.getOne(issueId);
            issue.setIssueStatus(IssueStatus.RESOLVED);
            map.put("status", "success");
        } else {
            map.put("status", "wrongId");
        }
        return map;
    }

    @PostMapping("/admin/issues/reject")
    @ResponseBody
    public Map rejectIssue(@RequestParam("issueId") Integer issueId, Principal principal) {

        Map map = new LinkedHashMap();
        User admin = AdminFilter.filter(principal, userRepository);
        if (admin == null) {
            map.put("status", "accessDenied");
            return map;
        }
        if (issueRepository.existsById(issueId)) {
            Issue issue = issueRepository.getOne(issueId);
            issue.setIssueStatus(IssueStatus.REJECTED);
            map.put("status", "success");
        } else {
            map.put("status", "wrongId");
        }
        return map;
    }

    @GetMapping("/admin/issues/download")
    @ResponseBody
    public ResponseEntity downloadIssueFile(@PathParam("issueId") int issueId, Principal principal) {

        if (issueRepository.existsById(issueId)) {
            Issue issue = issueRepository.getOne(issueId);
            String filePath = issue.getFilePath();
            return adminFileController.serveFile(principal, filePath);
        } else {
            return null;
        }
    }

    @PostMapping("/admin/issues/user")
    @ResponseBody
    public Map getUserProfile(@RequestParam("issueId") int issueId, Principal principal) {

        Map userMap = new LinkedHashMap();
        User admin = AdminFilter.filter(principal, userRepository);
        if (admin == null) {
            return null;
        }
        if (issueRepository.existsById(issueId)) {
            Issue issue = issueRepository.getOne(issueId);

            User user = issue.getUser();
            userMap.put("userId", user.getId());
            userMap.put("name", user.getName());
            userMap.put("profileLink", user.getProfile_link());
            userMap.put("profilePicLink", user.getProfile_link());
            userMap.put("email", user.getEmail());
            userMap.put("noOfConversions", conversionRepository.countConversionByUser(user));
            return userMap;

        } else return null;
    }

    @GetMapping("/admin/user_view_by_issue")
    public String viewUserByIssue(@PathParam("issueId") int issueId, Principal principal, Model model) {
        User admin = AdminFilter.filter(principal, userRepository);
        Map userMap = new LinkedHashMap();
        if (admin == null) {
            return "redirect:/";
        }
        if (issueRepository.existsById(issueId)) {

            Issue issue = issueRepository.getOne(issueId);
            User user = issue.getUser();
            userMap.put("userId", user.getId());
            userMap.put("name", user.getName());
            userMap.put("profileLink", user.getProfile_link());
            userMap.put("profilePicLink", user.getProfile_link());
            userMap.put("email", user.getEmail());
            userMap.put("noOfConversions", conversionRepository.countConversionByUser(user));
            model.addAttribute("user", userMap);
            return "admin/user_by_issue";
        } else return null;

    }
}
