package com.contenttree.user;

import com.contenttree.confirmationtoken.ConfirmationToken;
import com.contenttree.confirmationtoken.ConfirmationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

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
    public User getUserById(long id){
        return userRepository.findById(id).orElse(null);
    }

    public ResponseEntity<?> saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        userRepository.save(user);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/api/user/confirm-account?token=" + confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        return ResponseEntity.ok("Verify email by the link sent to your email address");
    }

    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null) {
            User user = token.getUser();
            if (user != null) {
                user.setEnabled(true);
                userRepository.save(user);
                return ResponseEntity.ok("Email verified successfully!");
            }
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
}
