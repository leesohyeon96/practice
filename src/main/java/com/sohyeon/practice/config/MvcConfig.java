package com.sohyeon.practice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurationSupport {
    // TODO: viewResolver 설정 필요
}
