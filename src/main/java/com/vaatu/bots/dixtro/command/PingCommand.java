package com.vaatu.bots.dixtro.command;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PingCommand implements IExecuteCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        Long ping = interaction.getJDA().getRestPing().complete();
        interaction.reply(MessageFormat.format("✅ {0} Pong!", ping)).setEphemeral(true).queue();
    }
}
