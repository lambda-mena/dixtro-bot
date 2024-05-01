package com.vaatu.bots.dixtro.command;

import org.springframework.stereotype.Component;

import com.vaatu.bots.dixtro.service.VoiceService;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class DisconnectCommand implements SlashCommand {

    private VoiceService voiceService;

    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public String getDescription() {
        return "Leaves the current VC";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        try {
            this.voiceService.leaveVoiceChannel();

            return event.reply("✅ Disconnected").withEphemeral(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return event.reply("❌ Error").withEphemeral(true);
        }

    }

}