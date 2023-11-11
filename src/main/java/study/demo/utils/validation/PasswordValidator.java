package study.demo.utils.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String>{

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }
    
    

}
