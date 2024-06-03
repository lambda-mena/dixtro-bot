package com.vaatu.bots.dixtro.service;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.exception.BotInOtherVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class TrackService {
    private final HashMap<String, GuildTrackManager> guildSendHandlers = new HashMap<>();

    private boolean isMemberInTheVC(AudioManager audioManager, Member member) {
        AudioChannelUnion channelUnion = audioManager.getConnectedChannel();
        if (channelUnion != null) {
            List<Member> memberList = channelUnion.getMembers();
            int index = memberList.indexOf(member);
            return index >= 0;
        } else {
            return false;
        }
    }

    public GuildTrackManager getAudioManager(String guildId, Member member) throws NoSuchElementException, UserException {
        Optional<GuildTrackManager> opt = Optional.ofNullable(this.guildSendHandlers.get(guildId));
        GuildTrackManager guildTrackManager = opt.orElseThrow();
        AudioManager audioManager = guildTrackManager.getGuild().getAudioManager();

        if (audioManager.isConnected() && isMemberInTheVC(audioManager, member)) {
            return guildTrackManager;
        } else {
            throw new BotInOtherVoiceException();
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
