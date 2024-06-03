package com.vaatu.bots.dixtro.service;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.exception.BotInOtherVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrackService {
    private final HashMap<String, GuildTrackManager> guildSendHandlers = new HashMap<>();

    private boolean isMemberInTheVC(AudioManager audioManager, Member member) {
        AudioChannelUnion channelUnion = audioManager.getConnectedChannel();
        if (channelUnion != null) {
            List<Member> memberList = channelUnion.getMembers();
            return memberList.contains(member);
        } else {
            return false;
        }
    }

    public GuildTrackManager getAudioManager(String guildId) throws NoSuchElementException {
        Optional<GuildTrackManager> opt = Optional.ofNullable(this.guildSendHandlers.get(guildId));
        return opt.orElseThrow();
    }

    public GuildTrackManager getAudioManager(String guildId, Member member) throws NoSuchElementException, UserException {
        Optional<GuildTrackManager> opt = Optional.ofNullable(this.guildSendHandlers.get(guildId));
        GuildTrackManager guildTrackManager = opt.orElseThrow();
        AudioManager audioManager = guildTrackManager.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            if (isMemberInTheVC(audioManager, member)) {
                return guildTrackManager;
            } else {
                throw new BotInOtherVoiceException();
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    public void removeAudioManager(String guildId) {
        try {
            GuildTrackManager trackManager = this.getAudioManager(guildId);
            trackManager.destroyInstance();
            this.guildSendHandlers.remove(guildId);
        } catch (NoSuchElementException ex) {
            log.error("TrackManager not found");
        }
    }

    public GuildTrackManager createAudioManager(Guild guild, MessageChannelUnion channelUnion, AudioChannelUnion voiceChannel) {
        GuildTrackManager guildTrackManager = new GuildTrackManager(guild, channelUnion, voiceChannel);
        this.guildSendHandlers.put(guild.getId(), guildTrackManager);
        return guildTrackManager;
    }
}
