package com.vaatu.bots.dixtro.service;

import org.springframework.stereotype.Service;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import com.vaatu.bots.dixtro.player.LavaPlayerAudioProvider;
import com.vaatu.bots.dixtro.track.TrackEventListener;
import com.vaatu.bots.dixtro.track.TrackScheduler;

import discord4j.voice.AudioProvider;
import lombok.Getter;

@Getter
@Service
public class LavaPlayerService {

    private AudioProvider audioProvider;
    private AudioPlayerManager playerManager;
    private AudioPlayer audioPlayer;
    private TrackScheduler trackScheduler;
    private TrackEventListener trackEventListener;

    public LavaPlayerService() {
        final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

        AudioSourceManagers.registerRemoteSources(playerManager);

        final AudioPlayer player = playerManager.createPlayer();

        AudioProvider provider = new LavaPlayerAudioProvider(player);
        
        this.playerManager = playerManager;
        this.audioPlayer = player;
        this.trackEventListener = new TrackEventListener();
        this.audioPlayer.addListener(this.trackEventListener);
        this.audioProvider = provider;
        this.trackScheduler = new TrackScheduler(player, this.trackEventListener);
    }

    public boolean isPlaying() {
        if (this.audioPlayer.getPlayingTrack() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void playMusicURL(String s) {
        this.playerManager.loadItem(s, this.trackScheduler);
    }

}
