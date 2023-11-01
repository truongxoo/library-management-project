package study.demo.service.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import study.demo.entity.User;
import study.demo.service.LinkVerificationService;
import study.demo.service.event.OnRegistrationCompleteEvent;

@Component
@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final LinkVerificationService linkVerificationService;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String verificationCode = UUID.randomUUID().toString();
        linkVerificationService.createVerificationToken(user, verificationCode);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/regitrationConfirm?verificationCode=" + verificationCode;
        String message = messages.getMessage("message.regSucc", null, null);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }

}
