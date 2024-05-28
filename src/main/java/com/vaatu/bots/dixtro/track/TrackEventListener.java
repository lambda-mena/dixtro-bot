package com.vaatu.bots.dixtro.track;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import lombok.Getter;

@Getter
public class TrackEventListener extends AudioEventAdapter {

    private final BlockingQueue<AudioTrack> blockingQueue = new LinkedBlockingQueue<>();

    public void addTrackToQueue(AudioTrack track) {
        this.blockingQueue.add(track);
    }

    public void clearMusicQueue() {
        this.blockingQueue.clear();
    }

    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        System.out.println("Unable to play... " + track.getInfo().title);
    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        try {
            if (!this.blockingQueue.isEmpty()) {
                AudioTrack nextTrack = this.blockingQueue.take();
                player.playTrack(nextTrack);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        System.out.printf("Error at loading track... %s%n", track.getInfo().title);
    }

}
