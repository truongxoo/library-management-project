package study.demo.utils.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateValidator.class)
public @interface ValidDate {
    
    String pattern() default ""; 
    
    String message() default "{ReleaseDate is incorrcet format}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
