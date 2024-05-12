package com.lihuel.discordbot.discord.utils.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AlertEmbed {
    /** Custom Emojis. */
    public static final String GREEN_TICK = "<:greenMark:1078434426368839750>";
    public static final String BLUE_TICK = "<:blue_tick:800623774293819413>";
    public static final String RED_X = "<:x:800554807164665916>";
    public static final String BLUE_X = "<:blue_x:800623785736798228>";

    public static @NotNull MessageEmbed createError(String errorMessage) {
        return new EmbedBuilder()
                .setColor(Color.red)
                .setDescription(RED_X + " " + errorMessage)
                .build();
    }

    public static @NotNull MessageEmbed createSuccess(String message) {
        return new EmbedBuilder()
                .setColor(Color.green)
                .setDescription("✅" + " " + message)
                .build();
    }
    public static @NotNull MessageEmbed createInfo(String message) {
        return new EmbedBuilder()
                .setColor(Color.blue)
                .setDescription(BLUE_TICK + " " + message)
                .build();
    }
}
