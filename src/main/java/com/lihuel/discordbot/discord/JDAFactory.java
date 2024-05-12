package com.lihuel.discordbot.discord;

import com.lihuel.discordbot.config.DiscordApplications;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to create JDA instances for each bot
 * It is a singleton
 * It is created when the application starts
 * It is autowired in the Discord class
 * It is used to get the JDA instance for a particular bot
 */
@Configuration
public class JDAFactory {
    private static final Logger logger = LogManager.getLogger(JDAFactory.class);
    private final DiscordApplications discordApplications;

    @Getter
    private final Map<String, JDA> jdaMap = new HashMap<>();


    /**
     * Constructor of JdaFactory
     * It creates a JDA instance for each bot
     * It creates a default JDA instance for the main bot
     * If you wish to have a different configuration for a particular bot, you should modify this method
     * In the future, this method should be modified to read the configuration from a file
     * @param discordApplications  The DiscordApplications object, which contains the configuration for each bot
     */
    @Autowired
    public JDAFactory(DiscordApplications discordApplications) {
        this.discordApplications = discordApplications;
        discordApplications.getBots().forEach((botName, discordApplication) -> {
            jdaMap.put(botName, jda(discordApplication.getDiscordSecret()));
        }
        );
    }


    public JDA jda(String token) {
        try {
            JDA jda = JDABuilder.createDefault(token)
                    .build();
            logger.info("JDA successfully created");
            return jda;
        } catch (Exception e) {
            logger.error("Error creating JDA: {}", e.getMessage());
            throw new RuntimeException("Error creating JDA");
        }
    }

    /**
     * Returns the JDA instance for a particular bot
     * @param botName The name of the bot. It should be the same as the name of the application in the application.yml file,
     * @return The JDA instance for the bot
     */
    public JDA getJda(String botName) {
        return jdaMap.get(botName);
    }

}
