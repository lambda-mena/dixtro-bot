package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.exception.UserException;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.springframework.stereotype.Component;

@Component
public class PingCommand implements IExecuteCommand {

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        interaction.reply("Pong!").setEphemeral(true).queue();
    }
}
