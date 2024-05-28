package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.exception.UserNotInVoiceException;
import com.vaatu.bots.dixtro.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class JoinCommand implements IExecuteCommand{
    private final ManagerService managerService;

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        try {
            Member member = interaction.getMember();
            Guild guild = interaction.getGuild();

            GuildVoiceState voiceState = member.getVoiceState();
            AudioChannelUnion channel = voiceState.getChannel();
            AudioManager audioManager = guild.getAudioManager();
            audioManager.setSendingHandler(managerService.createAudioManager(guild.getId()));
            audioManager.openAudioConnection(channel);
            interaction.reply("✅ Joined voice channel").setEphemeral(true).queue();
        } catch (NullPointerException ex) {
            log.error(ex.getMessage());
            throw new UserNotInVoiceException();
        }
    }
}
