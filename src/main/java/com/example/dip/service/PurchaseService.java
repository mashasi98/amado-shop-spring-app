package com.example.dip.service;

import com.example.dip.entity.Purchase;
import com.example.dip.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final JavaMailSender javaMailSender;
    public final JavaMailSender emailSender;

    public Purchase findById(Long id) {
        return purchaseRepository.getById(id);
    }

    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Purchase findByOrderNumber(Long orderNumber) {
        return purchaseRepository.findByOrder_number(orderNumber);
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }


    public void sendSimpleEmail() {

        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("mashasi1998@gmail.com");
        message.setSubject("Test Simple Email");
        message.setText("Hello, Im testing Simple Email");

        // Send Message!
        this.emailSender.send(message);


    }
}



