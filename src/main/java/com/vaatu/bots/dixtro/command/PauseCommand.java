package com.vaatu.bots.dixtro.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.exception.BotNotInVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class PauseCommand implements ISlashCommand {
    private final TrackService trackService;

    @Override
    public String getDescription() {
        return "Pauses the current track.";
    }

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        try {
            Guild guild = Objects.requireNonNull(interaction.getGuild());
            Member member = interaction.getMember();
            GuildTrackManager trackManager = trackService.getAudioManager(guild.getId(), member);
            AudioPlayer audioPlayer = trackManager.getAudioPlayer();
            audioPlayer.setPaused(!audioPlayer.isPaused());
            interaction.getHook().editOriginal("✅ Command executed ").queue();
        } catch (NoSuchElementException ex) {
            throw new BotNotInVoiceException();
        }
    }
}
