package study.demo.utils.validation;

import java.time.Instant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<ValidDate, Instant>{

    @Override
    public boolean isValid(Instant date, ConstraintValidatorContext context) {
        if (date!= null) {
            return date.isBefore(Instant.now());
        }
        return true;
    }
    
}
