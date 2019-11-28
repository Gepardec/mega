package com.gepardec.mega.annotations;

import com.gepardec.mega.security.Role;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@InterceptorBinding
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Authorization {
    @Nonbinding
    Role[] allowedRoles() default {};
}