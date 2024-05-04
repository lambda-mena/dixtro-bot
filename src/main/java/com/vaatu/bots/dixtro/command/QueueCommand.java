package com.vaatu.bots.dixtro.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.stereotype.Component;

import com.vaatu.bots.dixtro.service.DiscordVoiceService;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class QueueCommand implements SlashCommand {

    private DiscordVoiceService voiceService;

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Allows you to visualize the current music queue";
    }

    @Override
    public Collection<ApplicationCommandOptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        event.reply("Loading song queue...").block();

        try {
            List<EmbedCreateFields.Field> currentQueue = this.voiceService.getMusicQueue();

            EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
            .title("Current Song: " + this.voiceService.getCurrentTrack());

            for (EmbedCreateFields.Field field : currentQueue) {
                embedBuilder.addField(field);
            }

            EmbedCreateSpec embed = embedBuilder.build();

            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull(null)
                    .withEmbeds(embed)).then();
        } catch (NullPointerException exception) {
            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("❌ Unable to load queue.")).then();
        }

    }

}
