package com.vaatu.bots.dixtro.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
public class ReadyListener extends ListenerAdapter {
    // TODO: Register on-development commands.

    @Override
    public void onReady(ReadyEvent event) {
        JDA bot = event.getJDA();

        log.info("{} is ready.", bot.getSelfUser().getName());
    }
}
