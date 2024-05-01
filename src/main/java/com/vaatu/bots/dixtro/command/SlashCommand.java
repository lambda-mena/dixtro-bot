package com.vaatu.bots.dixtro.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public interface SlashCommand {

    public String getName();

    public String getDescription();

    public Mono<Void> execute(ChatInputInteractionEvent event);

}
