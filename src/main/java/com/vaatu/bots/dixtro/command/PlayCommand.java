package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.audio.AudioManager;
import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.service.ManagerService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PlayCommand implements IExecuteCommand {
    private final ManagerService managerService;

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        try {
            OptionMapping optionMapping = interaction.getOption("source");
            String source = optionMapping.getAsString();

            Guild guild = interaction.getGuild();
            AudioManager audioManager = this.managerService.getAudioManager(guild.getId());
            audioManager.loadTrack(source);
            interaction.reply("Playing....").setEphemeral(true).queue();
        } catch (NullPointerException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
