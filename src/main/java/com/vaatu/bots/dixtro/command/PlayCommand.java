package com.vaatu.bots.dixtro.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vaatu.bots.dixtro.service.DiscordVoiceService;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class PlayCommand implements SlashCommand {

    private DiscordVoiceService voiceService;

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "plays a single song.";
    }

    @Override
    public Collection<ApplicationCommandOptionData> getOptions() {
        ArrayList<ApplicationCommandOptionData> optionList = new ArrayList<ApplicationCommandOptionData>();

        ApplicationCommandOptionData playURL = ApplicationCommandOptionData
                .builder()
                .name("url")
                .description("url of the song.")
                .type(ApplicationCommandOption.Type.STRING.getValue())
                .required(true)
                .build();

        optionList.add(playURL);

        return optionList;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        try {
            event.reply("Loading...").withEphemeral(true).block();

            Optional<ApplicationCommandInteractionOption> url = event.getOption("url");

            if (voiceService.getVoiceConnection() == null) {
                voiceService.joinVoiceChannel(event);
            }

            url.ifPresent(option -> {
                option.getValue().ifPresent(value -> {
                    voiceService.playSong(value.asString());
                });
            });

            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("✅ Connected & Playing")).then();
        } catch (Exception e) {
            return event.reply("Error at trying to play music...").withEphemeral(true);
        }
        
    }

}
