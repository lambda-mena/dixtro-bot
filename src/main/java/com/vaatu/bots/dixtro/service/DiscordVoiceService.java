package com.vaatu.bots.dixtro.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.AudioChannelJoinSpec;
import discord4j.voice.VoiceConnection;
import lombok.Getter;
import reactor.core.publisher.Mono;

@Getter
@Service
public class DiscordVoiceService {

    private VoiceConnection voiceConnection;
    private LavaPlayerService playerService;

    public DiscordVoiceService(LavaPlayerService playerService) {
        this.voiceConnection = null;
        this.playerService = playerService;
    }

    public Mono<Void> joinVoiceChannel(ChatInputInteractionEvent event) {
        Interaction interaction = event.getInteraction();
        Optional<Member> member = interaction.getMember();

        if (member.isPresent() && !this.playerService.isPlaying()) {
            AudioChannelJoinSpec joinSpec = AudioChannelJoinSpec.builder().provider(playerService.getAudioProvider()).build();

            this.voiceConnection = Mono.just(member.get())
                    .flatMap(Member::getVoiceState)
                    .flatMap(VoiceState::getChannel)
                    .flatMap(channel -> channel.join(joinSpec))
                    .block();
        }

        return Mono.empty();
    }

    public Mono<Void> playSong(String url) {
        this.playerService.playMusicURL(url);

        return Mono.empty();
    }

    public Mono<Void> leaveVoiceChannel() {
        if (this.voiceConnection != null) {
            this.voiceConnection.disconnect().block();
        }

        return Mono.empty();
    }

}
