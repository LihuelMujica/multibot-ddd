package com.lihuel.discordbot.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties(prefix = "discord")
@Configuration
@Data
@NoArgsConstructor
public class DiscordApplications {
    Map<String, DiscordApplication> bots;

    @Data
    @NoArgsConstructor
    public static class DiscordApplication {
        private String discordId;
        private String discordSecret;
    }
}
