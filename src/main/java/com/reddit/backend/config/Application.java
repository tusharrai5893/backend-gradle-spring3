package com.reddit.backend.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@Data
@ConfigurationProperties(prefix = "app")
public class Application {

    @NotNull
    private String url;

}
