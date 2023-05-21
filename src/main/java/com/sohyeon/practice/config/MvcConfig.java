package com.sohyeon.practice.config;

import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;

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

    // 1. addResourceHandle("/**") : 모든 리소스 허용
    // 2. addResourceLocations("class:/templates/") : 리소스 위치 설정
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/img/**")
//                .addResourceLocations("classpath:/templates/")
////                .setCachePeriod(20);
//                .setCacheControl(CacheControl.noCache());
//    }

}
