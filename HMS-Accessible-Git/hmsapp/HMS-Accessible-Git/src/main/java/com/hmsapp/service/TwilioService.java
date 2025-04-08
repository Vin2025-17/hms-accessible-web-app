package com.hmsapp.service;
import com.hmsapp.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
        // Initialize Twilio with the account SID and auth token
        this.twilioConfig.init();
    }

    public void sendSms(String toPhoneNumber, String messageBody) {
        try {
            // Send the SMS message
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(twilioConfig.getPhoneNumber()),
                    messageBody
            ).create();
            System.out.println("MESSAGE SENT WITH SID"+message.getSid());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending SMS: " + e.getMessage());
        }
    }
}
