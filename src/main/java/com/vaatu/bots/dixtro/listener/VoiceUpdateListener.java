package com.vaatu.bots.dixtro.listener;

import com.vaatu.bots.dixtro.event.VoiceLeftEvent;
import com.vaatu.bots.dixtro.event.VoiceEmptyEvent;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class VoiceUpdateListener extends ListenerAdapter {
    private final VoiceEmptyEvent voiceEmptyEvent;
    private final VoiceLeftEvent voiceLeftEvent;
    private final TrackService trackService;

    private boolean isSelfEvent(GuildVoiceUpdateEvent event) {
        JDA bot = event.getJDA();
        Member member = event.getEntity();
        return member.getUser().equals(bot.getSelfUser());
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Optional<AudioChannelUnion> channelLeft = Optional.ofNullable(event.getChannelLeft());

        if (channelLeft.isPresent()) {
            if (isSelfEvent(event)) {
                voiceLeftEvent.onTrigger(event.getGuild(), trackService);
            } else {
                voiceEmptyEvent.onTrigger(event.getGuild(), trackService);
            }
        }
    }
}
