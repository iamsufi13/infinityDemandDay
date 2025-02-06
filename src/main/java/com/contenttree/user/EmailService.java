package com.contenttree.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendSimpleEmail(SimpleMailMessage email) {
        javaMailSender.send(email);  // For plain text emails
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String name="Infiniteb2b";
        helper.setTo(to);
        helper.setFrom(new InternetAddress(name));
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
    @Async
    public void sendHtmlEmailWithAttachment(String to, String subject, String htmlContent, String attachmentPath) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        if (attachmentPath != null) {
            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment(file.getFilename(), file);
        }

        javaMailSender.send(mimeMessage);
    }

}
