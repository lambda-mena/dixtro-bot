package com.vaatu.bots.dixtro.service;

import com.vaatu.bots.dixtro.audio.AudioManager;
import com.vaatu.bots.dixtro.audio.AudioPlayerSendHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ManagerService {
    private final HashMap<String, AudioManager> guildSendHandlers = new HashMap<>();

    public AudioManager getAudioManager(String guildId) {
        return this.guildSendHandlers.get(guildId);
    }

    public AudioPlayerSendHandler createAudioManager(String guildId) {
        AudioManager audioManager = new AudioManager();
        this.guildSendHandlers.put(guildId, audioManager);
        return audioManager.getAudioPlayerSendHandler();
    }
}
