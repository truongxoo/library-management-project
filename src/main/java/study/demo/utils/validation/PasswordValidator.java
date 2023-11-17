package study.demo.utils.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.util.ObjectUtils;

import study.demo.repository.UserRepository;
import study.demo.service.dto.request.ChangePasswordRequestDto;

public class PasswordValidator implements ConstraintValidator<ValidPassword, ChangePasswordRequestDto> {

    @Autowired
    private UserRepository userRepository;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

//    @Override
//    public void initialize(ValidPassword constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
//    }

    @Override
    public boolean isValid(ChangePasswordRequestDto value, ConstraintValidatorContext context) {

        boolean isErrored = false;

        if (ObjectUtils.isEmpty(value.getCurrentPassword()) || ObjectUtils.isEmpty(value.getNewPassword())
                || ObjectUtils.isEmpty(value.getConfirmPassword())) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate("All fields are required")
//            .addConstraintViolation();
            isErrored = true;
        }
        if (!Pattern.matches(PASSWORD_PATTERN, value.getNewPassword())) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate(
//                    "Password must be minimum 8 characters long and contain one uppercase,one lowercase character and number")
//                    .addConstraintViolation();
            isErrored = true;
        }
        if (!value.getConfirmPassword().equals(value.getNewPassword())) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate("Confirm password is not match").addConstraintViolation();
            isErrored = true;
        }

        return !isErrored;
    }

}
