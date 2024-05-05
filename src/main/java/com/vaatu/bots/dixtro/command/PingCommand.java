package com.vaatu.bots.dixtro.command;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Component;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class PingCommand implements SlashCommand {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Ping Pong!";
    }

    @Override
    public Collection<ApplicationCommandOptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        event.reply("Pong!").withEphemeral(true).block();

        return event.editReply(InteractionReplyEditSpec.builder()
                .build()
                .withContentOrNull("Pong Pong Pong!")).then();
    }

}
