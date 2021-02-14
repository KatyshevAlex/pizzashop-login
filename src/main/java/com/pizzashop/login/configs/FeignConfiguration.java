package com.pizzashop.login.configs;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Autowired
    JwtProperties jwtProperties;
    //Login and password for appUser in properties:
    //jwt.appUser
    //jwt.appPassword
    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(jwtProperties.getAppUser(), jwtProperties.getAppPassword());
    }

}
