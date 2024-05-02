package com.vaatu.bots.dixtro.command;

import java.util.Collection;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import reactor.core.publisher.Mono;

public interface SlashCommand {

    public String getName();

    public String getDescription();

    public Collection<ApplicationCommandOptionData> getOptions();

    public Mono<Void> execute(ChatInputInteractionEvent event);

}
