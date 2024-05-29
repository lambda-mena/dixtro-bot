package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class BotAudioManager {
    private final BlockingQueue<AudioTrack> queue;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final AudioPlayerSendHandler audioPlayerSendHandler;

    public BotAudioManager() {
        this.queue = new LinkedBlockingQueue<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        this.audioPlayer = this.audioPlayerManager.createPlayer();
        this.trackScheduler = new TrackScheduler(queue);
        this.audioPlayer.addListener(this.trackScheduler);
        this.audioPlayerSendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }

    public void loadTrack(String source) {
        this.audioPlayerManager.loadItem(source, new LoadResultHandler(audioPlayer, this.queue));
    }
}
