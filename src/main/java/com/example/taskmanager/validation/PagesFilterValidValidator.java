package com.example.taskmanager.validation;


import com.example.taskmanager.dto.PagesRq;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class PagesFilterValidValidator implements ConstraintValidator<PagesFilterValid, PagesRq> {

    @Override
    public boolean isValid(PagesRq value, ConstraintValidatorContext context) {
        return !ObjectUtils.anyNull(value.getPageNumber(), value.getPageSize());
    }
}
