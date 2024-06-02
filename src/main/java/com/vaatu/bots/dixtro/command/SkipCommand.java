package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.exception.BotNotInVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SkipCommand implements ISlashCommand {
    private final TrackService trackService;

    public String getDescription() {
        return "Skips the current track";
    }

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        Guild guild = Objects.requireNonNull(interaction.getGuild());
        AudioManager audioManager = guild.getAudioManager();

        Optional<GuildTrackManager> optGuildTrackManager = this.trackService.getAudioManager(guild.getId());
        if (optGuildTrackManager.isPresent() && audioManager.isConnected()) {
            GuildTrackManager guildTrackManager = optGuildTrackManager.get();
            guildTrackManager.skipTrack();
            interaction.getHook().editOriginal("✅ Song skipped").queue();
        } else {
            throw new BotNotInVoiceException();
        }
    }
}
