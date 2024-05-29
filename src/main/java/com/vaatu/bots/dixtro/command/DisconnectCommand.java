package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.exception.BotNotInVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DisconnectCommand implements IExecuteCommand {
    // TODO: Transform this into a event when the queue ends.

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        try {
            Guild guild = interaction.getGuild();

            AudioManager audioManager = Objects.requireNonNull(guild).getAudioManager();
            audioManager.closeAudioConnection();
            interaction.reply("✅ Disconnected").setEphemeral(true).queue();
        } catch (NullPointerException ex) {
            throw new BotNotInVoiceException();
        }

    }
}
