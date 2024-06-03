package com.vaatu.bots.dixtro.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.embed.EmbedFactory;
import com.vaatu.bots.dixtro.exception.BotNotInVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class QueueCommand implements ISlashCommand {
    private final TrackService trackService;

    @Override
    public String getDescription() {
        return "Shows the current track queue.";
    }

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        try {
            Guild guild = Objects.requireNonNull(interaction.getGuild());
            Member member = interaction.getMember();
            GuildTrackManager trackManager = trackService.getAudioManager(guild.getId(), member);
            List<AudioTrack> trackList = new ArrayList<>(trackManager.getQueue());
            trackList.addFirst(trackManager.getAudioPlayer().getPlayingTrack());

            MessageEmbed embed = EmbedFactory.createQueueEmbed(trackList);
            interaction.getHook().sendMessageEmbeds(embed).queue();
        } catch (NoSuchElementException ex) {
            throw new BotNotInVoiceException();
        }
    }
}
