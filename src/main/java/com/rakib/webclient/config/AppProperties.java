package com.rakib.webclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final GitHub github = new GitHub();

    @Setter
    @Getter
    public static class GitHub {
        private String username;
        private String token;
    }

    public GitHub getGithub() {
        return github;
    }
}
