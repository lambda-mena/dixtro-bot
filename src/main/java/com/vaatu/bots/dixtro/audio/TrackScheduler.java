package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class TrackScheduler extends AudioEventAdapter {
    private final BlockingQueue<AudioTrack> queue;

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Optional<AudioTrack> nextTrack = Optional.ofNullable(queue.poll());
        if (nextTrack.isPresent() && endReason.mayStartNext) {
            player.startTrack(nextTrack.get(), false);
        }
    }
}
