package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class LoadResultHandler implements AudioLoadResultHandler {
    private final BlockingQueue<AudioTrack> queue;
    private final AudioPlayer audioPlayer;

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        log.info("Song: {} Loaded", audioTrack.getInfo().title);
        if (!audioPlayer.startTrack(audioTrack, true)) {
            queue.add(audioTrack);
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        List<AudioTrack> tracks = audioPlaylist.getTracks();
        AudioTrack starterTrack = tracks.removeFirst();
        if (!audioPlayer.startTrack(starterTrack, true)) {
            tracks.add(starterTrack);
            queue.addAll(tracks);
        } else {
            queue.addAll(tracks);
        }
    }

    @Override
    public void noMatches() {
        log.error("Error at finding track.");
    }

    @Override
    public void loadFailed(FriendlyException e) {
        log.error("Error at loading track: {}", e.getMessage());
    }
}
