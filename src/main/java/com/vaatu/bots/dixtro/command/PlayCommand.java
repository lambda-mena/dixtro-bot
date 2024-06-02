package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.exception.BotNotInVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.exception.UserNotInVoiceException;
import com.vaatu.bots.dixtro.service.TrackService;
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
    // TODO: Fix bot connecting to a unmatched song.
    private final TrackService trackService;

    private String getSource(SlashCommandInteraction interaction) {
        OptionMapping optionMapping = interaction.getOption("source");
        return Objects.requireNonNull(optionMapping).getAsString();
    }

    private void playTrack(GuildTrackManager audioManager, SlashCommandInteraction interaction) {
        String source = getSource(interaction);
        audioManager.loadTrack(source);
    }

    private AudioChannelUnion getVoiceChannel(SlashCommandInteraction interaction) {
        GuildVoiceState voiceState = Objects.requireNonNull(interaction.getMember()).getVoiceState();
        return Objects.requireNonNull(voiceState).getChannel();
    }

    private void joinChannelAndPlay(SlashCommandInteraction interaction) throws UserException {
        try {
            Guild guild = interaction.getGuild();
            AudioChannelUnion channel = getVoiceChannel(interaction);
            AudioManager guildAudioManager = Objects.requireNonNull(guild).getAudioManager();
            guildAudioManager.openAudioConnection(channel);

            GuildTrackManager guildTrackManager = trackService.createAudioManager(guild, interaction.getChannel());
            guildAudioManager.setSendingHandler(guildTrackManager.getAudioPlayerSendHandler());

            playTrack(guildTrackManager, interaction);
            interaction.getHook().editOriginal("✅ Joined").queue();
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new UserNotInVoiceException();
        }
    }

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        interaction.deferReply(true).addContent("Loading...").queue();

        try {
            String guildId = Objects.requireNonNull(interaction.getGuild()).getId();
            Optional<GuildTrackManager> optAudioManager = this.trackService.getAudioManager(guildId);
            GuildTrackManager guildTrackManager = optAudioManager.orElseThrow();

            Guild guild = interaction.getGuild();
            AudioChannelUnion channelUnion = getVoiceChannel(interaction);

            if (channelUnion.equals(guild.getAudioManager().getConnectedChannel())) {
                playTrack(guildTrackManager, interaction);
                interaction.getHook().editOriginal("✅ Added to queue").queue();
            } else {
                throw new BotNotInVoiceException();
            }
        } catch (NoSuchElementException ex) {
            this.joinChannelAndPlay(interaction);
        }
    }
}
