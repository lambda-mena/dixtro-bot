package com.vaatu.bots.dixtro.listener;

import com.vaatu.bots.dixtro.command.IExecuteCommand;
import com.vaatu.bots.dixtro.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class CommandListener extends ListenerAdapter {

    private final HashMap<String, IExecuteCommand> commands = new HashMap<>();

    public CommandListener(List<IExecuteCommand> commands) {
        for (IExecuteCommand command : commands) {
            String className = command.getClass().getSimpleName();
            String commandName = className.split("Command")[0];
            this.commands.put(commandName.toLowerCase(), command);
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        MessageChannelUnion guildChannel = event.getChannel();
        String commandName = event.getName();
        IExecuteCommand foundCommand = this.commands.get(commandName);

        try {
            foundCommand.execute(event);
        } catch (UserException ex) {
            log.error("{} User gave a bad input.", event.getUser().getName());
            guildChannel.sendMessage(ex.getMessage()).queue();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            guildChannel.sendMessage("❌ Internal Error").queue();
        }
    }
}
