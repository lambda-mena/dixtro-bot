package com.vaatu.bots.dixtro.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.AudioChannelJoinSpec;
import discord4j.voice.AudioProvider;
import discord4j.voice.VoiceConnection;
import reactor.core.publisher.Mono;

@Service
public class VoiceService {

    private VoiceConnection voiceConnection;
    private AudioProvider audioProvider;

    public VoiceService(AudioProvider audioProvider) {
        this.voiceConnection = null;
        this.audioProvider = audioProvider;
    }

    public Mono<Void> joinVoiceChannel(ChatInputInteractionEvent event) {
        Interaction interaction = event.getInteraction();
        Optional<Member> member = interaction.getMember();

        if (member.isPresent() && voiceConnection == null) {
            AudioChannelJoinSpec joinSpec = AudioChannelJoinSpec.builder().provider(audioProvider).build();

            this.voiceConnection = Mono.just(member.get())
                    .flatMap(Member::getVoiceState)
                    .flatMap(VoiceState::getChannel)
                    .flatMap(channel -> channel.join(joinSpec))
                    .block();
        }

        return Mono.empty();
    }

    public Mono<Void> leaveVoiceChannel() {
        if (this.voiceConnection != null) {
            this.voiceConnection.disconnect().block();
        }

        return Mono.empty();
    }

}
