package com.contenttree.CampaignManager;

import com.contenttree.admin.Admin;
import com.contenttree.admin.AdminRepository;
import com.contenttree.admin.AdminService;
import com.contenttree.admin.Role;
import com.contenttree.category.Category;
import com.contenttree.category.CategoryRepository;
import com.contenttree.category.CategoryService;
import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.user.EmailService;
import com.contenttree.user.User;
import com.contenttree.user.UserRepository;
import com.contenttree.user.UserService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import io.swagger.annotations.Api;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/campaign-manager")
public class CampaignManagerController {
//    @Autowired
//    AdminService adminService;
//    @Autowired
//    AdminRepository adminRepository;
//
//    @Autowired
//    CategoryService categoryService;
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Autowired
//    SolutionSetsService solutionSetsServicel;
//    @Autowired
//    CampaignReposiotry campaignReposiotry;
//    @Autowired
//    WebinarRepository webinarRepository;
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    EmailService emailService;
//
//    @Autowired
//    EventRepository eventRepository;
//    @GetMapping
//    public ResponseEntity<ApiResponse1<Map<?, ?>>> getAllCampaignManagerDetails(@AuthenticationPrincipal Admin admin) {
//
//        System.out.println("Admin roles: " + admin.getRole());
//
//        if (admin.getRole() == null || !admin.getRole().contains(Role.CAMPAIGNMANAGER)) {
//            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "Unauthorized access", false));
//        }
//
//        List<Webinar> webinarList = webinarRepository.findByAdminId(admin.getId());
//        List<Event> eventList = eventRepository.findByAdminId(admin.getId());
//
//        Map<Object, Object> map = new HashMap<>();
//        map.put("webinar", webinarList);
//        map.put("event", eventList);
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
//    }
//
//    @PostMapping("/add-event")
//    public ResponseEntity<ApiResponse1<Event>> addEvent(@RequestParam String eventName,
//                                                        @RequestParam String eventAddress,
//                                                        @RequestParam String eventLink,
//                                                        @RequestParam List<String> eventSpeakers,
//                                                        @RequestParam String eventSubject, @AuthenticationPrincipal Admin admin) {
//        if (admin.getRole() == null || !admin.getRole().contains(Role.CAMPAIGNMANAGER)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(ResponseUtils.createResponse1(null, "Unauthorized access", false));
//        }
//
//        Event event = new Event();
//        event.setEventName(eventName);
//        event.setEventAddress(eventAddress);
//        event.setEventLink(eventLink);
//        event.setEventSpeakers(eventSpeakers);
//        event.setEventSubject(eventSubject);
////        event.setAdmin(admin);
//        eventRepository.save(event);
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(event, "SUCCESS", true));
//    }
//    @PostMapping("/add-webinar")
//    public ResponseEntity<ApiResponse1<Webinar>> addWebinar(@RequestParam String webinarName,
//                                                            @RequestParam String webinarSubject,
//                                                            @RequestParam List<String> webinarSpeakers,
//                                                            @RequestParam String liveLink, @AuthenticationPrincipal Admin admin) {
//        if (admin.getRole() == null || !admin.getRole().contains(Role.CAMPAIGNMANAGER)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(ResponseUtils.createResponse1(null, "Unauthorized access", false));
//        }
//        Webinar webinar = new Webinar();
//        webinar.setWebinarName(webinarName);
//        webinar.setWebinarSubject(webinarSubject);
//        webinar.setWebinarSpeakers(webinarSpeakers);
//        webinar.setLiveLink(liveLink);
//        webinar.setAdmin(admin);
//
//        webinarRepository.save(webinar);
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(webinar, "SUCCESS", true));
//    }
//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse1<Campaign>> addCampaign(
//            @RequestParam String name,
//            @RequestParam Long categoryId,
//            @RequestParam String startDate,
//            @RequestParam String endDate) {
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//        LocalDate start = LocalDate.parse(startDate, formatter);
//        LocalDateTime startDateTime = start.atStartOfDay();
//
//        LocalDate end = LocalDate.parse(endDate, formatter);
//        LocalDateTime endDateTime = end.atTime(23, 59, 59);
//
//        Category category = categoryRepository.findById(categoryId).orElse(null);
//        Campaign campaign = new Campaign();
//        campaign.setName(name);
//        campaign.setCategory(category);
//        campaign.setStartDate(startDateTime);
//        campaign.setEndDate(endDateTime);
//        campaign.setStatus(CampaignStatus.Hold);
//
//        campaignReposiotry.save(campaign);
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaign, "SUCCESS", true));
//    }
//
//    @PutMapping("/active-campaign")
//    public ResponseEntity<ApiResponse1<Campaign>> activeCampaign(@RequestParam long id){
//        Campaign campaign = campaignReposiotry.findById(id).orElse(null);
//        campaign.setStatus(CampaignStatus.Active);
//        campaignReposiotry.save(campaign);
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaign,"SUCCESS",true));
//    }
//    @PutMapping("/unactive-campaign")
//    public ResponseEntity<ApiResponse1<Campaign>> rejectCampaign(@RequestParam long id){
//        Campaign campaign = campaignReposiotry.findById(id).orElse(null);
//        campaign.setStatus(CampaignStatus.NotActive);
//        campaignReposiotry.save(campaign);
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaign,"SUCCESS",true));
//    }
//    private static final String logoUrl = "https://infinitydemand.com/wp-content/uploads/2024/01/cropped-Logo-22.png";
//
//    private static final String whitepaperUrl="https://infiniteb2b.com";
//    @PostMapping("/send-mail-all-user")
//    public ResponseEntity<ApiResponse1<String>> sendMailToAllUser(@RequestParam long solutionSetId,
//                                                                  @RequestParam String subject) throws MessagingException {
//        List<User> userList = userService.getAllUsers();
//        SolutionSets solutionSets = solutionSetsServicel.getSolutionSetById(solutionSetId);
//        String path = solutionSets.getFilePath();
//
//        Optional<Category> category = categoryService.getCategoryBySolutionSet(solutionSetId);
//
//        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
//                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
//                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
//                "</div>" +
//                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released!</h2>" +
//                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
//                "We are excited to share the latest updates from <strong>InfiniteB2B</strong>! Our new whitepapers on <strong>" + category.get().getName() + "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
//                "</p>" +
//                "<div style=\"text-align: center; margin: 30px 0;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
//                "</div>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
//                "</p>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
//                "</p>" +
//                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
//                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
//                "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
//                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
//                "</footer>" +
//                "</div>";
//        for (User user : userList){
//            emailService.sendHtmlEmailWithAttachment(user.getEmail(), subject,htmlMessage,path);
//        }
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"SUCCESS",true));
//
//    }
//
//    @Autowired
//    UserRepository userRepository;
//    @PostMapping("/send-mail-subscriber")
//    public ResponseEntity<ApiResponse1<String>> sendMailToAllSubscriber(@RequestParam long solutionSetId,
//                                                                  @RequestParam String subject) throws MessagingException {
//        List<User> subscribedUsers = userRepository.findAll().stream()
//                .filter(user -> user.isEnabled() && user.getIsSubscriber() == 1)
//                .toList();
//        SolutionSets solutionSets = solutionSetsServicel.getSolutionSetById(solutionSetId);
//        Optional<Category> category = categoryService.getCategoryBySolutionSet(solutionSetId);
//        String path = solutionSets.getFilePath();
//        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
//                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
//                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
//                "</div>" +
//                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released!</h2>" +
//                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
//                "We are excited to share the latest updates from <strong>InfiniteB2B</strong>! Our new whitepapers on <strong>" + category.get().getName() + "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
//                "</p>" +
//                "<div style=\"text-align: center; margin: 30px 0;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
//                "</div>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
//                "</p>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
//                "</p>" +
//                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
//                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
//                "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
//                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
//                "</footer>" +
//                "</div>";
//
//
//        for (User user : subscribedUsers){
//            emailService.sendHtmlEmailWithAttachment(user.getEmail(), subject,htmlMessage,path);
//        }
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"SUCCESS",true));
//
//    }
//    @PostMapping("/send-mail-all-unsubscriber")
//    public ResponseEntity<ApiResponse1<String>> sendMailToAllUnSubsriver(@RequestParam long solutionSetId,
//                                                                  @RequestParam String subject) throws MessagingException {
//        List<User> subscribedUsers = userRepository.findAll().stream()
//                .filter(user -> user.isEnabled() && user.getIsSubscriber() == 1)
//                .toList();
//        SolutionSets solutionSets = solutionSetsServicel.getSolutionSetById(solutionSetId);
//        String path = solutionSets.getFilePath();
//        Optional<Category> category = categoryService.getCategoryBySolutionSet(solutionSetId);
//
//        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
//                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
//                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
//                "</div>" +
//                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released!</h2>" +
//                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
//                "We are excited to share the latest updates from <strong>InfiniteB2B</strong>! Our new whitepapers on <strong>" + category.get().getName()+ "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
//                "</p>" +
//                "<div style=\"text-align: center; margin: 30px 0;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
//                "</div>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
//                "</p>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
//                "</p>" +
//                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
//                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
//                "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
//                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
//                "</footer>" +
//                "</div>";
//        for (User user : subscribedUsers){
//            emailService.sendHtmlEmailWithAttachment(user.getEmail(), subject,htmlMessage,path);
//        }
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"SUCCESS",true));
//
//    }
}
