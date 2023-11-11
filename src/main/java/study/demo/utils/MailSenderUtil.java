package study.demo.utils;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.dto.response.MailContentDto;
import study.demo.service.exception.ConfirmationInvalidException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSenderUtil {

    private final JavaMailSender mailSender;

    public void sendMail(MailContentDto mail) {

        String fromAddress = "truongxo7899@gmail.com";
        String senderName = "Library";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(mail.getReceiver());
            helper.setSubject(mail.getSubject());

            helper.setText(mail.getContent(), true);
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Can not send email");
            throw new ConfirmationInvalidException("Can not send email");
        }
    }

}
