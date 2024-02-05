package com.city.adminpermission.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminPermission {
    public abstract String[] value();
}
