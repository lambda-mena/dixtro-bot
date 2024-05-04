package com.vaatu.bots.dixtro.command;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Component;

import com.vaatu.bots.dixtro.service.DiscordVoiceService;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class JoinCommand implements SlashCommand {

    private DiscordVoiceService voiceService;

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Join the voice channel";
    }

    @Override
    public Collection<ApplicationCommandOptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        try {
            event.reply("Joining...").block();

            this.voiceService.joinVoiceChannel(event);

            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("✅ Connected")).then();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("❌ Unable to connect")).then();
        }

    }

}
