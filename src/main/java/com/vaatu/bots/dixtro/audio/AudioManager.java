package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class AudioManager {
    private final BlockingQueue<AudioTrack> queue;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final AudioPlayerSendHandler audioPlayerSendHandler;

    public AudioManager() {
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        this.audioPlayer = this.audioPlayerManager.createPlayer();
        this.trackScheduler = new TrackScheduler();
        this.audioPlayer.addListener(this.trackScheduler);
        this.audioPlayerSendHandler = new AudioPlayerSendHandler(this.audioPlayer);
        this.queue = new LinkedBlockingQueue<>();
    }

    public void loadTrack(String source) {
        this.audioPlayerManager.loadItem(source, new LoadResultHandler(audioPlayer, this.queue));
    }
}
