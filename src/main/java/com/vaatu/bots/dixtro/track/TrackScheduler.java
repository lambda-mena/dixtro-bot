package com.vaatu.bots.dixtro.track;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import lombok.Getter;

@Getter
public class TrackScheduler implements AudioLoadResultHandler {

    private final AudioPlayer player;
    private final TrackEventListener trackEventListener;

    public TrackScheduler(AudioPlayer player, TrackEventListener trackEventListener) {
        this.player = player;
        this.trackEventListener = trackEventListener;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        try {
            if (this.player.getPlayingTrack() == null) {
                player.playTrack(track);

            } else {
                this.trackEventListener.addTrackToQueue(track);
            }
        } catch (Exception e) {
            System.err.println("Error in single track load: " + e);
        }
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        try {
            AudioTrack firstTrack = playlist.getSelectedTrack();
            player.playTrack(firstTrack);
            playlist.getTracks().remove(firstTrack);

            for (AudioTrack track : playlist.getTracks()) {
                trackEventListener.addTrackToQueue(track);
            }
        } catch (Exception exception) {
            System.err.println("Error in playlist load: " + exception.getMessage());
        }
    }

    @Override
    public void noMatches() {
        // LavaPlayer did not find any audio to extract
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        System.out.println("Not able to load track. Exception: " + exception.getMessage());
    }

}
