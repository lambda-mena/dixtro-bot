package com.vaatu.bots.dixtro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import com.vaatu.bots.dixtro.player.LavaPlayerAudioProvider;

import discord4j.voice.AudioProvider;

@Configuration
public class PlayerConfig {

    @Bean
    public AudioProvider audioProvider() {
        final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

        AudioSourceManagers.registerRemoteSources(playerManager);

        final AudioPlayer player = playerManager.createPlayer();

        AudioProvider provider = new LavaPlayerAudioProvider(player);

        return provider;
    }

}
