package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.exception.UserException;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public interface ISlashCommand {
    String getDescription();
    void execute(SlashCommandInteraction interaction) throws UserException;
}
