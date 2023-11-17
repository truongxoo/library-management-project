package study.demo.service.listener;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.User;
import study.demo.service.event.OnRegistrationCompleteEvent;
import study.demo.service.exception.ConfirmationInvalidException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final JavaMailSender mailSender;
    
    private final MessageSource messageSource;
    
    @Value("${mail.url.confirm}")
    private String urlConfirm;

    // called when registration is complete to send OTP and link to registered email
    // user
    @Async
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {

        User user = event.getUser();
        String fromAddress = "truongxo7899@gmail.com";
        String senderName = "Library";
        String subject = "Please verify your registration";
        String link = null;
        String otp = null;
        String content = null;
        
        if (event.getLink() != null && event.getOtp() != null) {
            link = event.getLink().getVerificationCode();
            otp = event.getOtp().getOtpCode();
            content = contentForRegisterNewMember(otp, link, user);
            String verifyURL = urlConfirm + link;
            content = content.replace("[[URL]]", verifyURL);
        } else {
            if (event.getLink() != null) {
                link = event.getLink().getVerificationCode();
                content = contentForResendNewLink(user);
                String verifyURL = urlConfirm + link;
                content = content.replace("[[URL]]", verifyURL);
            }
            if (event.getOtp() != null) {
                otp = event.getOtp().getOtpCode();
                content = contentForResendNewOtp(otp, user);
            }
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            helper.setText(content, true);
            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Can not send email");
            throw new ConfirmationInvalidException(messageSource.getMessage(
                    "sendmail.failed", null,Locale.getDefault()),"sendmail.failed");
        }

    }

    private String contentForResendNewLink(User user) {
        return "<p>Hello " + user.getEmail() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "This is your new link verification:</p>"
                +"<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" 
                + "<p>Note: This Link will expire in 24 hours.</p>";
                
    }

    private String contentForResendNewOtp(String otp,User user) {
        return "<p>Hello " + user.getEmail() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "This is your new OTP:</p>"
                + "<h4><b>" + otp + "</b></h4>"
                + "<p>Note: This OTP will expire in 1 minutes.</p>";
    }
    
    private String contentForRegisterNewMember(String otp,String link,User user) {
        return "<p>Hello " + user.getEmail() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "Please click the link or use OTP below to verify your registration:</p>"
                + "<h4><b>" + otp +"</b></h4>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "<p>Note: This OTP will expire in 1 minutes.</p>" 
                + "<p>Note: This Link will expire in 24 hours.</p>";
    }
    
}
