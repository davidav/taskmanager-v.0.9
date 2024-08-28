package com.example.taskmanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PagesFilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PagesFilterValid {
    String message() default "Pagination fields must be specified";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
