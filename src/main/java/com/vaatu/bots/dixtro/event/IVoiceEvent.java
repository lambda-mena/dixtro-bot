package com.vaatu.bots.dixtro.event;

import com.vaatu.bots.dixtro.service.TrackService;
import net.dv8tion.jda.api.entities.Guild;

public interface IVoiceEvent {
    void onTrigger(Guild guild, TrackService trackService);
}
