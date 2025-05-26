package com.eminyagiz.creditmodule.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class OneOfValidator implements ConstraintValidator<OneOf, Integer> {

    private Set<Integer> allowedValues;

    @Override
    public void initialize(OneOf constraintAnnotation) {
        allowedValues = Arrays.stream(constraintAnnotation.value()).boxed().collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Integer i, ConstraintValidatorContext constraintValidatorContext) {
        return i == null || allowedValues.contains(i);
    }
}

