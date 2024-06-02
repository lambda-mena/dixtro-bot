package com.vaatu.bots.dixtro.service;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TrackService {
    private final HashMap<String, GuildTrackManager> guildSendHandlers = new HashMap<>();

    public GuildTrackManager getAudioManager(String guildId) throws NoSuchElementException {
        Optional<GuildTrackManager> opt = Optional.ofNullable(this.guildSendHandlers.get(guildId));
        GuildTrackManager guildTrackManager = opt.orElseThrow();
        AudioManager audioManager = guildTrackManager.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            return guildTrackManager;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void removeAudioManager(String guildId) {
        this.guildSendHandlers.remove(guildId);
    }

    public GuildTrackManager createAudioManager(Guild guild, MessageChannelUnion channelUnion, AudioChannelUnion voiceChannel) {
        GuildTrackManager guildTrackManager = new GuildTrackManager(guild, channelUnion, voiceChannel);
        this.guildSendHandlers.put(guild.getId(), guildTrackManager);
        return guildTrackManager;
    }
}
