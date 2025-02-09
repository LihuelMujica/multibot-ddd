package com.lihuel.discordbot.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface SlashCommand {
    String[] value();
}
