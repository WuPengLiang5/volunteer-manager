//package com.wpl.volunteer.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        String[] whiteList = {"/admin/doLogin",
//                "/user/doLogin",
//                "/user/register",
//                "/user/verifyCode",
//                "/news/getNewsByType",
//                "/activity/listActivities",
//                "/activity/getActivityById",
//                "/signup/saveSignUp",
//                "/signup/getSignUpByActivityId",
//                "/signup/getSignUpByAIdAndUId"};
//        registry.addInterceptor(jwtAuthenticationInterceptor)
//                //拦截请求 /**是拦截所有请求
//                .addPathPatterns("/**","/hello1")
//                //放行请求
//                .excludePathPatterns(whiteList);
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/**");
//    }
//}
