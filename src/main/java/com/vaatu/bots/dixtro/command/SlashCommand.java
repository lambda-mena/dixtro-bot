package com.vaatu.bots.dixtro.command;

import java.util.Collection;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import reactor.core.publisher.Mono;

public interface SlashCommand {

    String getName();

    String getDescription();

    Collection<ApplicationCommandOptionData> getOptions();

    Mono<Void> execute(ChatInputInteractionEvent event);

}
