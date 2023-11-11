package study.demo.service;

import java.net.URI;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;
import study.demo.service.dto.response.MessageResponseDto;

@Service
@RequiredArgsConstructor
public class SMSService {

    private final MessageSource messages;

    public static final String ACCOUNT_SID = "SID";
    public static final String AUTH_TOKEN = "AUTH";

    public static final String TWILIO_NUMBER = "+12223334444";

    public MessageResponseDto sendSMS() {

        Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));
        Message.creator(new PhoneNumber("0398455205"), 
                new PhoneNumber("0398455205"), 
                "Hello from Library")
        .create();

        return new MessageResponseDto(messages.getMessage("sendsms.success", null, Locale.getDefault()));
    }
}
