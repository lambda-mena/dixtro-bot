package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.exception.BotInOtherVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.exception.UserNotInVoiceException;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class PlayCommand implements ISlashCommand, IOptionsCommand {
    private final TrackService trackService;

    public String getDescription() {
        return "Says pong!";
    }

    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "source", "URL or Keyword of track", true));
        return options;
    }

    private String getSource(SlashCommandInteraction interaction) {
        OptionMapping optionMapping = interaction.getOption("source");
        return Objects.requireNonNull(optionMapping).getAsString();
    }

    private void playTrack(GuildTrackManager audioManager, SlashCommandInteraction interaction) {
        String source = getSource(interaction);
        audioManager.loadTrack(source);
    }

    private AudioChannelUnion getBotVoiceChannel(Guild guild) throws NoSuchElementException {
        AudioManager guildAudioManager = guild.getAudioManager();
        Optional<AudioChannelUnion> optAudioChannel = Optional.ofNullable(guildAudioManager.getConnectedChannel());
        return optAudioChannel.orElseThrow();
    }

    private AudioChannelUnion getUserVoiceChannel(SlashCommandInteraction interaction) {
        GuildVoiceState voiceState = Objects.requireNonNull(interaction.getMember()).getVoiceState();
        return Objects.requireNonNull(voiceState).getChannel();
    }

    private void createTrackManager(SlashCommandInteraction interaction) throws UserException {
        try {
            Guild guild = interaction.getGuild();
            AudioChannelUnion channel = getUserVoiceChannel(interaction);
            AudioManager guildAudioManager = Objects.requireNonNull(guild).getAudioManager();

            GuildTrackManager guildTrackManager = trackService.createAudioManager(guild, interaction.getChannel(), channel);
            guildAudioManager.setSendingHandler(guildTrackManager.getAudioPlayerSendHandler());

            playTrack(guildTrackManager, interaction);
            interaction.getHook().editOriginal("✅ Searching track...").queue();
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new UserNotInVoiceException();
        }
    }

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        try {
            String guildId = Objects.requireNonNull(interaction.getGuild()).getId();
            Optional<GuildTrackManager> optAudioManager = this.trackService.getAudioManager(guildId);
            GuildTrackManager guildTrackManager = optAudioManager.orElseThrow();

            Guild guild = interaction.getGuild();
            AudioChannelUnion channelUnion = getUserVoiceChannel(interaction);
            AudioChannelUnion botChannel = getBotVoiceChannel(guild);

            if (channelUnion.equals(botChannel)) {
                playTrack(guildTrackManager, interaction);
                interaction.getHook().editOriginal("✅ Added to queue").queue();
            } else {
                throw new BotInOtherVoiceException();
            }
        } catch (NoSuchElementException ex) {
            this.createTrackManager(interaction);
        }
    }
}
