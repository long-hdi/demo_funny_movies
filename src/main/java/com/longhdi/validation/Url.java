package com.longhdi.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;

import static java.lang.annotation.ElementType.*;

@Target({CONSTRUCTOR, FIELD, METHOD, TYPE_USE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {Url.UrlValidator.class})
public @interface Url {

    String message() default "{com.remidemo.validation.Url.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class UrlValidator implements ConstraintValidator<Url, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) return true;
            if (value.trim().contains(" ")) return false;
            if (value.isEmpty() || value.isBlank()) return false;
            try {
                java.net.URL url = new java.net.URL(value);
                return true;
            } catch (MalformedURLException e) {
                return false;
            }
        }

    }
}
