package com.vaatu.bots.dixtro.listener;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.vaatu.bots.dixtro.command.SlashCommand;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SlashCommandListener {

    private final Collection<SlashCommand> commands;

    public SlashCommandListener(List<SlashCommand> slashCommands, GatewayDiscordClient client) {
        this.commands = slashCommands;

        client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
    }

    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return Flux.fromIterable(commands)
            .filter(command -> command.getName().equals(event.getCommandName()))
            .next()
            .flatMap(command -> command.execute(event));
    }

}
