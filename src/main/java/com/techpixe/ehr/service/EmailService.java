package com.techpixe.ehr.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);

}
