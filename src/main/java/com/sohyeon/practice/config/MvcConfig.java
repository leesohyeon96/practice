package com.sohyeon.practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//@EnableWebMvc -> Spring boot의 기능을 사용하지 않고 Spring MVC를 사용하여 직접 설정해주겠다는 의미 ㅠㅠ..
public class MvcConfig implements WebMvcConfigurer {
// WebMvcConfigurer VS WebMvcConfigurationSupport
// 1. WebMvcConfigurer -> 필요한 메소드만 구현해서 사용
//    ex) addViewControllers()
// 2. WebMvcConfigurationSupport


//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/examples/scripting/basic").setViewName("examples/scripting/basic");
//        registry.addViewController("/examples/scripting/mapbox").setViewName("examples/scripting/mapbox");
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/resource/img/").setCachePeriod(20);
//    }

}
