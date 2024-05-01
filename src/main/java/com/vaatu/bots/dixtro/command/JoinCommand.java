package com.vaatu.bots.dixtro.command;

import org.springframework.stereotype.Component;

import com.vaatu.bots.dixtro.service.VoiceService;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionReplyEditSpec;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class JoinCommand implements SlashCommand {

    private VoiceService voiceService;

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Joins VC";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        try {
            event.reply("Joining...").withEphemeral(true).block();
            this.voiceService.joinVoiceChannel(event);

            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("✅ Connected")).then();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return event.reply("❌ Error").withEphemeral(true);
        }

    }

}
