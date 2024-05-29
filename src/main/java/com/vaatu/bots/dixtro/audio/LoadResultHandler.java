package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class LoadResultHandler implements AudioLoadResultHandler {
    private final AudioPlayer audioPlayer;
    private final BlockingQueue<AudioTrack> queue;

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        log.info("Song: {} Loaded", audioTrack.getInfo().title);
        if (!audioPlayer.startTrack(audioTrack, true)) {
            queue.add(audioTrack);
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {

    }

    @Override
    public void noMatches() {
        log.error("Unable to find this match.");
    }

    @Override
    public void loadFailed(FriendlyException e) {
        log.error("Unable to load this track.");
    }
}
