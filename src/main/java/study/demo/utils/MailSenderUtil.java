package study.demo.utils;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
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
    
    private final MessageSource messages;
    
    @Async
    public void sendMail(MailContentDto mail) {

        String fromAddress = "truongxo7899@gmail.com";
        String senderName = "Library";

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom(fromAddress, senderName);
            helper.setTo(mail.getReceiver());
            helper.setSubject(mail.getSubject());

            helper.setText(mail.getContent(), true);
            if(mail.getAddInline() !=null || mail.getResource() !=null) {
                helper.addInline("imageBook",mail.getResource());
            }
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Can not send email");
            throw new ConfirmationInvalidException(messages.getMessage(
                    "sendmail.fail", null, Locale.getDefault()),"sendmail.fail");
        }
    }

}
