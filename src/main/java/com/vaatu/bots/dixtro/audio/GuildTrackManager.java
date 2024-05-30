package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Getter
public class GuildTrackManager {
    private final BlockingQueue<AudioTrack> queue;
    private final AudioPlayerManager audioPlayerManager;
    private final Guild guild;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final AudioPlayerSendHandler audioPlayerSendHandler;

    public GuildTrackManager(Guild guild) {
        this.guild = guild;
        this.queue = new LinkedBlockingQueue<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        this.audioPlayer = this.audioPlayerManager.createPlayer();
        Consumer<String> action = str -> this.disconnectVoiceManager();
        this.trackScheduler = new TrackScheduler(queue, action);
        this.audioPlayer.addListener(this.trackScheduler);
        this.audioPlayerSendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }

    public void disconnectVoiceManager() {
        AudioManager audioManager = this.guild.getAudioManager();
        audioManager.closeAudioConnection();
    }

    public void skipTrack() {
        AudioTrack nextTrack = queue.poll();
        if (nextTrack != null) {
            this.audioPlayer.startTrack(nextTrack, false);
        } else {
            this.audioPlayer.stopTrack();
        }
    }

    public void loadTrack(String source) {
        this.audioPlayerManager.loadItem(source, new LoadResultHandler(audioPlayer, this.queue));
    }
}
