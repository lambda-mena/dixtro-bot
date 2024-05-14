package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.service.DiscordVoiceService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class ClearCommand implements SlashCommand {

    private DiscordVoiceService voiceService;

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Clears the current music queue";
    }

    @Override
    public Collection<ApplicationCommandOptionData> getOptions() {
        return List.of();
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        try {
            event.reply("Clearing queue...").block();

            this.voiceService.clearMusicQueue();

            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("✅ Cleared")).then();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("❌ Unable to clear queue")).then();
        }
    }
}
