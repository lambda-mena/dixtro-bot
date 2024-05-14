package com.vaatu.bots.dixtro.command;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.google.api.services.youtube.model.SearchResult;
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
            new URI(source);
            return true;
        } catch (URISyntaxException e) {
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

    private String getSourceValue(ChatInputInteractionEvent event) throws Exception {
        Optional<ApplicationCommandInteractionOption> source = event.getOption("source");
        ApplicationCommandInteractionOption urlOption = source.orElseThrow(() -> new Exception("You need to pass a source or url."));

        Optional<ApplicationCommandInteractionOptionValue> urlValue = urlOption.getValue();
        urlValue.orElseThrow(() -> new Exception("Invalid Source"));

        return urlValue.get().asString();
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        event.reply("Loading...").block();

        try {
            String videoTitle;
            String videoSource = getSourceValue(event);

            if (!validateURL(videoSource)) {
                SearchResult videoResult = youtubeService.getVideoUrl(videoSource);
                videoTitle = videoResult.getSnippet().getTitle();
                videoSource = "https://www.youtube.com/watch?v=" + videoResult.getId().getVideoId();
            } else {
                videoTitle = youtubeService.getVideoTitle(videoSource);
            }

            this.voiceService.playSong(event, videoSource);

            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("✅ Playing: `` " + videoTitle + " ``")).then();
        } catch (Exception exception) {
            log.error(exception.toString());
            return event.editReply(InteractionReplyEditSpec.builder()
                    .build()
                    .withContentOrNull("❌ Internal Error")).then();
        }
        
    }

}
