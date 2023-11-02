package study.demo.service.listener;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import study.demo.entity.LinkVerification;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;
import study.demo.service.LinkVerificationService;
import study.demo.service.OtpVerificationService;
import study.demo.service.event.OnRegistrationCompleteEvent;

@Component
@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final LinkVerificationService linkVerificationService;
    
    private final OtpVerificationService otpVerificationService;

    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {

        User user = event.getUser();
       LinkVerification link =  linkVerificationService.createVerificationToken(user);
       OtpVerification otp = otpVerificationService.createOtpVerification(user);
      
        String fromAddress = "truongxo7899@gmail.com";
        String senderName = "Library";
        String subject = "Please verify your registration";
        
        String content ="<p>Hello " + user.getEmail() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "Please click the link or use OTP below to verify your registration:</p>"
                + "<h4><b>" + otp.getOtpCode() + "</b></h4>"
                +"<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" 
                + "<p>Note: This OTP will expire in 1 minutes.</p>"
                + "<p>Note: This OTP will expire in 24 hours.</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            
            String verifyURL = event.getAppUrl() + "/confirmationLink?verificationCode=" + link.getVerificationCode();
            content = content.replace("[[URL]]", verifyURL);
            
            helper.setText(content, true);
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } 

    }
}
