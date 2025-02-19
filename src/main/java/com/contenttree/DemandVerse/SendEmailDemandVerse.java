package com.contenttree.DemandVerse;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendEmailDemandVerse {
    @Autowired
    JavaMailSender javaMailSender;
    public void sendEmail(String to, String subject, String body) {
        try {
            if (to == null || to.isEmpty()) {
                log.error("Recipient email address is missing");
                return;
            }
            if (subject == null || subject.isEmpty()) {
                log.error("Email subject is missing");
                return;
            }
            if (body == null || body.isEmpty()) {
                log.error("Email body is missing");
                return;
            }

            String fromName = "Emily Roberts";
            String fromEmail = "emily.roberts@openwhitepapers.com";

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mimeMessage.addHeader("Disposition-Notification-To", "no-reply@openwhitepapers.com");
            mimeMessage.addHeader("Return-Receipt-To", "no-reply@openwhitepapers.com");
            mimeMessage.addHeader("X-Confirm-Reading-To", "no-reply@openwhitepapers.com");
            mimeMessage.setFrom(new InternetAddress(fromEmail, fromName));
            ClassPathResource pdfResource = new ClassPathResource("dear-security-teams-why-your-rbvm-strategy-isnt-working.pdf");
            helper.addAttachment("RBVM_Strategy.pdf", pdfResource);

            javaMailSender.send(mimeMessage);

            log.info("Email successfully sent to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }
    public void sendEmailPcs(String to, String subject, String body) {
        try {
            if (to == null || to.isEmpty()) {
                log.error("Recipient email address is missing");
                return;
            }
            if (subject == null || subject.isEmpty()) {
                log.error("Email subject is missing");
                return;
            }
            if (body == null || body.isEmpty()) {
                log.error("Email body is missing");
                return;
            }

            String fromName = "Emily Roberts";
            String fromEmail = "emily.roberts@openwhitepapers.com";

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mimeMessage.addHeader("Disposition-Notification-To", "no-reply@openwhitepapers.com");
            mimeMessage.addHeader("Return-Receipt-To", "no-reply@openwhitepapers.com");
            mimeMessage.addHeader("X-Confirm-Reading-To", "no-reply@openwhitepapers.com");
            mimeMessage.setFrom(new InternetAddress(fromEmail, fromName));
            ClassPathResource pdfResource = new ClassPathResource("ai-pcs.pdf");
            helper.addAttachment("drive-business-resilience-with-ai-pcs.pdf", pdfResource);

            javaMailSender.send(mimeMessage);

            log.info("Email successfully sent to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }
    public void sendEmailBuyersGuide(String to, String subject, String body) {
        try {
            if (to == null || to.isEmpty()) {
                log.error("Recipient email address is missing");
                return;
            }
            if (subject == null || subject.isEmpty()) {
                log.error("Email subject is missing");
                return;
            }
            if (body == null || body.isEmpty()) {
                log.error("Email body is missing");
                return;
            }

            String fromName = "Emily Roberts";
            String fromEmail = "emily.roberts@openwhitepapers.com";

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mimeMessage.addHeader("Disposition-Notification-To", "no-reply@openwhitepapers.com");
            mimeMessage.addHeader("Return-Receipt-To", "no-reply@openwhitepapers.com");
            mimeMessage.addHeader("X-Confirm-Reading-To", "no-reply@openwhitepapers.com");
            mimeMessage.setFrom(new InternetAddress(fromEmail, fromName));
//            ClassPathResource pdfResource = new ClassPathResource("ai-pcs.pdf");
//            helper.addAttachment("drive-business-resilience-with-ai-pcs.pdf", pdfResource);

            javaMailSender.send(mimeMessage);

            log.info("Email successfully sent to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }
}
