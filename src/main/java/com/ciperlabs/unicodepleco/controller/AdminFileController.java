package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.Conversion;
import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.model.UserRole;
import com.ciperlabs.unicodepleco.repository.ConversionRepository;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import com.ciperlabs.unicodepleco.service.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminFileController {

    private final Logger logger = LoggerFactory.getLogger(AdminFileController.class);

    private final StorageService storageService;
    private final ConversionRepository conversionRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminFileController(StorageService storageService, ConversionRepository conversionRepository, UserRepository userRepository) {
        this.storageService = storageService;
        this.conversionRepository = conversionRepository;
        this.userRepository = userRepository;
    }


    private ResponseEntity serveFile(Principal principal, String filePath){
        logger.info("Download FileName : " + filePath);

        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();
            logger.info("details = " + details);  // id, email, name, link etc.

            Long id = Long.valueOf(details.get("id"));
            User user = userRepository.getOne(id);

            if (user.getRole() != UserRole.SUPER_ADMIN || user.getRole() != UserRole.ADMIN){
                Map<String, String> response = new LinkedHashMap<>();
                response.put("status","UserNotAllowed");
                return ResponseEntity.badRequest().body(response);            }

            Resource resource = storageService.loadAsResource(filePath);
            System.out.println("Attaching file to download user unknown");
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
        } else {
            Map<String, String> response = new LinkedHashMap<>();
            response.put("status","notLoggedIn");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/download/converted")
    @ResponseBody
    public ResponseEntity downloadConvertedFile(@PathParam("conversionId") int conversionId
            , Principal principal) {
        System.out.println(conversionId);
        if (!conversionRepository.findById(conversionId).isPresent()) {

            Map<String, String> response = new LinkedHashMap<>();
            response.put("status","InvalidFile");
            return ResponseEntity.badRequest().body(response);        }
        Conversion conversion = conversionRepository.findById(conversionId).get();

        String filePath = conversion.getOutputFilePath();

        return serveFile(principal, filePath);

    }

    @GetMapping("/download/original")
    @ResponseBody
    public ResponseEntity downloadOriginalFile(@PathParam("conversionId") int convertionId, Principal principal) {


        System.out.println(convertionId);
        if (!conversionRepository.findById(convertionId).isPresent()) {

            Map<String, String> response = new LinkedHashMap<>();
            response.put("status","InvalidFile");
            return ResponseEntity.badRequest().body(response);        }
        Conversion conversion = conversionRepository.findById(convertionId).get();

        String filePath = conversion.getInputFilePath();

        return serveFile(principal, filePath);

    }
    @PostMapping("/history/user")
     public List<Conversion> getHistoryOfUser(@RequestParam("userId") Long userId, Principal principal){

//        User admin = AdminFilter.filter(principal, userRepository);
//        if(admin == null){
//            return null;
//        }

        if (userRepository.existsById(userId)){

            return userRepository.getOne(userId).getConversions();
        }
        return null;
    }

    @GetMapping("/history/user")
    public String getHistoryOfUserPage(@PathParam("userId") Long userId, Model model, Principal principal){

//        User admin = AdminFilter.filter(principal, userRepository);
//        if(admin == null){
//            return "redirect:/;
//        }

        if (userRepository.existsById(userId)){

            List<Conversion> conversions = userRepository.getOne(userId).getConversions();
            logger.info("Coversions by User : "+userId +" : " + conversions);
            model.addAllAttributes(conversions);

            return "admin/files";
        }
        return "admin/error-404";
    }

    @RequestMapping("/history/month")
    public List<Conversion> getHistoryOfMonth(@RequestParam("month")LocalDateTime month, Principal principal){

//        User admin = AdminFilter.filter(principal, userRepository);
//        if(admin == null){
//            return null;
//        }
        //TODO fix next month
        LocalDateTime nextMonth = month.plusMonths(1);
        return  conversionRepository.findConversionByCreatedTimeAfterAndCreatedTimeBefore(month,nextMonth);

    }

}
