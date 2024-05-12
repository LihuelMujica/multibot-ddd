package com.lihuel.discordbot.discord;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.discord.commands.Command;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandManager extends ListenerAdapter {
    private static final Logger log = LogManager.getLogger(CommandManager.class);

    private final Map<String, Command> commands = new HashMap<>();
    private final JDAFactory jdaFactory;

    @Autowired
    public CommandManager(List<Command> commands, JDAFactory jdaFactory) {
        commands.forEach(command -> this.commands.put(command.getName(), command));
        this.jdaFactory = jdaFactory;
        log.info("Registering CommandManager as listener...");
        registerCommands();
    }

    public void registerCommands() {
        log.info("Registering commands...");
        commands.forEach(
                (name, command) -> {
                    SlashCommand slashCommand = command.getClass().getAnnotation(SlashCommand.class);
                    if (slashCommand == null) {
                        log.error("Command {} does not have SlashCommand annotation", name);
                        throw new RuntimeException("Command " + name + " does not have SlashCommand annotation");
                    }
                    String[] botsAliases = slashCommand.value();
                    if (botsAliases.length == 0) {
                        log.error("Command {} does not have any bot alias", name);
                        throw new RuntimeException("Command " + name + " does not have any bot alias");
                    }
                    for (String botsAlias : botsAliases) {
                        JDA jda = jdaFactory.getJda(botsAlias);
                        if (jda == null) {
                            log.error("Bot {} not found", botsAlias);
                            throw new RuntimeException("Bot " + botsAlias + " not found");
                        }
                        log.info("Registering command {} for bot {}", name, botsAlias);
                        registerCommand(command, jda);

                    }
                }
        );
        jdaFactory.getJdaMap().forEach((botName, jda) -> jda.addEventListener(this));
    }

    private void registerCommand(Command command, JDA jda) {
        if (command.getOptions() != null) {
            jda.upsertCommand(command.getName(), command.getDescription())
                    .addOptions(command.getOptions())
                    .queue();
            return;
        }
        jda.upsertCommand(command.getName(), command.getDescription()).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command command = commands.get(event.getName());
        if (command == null) {
            log.error("Command {} not found", event.getName());
            throw new RuntimeException("Command " + event.getName() + " not found");
        }
        try {
            log.info("Executing command: {}", command.getName());
            command.execute(event);
        }
        catch (Exception e) {
            log.error("Error executing command: {}", command.getName(), e);
            event.replyEmbeds(AlertEmbed.createError(e.getMessage())).setEphemeral(true).queue();
        }
    }

    //    @Override
//    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
//        for (Command command : commands) {
//            if (event.getName().equals(command.getName())) {
//                log.info("Executing command: {}", command.getName());
//                command.execute(event);
//                return;
//            }
//        }
//    }
}
