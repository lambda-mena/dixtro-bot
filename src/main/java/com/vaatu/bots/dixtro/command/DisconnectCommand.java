package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.exception.BotNotInVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

@Component
public class DisconnectCommand implements IExecuteCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        try {
            Guild guild = interaction.getGuild();

            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();
            interaction.reply("✅ Disconnected").setEphemeral(true).queue();
        } catch (NullPointerException ex) {
            throw new BotNotInVoiceException();
        }

    }
}
