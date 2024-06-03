package com.vaatu.bots.dixtro.event;

import com.vaatu.bots.dixtro.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VoiceLeftEvent implements IVoiceEvent {
    @Override
    public void onTrigger(Guild guild, TrackService trackService) {
        log.info("Bot disconnected from voice channel.");
        trackService.removeAudioManager(guild.getId());
    }
}
