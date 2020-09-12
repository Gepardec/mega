package com.gepardec.mega.application.interceptor;

import com.gepardec.mega.domain.model.Role;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@InterceptorBinding
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RolesAllowed {
    @Nonbinding
    Role[] allowedRoles();
}