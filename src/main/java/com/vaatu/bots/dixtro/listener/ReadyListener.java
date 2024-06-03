package com.vaatu.bots.dixtro.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        JDA bot = event.getJDA();
        log.info("{} is ready.", bot.getSelfUser().getName());
    }
}
