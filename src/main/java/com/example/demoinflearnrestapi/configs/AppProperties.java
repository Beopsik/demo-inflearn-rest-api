package com.example.demoinflearnrestapi.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "test-client")
public class AppProperties {
    @NotEmpty
    private String adminEmail;

    @NotEmpty
    private String adminPassword;

    @NotEmpty
    private String userEmail;

    @NotEmpty
    private String userPassword;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;
}
