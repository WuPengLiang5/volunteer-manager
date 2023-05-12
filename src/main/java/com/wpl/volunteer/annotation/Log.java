package com.wpl.volunteer.annotation;

import com.wpl.volunteer.enums.BusinessType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String module() default "操作模块"; //模块
    BusinessType businessType() default BusinessType.OTHER; //功能
}
