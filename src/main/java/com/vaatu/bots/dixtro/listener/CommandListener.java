package com.vaatu.bots.dixtro.listener;

import com.vaatu.bots.dixtro.command.ISlashCommand;
import com.vaatu.bots.dixtro.embed.MusicEmbedFactory;
import com.vaatu.bots.dixtro.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class CommandListener extends ListenerAdapter {
    // TODO: Remake error messages to embeds.
    private final HashMap<String, ISlashCommand> commands = new HashMap<>();

    public CommandListener(List<ISlashCommand> commands) {
        for (ISlashCommand command : commands) {
            String className = command.getClass().getSimpleName();
            String commandName = className.split("Command")[0];
            this.commands.put(commandName.toLowerCase(), command);
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        String commandName = event.getName();
        ISlashCommand foundCommand = this.commands.get(commandName);

        try {
            foundCommand.execute(event);
        } catch (UserException ex) {
            log.error("{} User gave a bad input.", event.getUser().getName());
            MessageEmbed userErrorEmbed = MusicEmbedFactory.createUserErrorEmbed(ex.getMessage());
            event.getHook().sendMessageEmbeds(userErrorEmbed).queue();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            MessageEmbed errorEmbed = MusicEmbedFactory.createInternalErrorEmbed();
            event.getHook().sendMessageEmbeds(errorEmbed).queue();
        }
    }
}
