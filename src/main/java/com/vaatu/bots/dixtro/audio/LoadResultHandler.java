package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.vaatu.bots.dixtro.embed.EmbedFactory;
import com.vaatu.bots.dixtro.message.FailedToLoadMessage;
import com.vaatu.bots.dixtro.message.NotFoundMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LoadResultHandler implements AudioLoadResultHandler {
    private final GuildTrackManager trackManager;

    private void connectToVoice() {
        if (!this.trackManager.isVoiceConnected()) {
            this.trackManager.connectVoiceManager();
        }
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        connectToVoice();
        log.info("Song: {} Loaded", audioTrack.getInfo().title);
        if (!trackManager.getAudioPlayer().startTrack(audioTrack, true)) {
            trackManager.getQueue().add(audioTrack);
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        connectToVoice();
        List<AudioTrack> tracks = audioPlaylist.getTracks();
        AudioTrack starterTrack = tracks.removeFirst();

        if (audioPlaylist.isSearchResult()) {
            trackLoaded(starterTrack);
        } else {
            trackManager.announceInChannel("🗒️ Loading playlist: " + (tracks.size() + 1) + " Songs 😎");
            if (!trackManager.getAudioPlayer().startTrack(starterTrack, true)) {
                tracks.add(starterTrack);
                trackManager.getQueue().addAll(tracks);
            } else {
                trackManager.getQueue().addAll(tracks);
            }
        }
    }

    @Override
    public void noMatches() {
        log.error("Error at finding track.");
        MessageEmbed errorEmbed = EmbedFactory.createUserErrorEmbed(new NotFoundMessage().getMessage());
        trackManager.announceInChannel(errorEmbed);
    }

    @Override
    public void loadFailed(FriendlyException e) {
        log.error("Error at loading track: {}", e.getMessage());
        MessageEmbed errorEmbed = EmbedFactory.createUserErrorEmbed(new FailedToLoadMessage().getMessage());
        trackManager.announceInChannel(errorEmbed);
    }
}
