package com.vaatu.bots.dixtro.event;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.embed.EmbedFactory;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class VoiceEmptyEvent implements IVoiceEvent {

    private boolean isSelfPresent(JDA bot, AudioChannelUnion channelUnion) {
        Guild guild = channelUnion.getGuild();
        List<Member> members = channelUnion.getMembers();
        Member selfMember = guild.getMember(bot.getSelfUser());
        return members.contains(selfMember);
    }

    private boolean isVoiceEmpty(Guild guild) {
        try {
            AudioManager audioManager = guild.getAudioManager();
            AudioChannelUnion channelUnion = Objects.requireNonNull(audioManager.getConnectedChannel());
            List<Member> members = channelUnion.getMembers();
            return members.size() == 1 && isSelfPresent(guild.getJDA(), channelUnion);
        } catch (NullPointerException ex) {
            return false;
        }
    }

    @Override
    public void onTrigger(Guild guild, TrackService trackService) {
        if (!isVoiceEmpty(guild)) return;
        log.info("Bot is leaving voice since no one is there.");
        AudioManager audioManager = guild.getAudioManager();
        GuildTrackManager trackManager = trackService.getAudioManager(guild.getId());
        MessageEmbed embed = EmbedFactory.createDefault("😎 Leaving voice since no one is here!");
        trackManager.announceInChannel(embed);
        audioManager.closeAudioConnection();
    }
}
