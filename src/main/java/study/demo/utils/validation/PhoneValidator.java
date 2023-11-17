package study.demo.utils.validation;

import java.time.Instant;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.service.exception.DataInvalidException;


@Slf4j
@RequiredArgsConstructor
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private final MessageSource messageSource;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return isPhoneNumberValid(phone);
    }

    private boolean isPhoneNumberValid(String phone) {
        
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        
        PhoneNumber phoneNumber = null;
        
//        PhoneNumber phoneNumber = phoneUtil.parse(number, country); 
        try {
            phoneNumber = phoneUtil.parse(phone, "VN"); // 
        } catch (NumberParseException ex) {
            log.warn("Fail to parse phone at : {}", Instant.now()); 
            throw new DataInvalidException(messageSource.getMessage("parsephone.fail", null,Locale.getDefault()),"parsephone.fail");
        }
        return phoneUtil.isValidNumber(phoneNumber);
    }

}

