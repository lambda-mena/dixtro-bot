package com.vaatu.bots.dixtro.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Getter
public class GuildTrackManager {
    private final Consumer<String> disconnectConsumer = str -> this.disconnectVoiceManager();
    private final Consumer<String> announceConsumer = this::announceTrackInChannel;
    private final AudioPlayerSendHandler audioPlayerSendHandler;
    private final AudioPlayerManager audioPlayerManager;
    private final LoadResultHandler loadResultHandler;
    private final BlockingQueue<AudioTrack> queue;
    private final MessageChannelUnion channelUnion;
    private final TrackScheduler trackScheduler;
    private final AudioPlayer audioPlayer;
    private final Guild guild;

    public GuildTrackManager(Guild guild, MessageChannelUnion messageChannelUnion) {
        this.channelUnion = messageChannelUnion;
        this.guild = guild;
        this.queue = new LinkedBlockingQueue<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager audioSourceManager = new YoutubeAudioSourceManager(true);
        this.audioPlayerManager.registerSourceManager(audioSourceManager);
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        this.audioPlayer = this.audioPlayerManager.createPlayer();
        this.trackScheduler = new TrackScheduler(queue, disconnectConsumer, announceConsumer);
        this.audioPlayer.addListener(this.trackScheduler);
        this.audioPlayerSendHandler = new AudioPlayerSendHandler(this.audioPlayer);
        this.loadResultHandler = new LoadResultHandler(queue, audioPlayer);
    }

    public void announceTrackInChannel(String trackTitle) {
        this.channelUnion.sendMessage("🎵 Playing: `" + trackTitle + "`").queue();
    }

    public void disconnectVoiceManager() {
        AudioManager audioManager = this.guild.getAudioManager();
        if (audioManager.isConnected() && audioPlayer.getPlayingTrack() == null) {
            audioManager.closeAudioConnection();
        }
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
        this.audioPlayerManager.loadItem(source, this.loadResultHandler);
    }
}
