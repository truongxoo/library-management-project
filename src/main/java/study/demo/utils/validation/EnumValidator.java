package study.demo.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEmail, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }

}
