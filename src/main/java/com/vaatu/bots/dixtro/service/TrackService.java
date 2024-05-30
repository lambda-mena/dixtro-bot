package com.vaatu.bots.dixtro.service;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TrackService {
    private final HashMap<String, GuildTrackManager> guildSendHandlers = new HashMap<>();

    public Optional<GuildTrackManager> getAudioManager(String guildId) {
        return Optional.ofNullable(this.guildSendHandlers.get(guildId));
    }

    public void removeAudioManager(String guildId) {
        this.guildSendHandlers.remove(guildId);
    }

    public GuildTrackManager createAudioManager(Guild guild) {
        GuildTrackManager guildTrackManager = new GuildTrackManager(guild);
        this.guildSendHandlers.put(guild.getId(), guildTrackManager);
        return guildTrackManager;
    }
}