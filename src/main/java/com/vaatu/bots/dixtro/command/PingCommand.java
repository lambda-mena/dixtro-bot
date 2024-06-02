package com.vaatu.bots.dixtro.command;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PingCommand implements ISlashCommand {
    public String getDescription() {
        return "Says pong!";
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        Long ping = interaction.getJDA().getRestPing().complete();
        interaction.getHook().editOriginal(MessageFormat.format("✅ {0} Pong!", ping)).queue();
    }
}
