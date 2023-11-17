package study.demo.utils.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    
    private static final String EMAIL_PATTERN = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";
    
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

}
