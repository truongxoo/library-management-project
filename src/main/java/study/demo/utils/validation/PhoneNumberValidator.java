package study.demo.utils.validation;

import java.time.Instant;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.PhoneNumber;
import study.demo.service.exception.DataInvalidException;

@Slf4j
@RequiredArgsConstructor

public class PhoneNumberValidator implements ConstraintValidator<Phone, PhoneNumber> {

    private final MessageSource messageSource;
    
    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(PhoneNumber phoneNumber, ConstraintValidatorContext context) {
        if(phoneNumber.getLocale()==null || phoneNumber.getNewPhone()==null){
             return false;
        }
        try{
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumber.getNewPhone(), phoneNumber.getLocale()));
        }
        catch (NumberParseException e){
            log.warn("Fail to parse phone at : {}", Instant.now()); 
            throw new DataInvalidException(messageSource.getMessage("parsephone.fail", null,Locale.getDefault()),"parsephone.fail");
        }
      }
    }