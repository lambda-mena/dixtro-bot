package com.vaatu.bots.dixtro.command;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Component;

import com.vaatu.bots.dixtro.service.DiscordVoiceService;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class DisconnectCommand implements SlashCommand {

    private DiscordVoiceService voiceService;

    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public String getDescription() {
        return "Leaves the current VC";
    }

    @Override
    public Collection<ApplicationCommandOptionData> getOptions() {
        return new ArrayList<ApplicationCommandOptionData>();
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