package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class TrackScheduler extends AudioEventAdapter {
    private final BlockingQueue<AudioTrack> queue;
    private final Consumer<String> closeConnection;
    private final Consumer<String> announceTrack;

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        this.announceTrack.accept(track.getInfo().title);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Optional<AudioTrack> nextTrack = Optional.ofNullable(queue.poll());
        if (nextTrack.isPresent() && (endReason.mayStartNext)) {
            player.startTrack(nextTrack.get(), false);
        } else {
            closeConnection.accept("");
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        boolean lastSong = this.queue.isEmpty();
        if (exception.severity.equals(FriendlyException.Severity.SUSPICIOUS) && lastSong) {
            closeConnection.accept("");
        }
    }
}
