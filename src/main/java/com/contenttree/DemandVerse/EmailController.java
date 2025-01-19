package com.contenttree.DemandVerse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
public class EmailController {
    @Autowired
    SendEmailDemandVerse emailSender;

    @PostMapping("/api/mail/html/demand-verse/{email}")
    public ResponseEntity<String> sendHtmlMail(@PathVariable String email) {
        try {
            String personalizedBody = loadEmailTemplate("thankyou_email_send_view.php");
//            String personalizedBody = loadEmailTemplate("Demandverse.html");

            try {
                emailSender.sendEmail(email, "Thank You for Your Interest - Dear Security Teams: Why Your RBVM Strategy Isn’t Working ", personalizedBody);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error sending email: " + e.getMessage());
            }

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error loading email template: " + ex.getMessage());
        }

        return ResponseEntity.ok("Email sent successfully. to "+ email);
    }

    private String loadEmailTemplate(String templatePath) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                throw new IOException("Template file not found: " + templatePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
