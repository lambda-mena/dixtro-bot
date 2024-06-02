package com.vaatu.bots.dixtro.listener;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class VoiceUpdateListener extends ListenerAdapter {
    private final TrackService trackService;

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        JDA bot = event.getJDA();
        Member member = event.getEntity();
        Optional<AudioChannelUnion> channelLeft = Optional.ofNullable(event.getChannelLeft());
        boolean isSelf = member.getUser().equals(bot.getSelfUser());

        if (isSelf && channelLeft.isPresent()) {
            Guild guild = event.getGuild();
            this.trackService.removeAudioManager(guild.getId());
            log.info("Bot has been disconnected from {}", event.getGuild().getName());
        }
    }
}
