package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.audio.BotAudioManager;
import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.exception.UserNotInVoiceException;
import com.vaatu.bots.dixtro.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class PlayCommand implements IExecuteCommand {
    // TODO: Make users able to input song names instead of urls or ids.
    private final ManagerService managerService;

    private String getSource(SlashCommandInteraction interaction) {
        OptionMapping optionMapping = interaction.getOption("source");
        return Objects.requireNonNull(optionMapping).getAsString();
    }

    private void playTrack(BotAudioManager audioManager, SlashCommandInteraction interaction) {
        String source = getSource(interaction);
        audioManager.loadTrack(source);
    }

    private void joinChannelAndPlay(SlashCommandInteraction interaction) throws UserException {
        try {
            Guild guild = interaction.getGuild();
            GuildVoiceState voiceState = Objects.requireNonNull(interaction.getMember()).getVoiceState();
            AudioChannelUnion channel = Objects.requireNonNull(voiceState).getChannel();

            AudioManager guildAudioManager = Objects.requireNonNull(guild).getAudioManager();
            BotAudioManager botAudioManager = managerService.createAudioManager(guild.getId());
            guildAudioManager.setSendingHandler(botAudioManager.getAudioPlayerSendHandler());
            guildAudioManager.openAudioConnection(channel);

            playTrack(botAudioManager, interaction);
            interaction.getHook().editOriginal("✅ Joined & Playing").queue();
        } catch (NullPointerException ex) {
            throw new UserNotInVoiceException();
        }
    }

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        interaction.deferReply(true).addContent("Loading...").queue();

        try {
            String guildId = Objects.requireNonNull(interaction.getGuild()).getId();
            Optional<BotAudioManager> optAudioManager = this.managerService.getAudioManager(guildId);
            BotAudioManager botAudioManager = optAudioManager.orElseThrow();

            playTrack(botAudioManager, interaction);
            interaction.getHook().editOriginal("✅ Added to queue").queue();
        } catch (NoSuchElementException ex) {
            this.joinChannelAndPlay(interaction);
        }
    }
}
