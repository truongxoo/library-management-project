package study.demo.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.exception.ConfirmationInvalidException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;

    public void sendMail(String userName,String newPassword) {

        String fromAddress = "truongxo7899@gmail.com";
        String senderName = "Library";
        String subject = "Reset Password";
        String content = null;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(userName);
            helper.setSubject(subject);

            content = "<p>Hello " + userName + "</p>" 
                    + "This is your new passowrd:</p>"
                    + "<b>"+newPassword+"</b>";

            helper.setText(content, true);
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Can not send email");
            throw new ConfirmationInvalidException("Can not send email");
        }
    }

}
