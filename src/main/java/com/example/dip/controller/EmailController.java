package com.example.dip.controller;


import com.example.dip.entity.customEmail.EmailDetails;
import com.example.dip.service.Interface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Annotation
@RestController
@RequiredArgsConstructor
// Class
public class EmailController {

     private final EmailService emailService;

    // Sending a simple Email
    @GetMapping("/sendMail")
    public String
    sendMail()
    {
        EmailDetails details= new EmailDetails();
        String status
                = emailService.sendSimpleMail(details);

        return status;
    }

//    // Sending email with attachment
//    @PostMapping("/sendMailWithAttachment")
//    public String sendMailWithAttachment(
//            @RequestBody EmailDetails details)
//    {
//        String status
//                = emailService.sendMailWithAttachment(details);
//
//        return status;
//    }
}