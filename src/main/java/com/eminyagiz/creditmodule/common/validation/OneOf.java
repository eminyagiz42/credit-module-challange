package com.eminyagiz.creditmodule.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OneOfValidator.class)
public @interface OneOf {

    String message() default "Value is not one of the allowed values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

    int[] value();
}

