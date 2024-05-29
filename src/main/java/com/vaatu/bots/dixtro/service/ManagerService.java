package com.vaatu.bots.dixtro.service;

import com.vaatu.bots.dixtro.audio.BotAudioManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class ManagerService {
    private final HashMap<String, BotAudioManager> guildSendHandlers = new HashMap<>();

    public Optional<BotAudioManager> getAudioManager(String guildId) {
        return Optional.ofNullable(this.guildSendHandlers.get(guildId));
    }

    public BotAudioManager createAudioManager(String guildId) {
        BotAudioManager botAudioManager = new BotAudioManager();
        this.guildSendHandlers.put(guildId, botAudioManager);
        return botAudioManager;
    }
}
