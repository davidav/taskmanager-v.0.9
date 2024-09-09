package com.example.taskmanager.aop;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMocUser {

    long userId() default 1L;
    String username() default "user";
    String email() default "user@mail.com";
    String password() default "user";
    String[] authorities() default "ROLE_USER";


}
