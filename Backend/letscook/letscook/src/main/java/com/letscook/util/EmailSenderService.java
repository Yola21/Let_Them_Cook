package com.letscook.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public boolean sendSimpleEmail(String toEmail,
                                   String subject,
                                   String body
    ) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("fromemail@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            mailSender.send(message);
        } catch (Exception e) {

        }
        return true;


    }


}

