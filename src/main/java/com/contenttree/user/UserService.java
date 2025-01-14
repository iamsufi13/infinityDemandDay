package com.contenttree.user;

import com.contenttree.confirmationtoken.ConfirmationToken;
import com.contenttree.confirmationtoken.ConfirmationTokenRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    public User getUserByEmail(String email){
        log.info("User Service {}",email);
        return userRepository.findByEmailIgnoreCase(email);
    }

    public List<User> getAllUsers(){return userRepository.findAll();}
    public User getUserById(long id){
        return userRepository.findById(id).orElse(null);
    }

//    public ResponseEntity<?> saveUser(User user) {
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return ResponseEntity.badRequest().body("Error: Email is already in use!");
//        }
//
//        userRepository.save(user);
//        ConfirmationToken confirmationToken = new ConfirmationToken(user);
//        confirmationTokenRepository.save(confirmationToken);
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(user.getEmail());
//        mailMessage.setSubject("Complete Registration!");
//        mailMessage.setText("To confirm your account, please click here : "
//                + "http://localhost:8080/api/user/confirm-account?token=" + confirmationToken.getConfirmationToken());
//        emailService.sendEmail(mailMessage);
//
//        return ResponseEntity.ok("Verify email by the link sent to your email address");
//    }


//    public ResponseEntity<?> saveUser(User user) throws MessagingException, IOException {
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return ResponseEntity.badRequest().body("Error: Email is already in use!");
//        }
//
//        userRepository.save(user);
//        ConfirmationToken confirmationToken = new ConfirmationToken(user);
//        confirmationTokenRepository.save(confirmationToken);
//
//        String logoUrl = "https://infiniteb2b.com/static/media/Infinite-b2b-1-scaled.f42a6998e6eac74721e6.png";
//
////        String confirmationUrl = "http://141.136.35.203:8080/api/user/confirm-account?token=" + confirmationToken.getConfirmationToken();
////        String confirmationUrl = "https://141.136.35.203:8443/api/user/confirm-account?token=" + confirmationToken.getConfirmationToken();
//        String confirmationUrl = "https://infiniteb2b.com:8443/api/user/confirm-account?token=" + confirmationToken.getConfirmationToken();
//
//        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
//                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
//                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
//                "</div>" +
//                "<h2 style=\"text-align: center; color: #28a745; font-size: 24px;\">Complete Your Registration</h2>" +
//                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
//                "Thank you for registering with <strong>InfiniteB2B</strong>! To confirm your account and complete the registration process, please click the button below." +
//                "Thank you for registering with <strong>InfiniteB2B</strong>! To confirm your account and complete the registration process, please click the button below." +
//                "</p>" +
//                "<div style=\"text-align: center; margin: 30px 0;\">" +
//                "<a href=\"" + confirmationUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px;\">Confirm Account</a>" +
//                "</div>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
//                "</p>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "<a href=\"" + confirmationUrl + "\" style=\"color: #28a745; word-wrap: break-word;\">" + confirmationUrl + "</a>" +
//                "</p>" +
//                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
//                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
//                "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
//                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
//                "</footer>" +
//                "</div>";
//
//        emailService.sendHtmlEmail(user.getEmail(), "Complete Registration!", htmlMessage);
//
//        return ResponseEntity.ok("Verify email by the link sent to your email address");
//    }
public ResponseEntity<?> saveUser(User user) throws MessagingException, IOException {
    if (userRepository.existsByEmail(user.getEmail())) {
        return ResponseEntity.badRequest().body("Error: Email is already in use!");
    }

    try {
        userRepository.save(user); // Save user entity
    } catch (Exception e) {
        log.error("Error while saving user to database: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to save user. Please try again later.");
    }

    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    try {
        confirmationTokenRepository.save(confirmationToken);
    } catch (Exception e) {
        // Log the error and return a failure response
        log.error("Error while saving confirmation token: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to generate confirmation token. Please try again later.");
    }

    String logoUrl = "https://infiniteb2b.com/static/media/Infinite-b2b-1-scaled.f42a6998e6eac74721e6.png";
    String confirmationUrl = "https://infiniteb2b.com:8443/api/user/confirm-account?token=" + confirmationToken.getConfirmationToken();

    String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
            "<div style=\"text-align: center; margin-bottom: 30px;\">" +
            "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
            "</div>" +
            "<h2 style=\"text-align: center; color: #28a745; font-size: 24px;\">Complete Your Registration</h2>" +
            "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
            "Thank you for registering with <strong>InfiniteB2B</strong>! To confirm your account and complete the registration process, please click the button below." +
            "</p>" +
            "<div style=\"text-align: center; margin: 30px 0;\">" +
            "<a href=\"" + confirmationUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px;\">Confirm Account</a>" +
            "</div>" +
            "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
            "If the button above doesn’t work, copy and paste the following URL into your browser:" +
            "</p>" +
            "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
            "<a href=\"" + confirmationUrl + "\" style=\"color: #28a745; word-wrap: break-word;\">" + confirmationUrl + "</a>" +
            "</p>" +
            "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
            "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
            "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
            "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
            "</footer>" +
            "</div>";

    // Send the confirmation email
    try {
        emailService.sendHtmlEmail(user.getEmail(), "Complete Registration!", htmlMessage);
    } catch (MessagingException e) {
        log.error("Error while sending confirmation email: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to send confirmation email. Please try again later.");
    }

    // Return a successful response
    return ResponseEntity.ok("Verification email sent. Please check your inbox to complete registration.");
}


    public ResponseEntity<User> updateUser(User user){
        User user1 = getUserById(user.getId());
        user1.setFavorites(user.getFavorites());
        user1.setIpAddress(user.getIpAddress());
        userRepository.save(user1);
        return ResponseEntity.ok().body(user1);
    }



//    public ResponseEntity<?> confirmEmail(String confirmationToken) {
//        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
//        if (token != null) {
//            User user = token.getUser();
//            if (user != null) {
//                user.setEnabled(true);
//                userRepository.save(user);
//                return ResponseEntity.ok("Email verified successfully!");
//            }
//        }
//        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
//    }
public ResponseEntity<?> confirmEmail(String confirmationToken) {
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    if (token != null) {
        User user = token.getUser();
        if (user != null) {
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.status(302)
                    .header("Location", "https://infiniteb2b.com/login")
                    .build();
        }
    }
    return ResponseEntity.badRequest().body("Error: Couldn't verify email");
}
    public User updateUserDetails(long userId, String name, String lastName, String country, Long phone,
                                  String jobTitle, String company, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (name != null) {
            user.setName(name);
        }
        if (lastName != null) {
            user.setLastName(lastName);
        }
        if (country != null) {
            user.setCountry(country);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (jobTitle != null) {
            user.setJobTitle(jobTitle);
        }
        if (company != null) {
            user.setCompany(company);
        }
        if (password != null) {
            user.setPassword(password);
        }

        return userRepository.save(user);
    }
}
