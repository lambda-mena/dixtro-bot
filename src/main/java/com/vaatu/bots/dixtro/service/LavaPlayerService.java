package com.vaatu.bots.dixtro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import discord4j.core.spec.EmbedCreateFields;
import org.springframework.stereotype.Service;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import com.vaatu.bots.dixtro.provider.LavaPlayerAudioProvider;
import com.vaatu.bots.dixtro.track.TrackEventListener;
import com.vaatu.bots.dixtro.track.TrackScheduler;

import discord4j.voice.AudioProvider;
import lombok.Getter;

@Getter
@Service
public class LavaPlayerService {

    private final AudioProvider audioProvider;
    private final AudioPlayerManager playerManager;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final TrackEventListener trackEventListener;

    public LavaPlayerService() {
        final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        final AudioPlayer player = playerManager.createPlayer();
        AudioProvider provider = new LavaPlayerAudioProvider(player);

        this.playerManager = playerManager;
        this.audioPlayer = player;
        this.trackEventListener = new TrackEventListener();
        this.audioPlayer.addListener(this.trackEventListener);
        this.audioProvider = provider;
        this.trackScheduler = new TrackScheduler(player, this.trackEventListener);
    }

    public boolean canPlayTrack() {
        return this.audioPlayer.getPlayingTrack() == null && this.trackEventListener.getBlockingQueue().isEmpty();
    }

    public void playMusicURL(String s) {
        this.playerManager.loadItem(s, this.trackScheduler);
    }

    public void stopMusic() {
        this.audioPlayer.stopTrack();
    }

    public void clearMusicQueue() {
        this.trackEventListener.clearMusicQueue();
    }

    public String getCurrentTrack() throws NullPointerException {
        AudioTrack currentTrack = this.audioPlayer.getPlayingTrack();

        return currentTrack.getInfo().title;
    }

    public List<EmbedCreateFields.Field> getTrackQueue() {
        BlockingQueue<AudioTrack> trackQueue = this.trackEventListener.getBlockingQueue();
        List<EmbedCreateFields.Field> trackCollection = new ArrayList<>();
        int queuePosition = 1;

        for (AudioTrack track : trackQueue) {
            String trackTitle = track.getInfo().title;
            trackCollection.add(EmbedCreateFields.Field.of(String.valueOf(queuePosition), "```" + trackTitle + "```", false));
            queuePosition++;
        }

        return trackCollection;
    }

}
