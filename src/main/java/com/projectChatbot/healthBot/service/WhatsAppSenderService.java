package com.projectChatbot.healthBot.service;

import com.projectChatbot.healthBot.entity.User;
import com.projectChatbot.healthBot.repository.UserRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsAppSenderService {

    private final UserRepository userRepository;

    @Value("${twilio.whatsappNumber}")
    private String fromNumber;

    public WhatsAppSenderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void sendToAllUsers(String messageBody) {

        List<User> users = userRepository.findBySubscribedTrue();

        for (User user : users) {

//            Message.creator(
//                    new PhoneNumber("whatsapp:" + user.getPhoneNumber()),
//                    new PhoneNumber(fromNumber),
//                    messageBody
//            ).create();
            System.out.println("whatsapp: " + user.getPhoneNumber() + "From : " + fromNumber + messageBody);
        }
    }
}