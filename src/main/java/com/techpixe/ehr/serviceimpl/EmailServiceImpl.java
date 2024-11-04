package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("$(spring.mail.username)")
    private String fromMail;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("sucwess");
    }
}
