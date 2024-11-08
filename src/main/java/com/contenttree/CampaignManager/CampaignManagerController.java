package com.contenttree.CampaignManager;

import com.contenttree.admin.Admin;
import com.contenttree.admin.AdminRepository;
import com.contenttree.admin.AdminService;
import com.contenttree.admin.Role;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaign-manager")
public class CampaignManagerController {
    @Autowired
    AdminService adminService;
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    WebinarRepository webinarRepository;

    @Autowired
    EventRepository eventRepository;
    @GetMapping
    public ResponseEntity<ApiResponse1<Map<?, ?>>> getAllCampaignManagerDetails(@AuthenticationPrincipal Admin admin) {

        System.out.println("Admin roles: " + admin.getRole());

        if (admin.getRole() == null || !admin.getRole().contains(Role.CAMPAIGNMANAGER)) {
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "Unauthorized access", false));
        }

        List<Webinar> webinarList = webinarRepository.findByAdminId(admin.getId());
        List<Event> eventList = eventRepository.findByAdminId(admin.getId());

        Map<Object, Object> map = new HashMap<>();
        map.put("webinar", webinarList);
        map.put("event", eventList);

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
    }

    @PostMapping("/add-event")
    public ResponseEntity<ApiResponse1<Event>> addEvent(@RequestBody Event event, @AuthenticationPrincipal Admin admin) {
        if (admin.getRole() == null || !admin.getRole().contains(Role.CAMPAIGNMANAGER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseUtils.createResponse1(null, "Unauthorized access", false));
        }

        event.setAdmin(admin);
        eventRepository.save(event);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(event, "SUCCESS", true));
    }





}
