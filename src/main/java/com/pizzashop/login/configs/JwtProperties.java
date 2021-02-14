package com.pizzashop.login.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jwt")
@Data
@Configuration
public class JwtProperties {
    private String secretKey;
    private String issuer;
    private long validityInMilliseconds;
    private String authorization;
    private String bearer;
    private String appUser;
    private String appPassword;
}
