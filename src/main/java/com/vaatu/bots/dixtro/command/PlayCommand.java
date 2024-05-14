package com.vaatu.bots.dixtro.command;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.vaatu.bots.dixtro.service.YoutubeService;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.vaatu.bots.dixtro.service.DiscordVoiceService;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Component
public class PlayCommand implements SlashCommand {

    private DiscordVoiceService voiceService;
    private YoutubeService youtubeService;

    private boolean validateURL(String source) {
        try {
            new URL(source).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "loads a single track or a playlist in your vc!";
    }

    @Override
    public Collection<ApplicationCommandOptionData> getOptions() {
        ArrayList<ApplicationCommandOptionData> optionList = new ArrayList<>();

        ApplicationCommandOptionData sourceOption = ApplicationCommandOptionData
                .builder()
                .name("source")
                .description("url/source of the song.")
                .type(ApplicationCommandOption.Type.STRING.getValue())
                .required(true)
                .build();

        optionList.add(sourceOption);

        return optionList;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        event.reply("Loading...").block();

        try {
            Optional<ApplicationCommandInteractionOption> source = event.getOption("source");
            ApplicationCommandInteractionOption urlOption = source.orElseThrow(() -> new Exception("You need to pass a source or url."));

            Optional<ApplicationCommandInteractionOptionValue> urlValue = urlOption.getValue();
            urlValue.orElseThrow(() -> new Exception("Invalid Source"));

            String videoSource = urlValue.get().asString();

            if (!validateURL(videoSource)) {
                String videoId = youtubeService.getVideoUrl(videoSource);
                videoSource = "https://www.youtube.com/watch?v=" + videoId;
            }

            this.voiceService.playSong(event, videoSource);

            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("✅ Playing")).then();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("❌ Internal Error")).then();
        }
        
    }

}
