package com.vaatu.bots.dixtro.track;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackEventListener extends AudioEventAdapter {

    private final BlockingQueue<AudioTrack> blockingQueue = new LinkedBlockingQueue<>();

    public void addTrackToQueue(AudioTrack track) {
        this.blockingQueue.add(track);
    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        try {
            if (this.blockingQueue.size() > 0) {
                AudioTrack nextTrack = this.blockingQueue.take();
                player.playTrack(nextTrack);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
