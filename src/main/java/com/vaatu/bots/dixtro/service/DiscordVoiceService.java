package com.vaatu.bots.dixtro.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import discord4j.common.util.Snowflake;
import discord4j.core.spec.EmbedCreateFields;
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
    private final LavaPlayerService playerService;

    public DiscordVoiceService(LavaPlayerService playerService) {
        this.voiceConnection = null;
        this.playerService = playerService;
    }

    private Member getInteractionMember(ChatInputInteractionEvent event) throws NoSuchElementException {
        Interaction interaction = event.getInteraction();
        Optional<Member> member = interaction.getMember();

        return member.orElseThrow();
    }

    private Boolean canJoinVC() {
        return this.playerService.canPlayTrack();
    }

    private Boolean canPlayMusic(Member member) {
        VoiceState memberVoiceState = member.getVoiceState().block();

        if (this.voiceConnection == null || memberVoiceState == null) {
            return false;
        }

        Snowflake botChannelId = this.voiceConnection.getChannelId().block();
        Snowflake channelId = memberVoiceState.getChannelId().orElseThrow();

        return channelId.equals(botChannelId);
    }

    public Boolean joinVoiceChannel(ChatInputInteractionEvent event) {
        Member interactionMember = getInteractionMember(event);

        if (canJoinVC()) {
            AudioChannelJoinSpec joinSpec = AudioChannelJoinSpec.builder().provider(playerService.getAudioProvider())
                    .build();

            this.voiceConnection = Mono.just(interactionMember)
                    .flatMap(Member::getVoiceState)
                    .flatMap(VoiceState::getChannel)
                    .flatMap(channel -> channel.join(joinSpec))
                    .block();

            return true;
        }

        return false;
    }

    public void playSong(ChatInputInteractionEvent event, String url) throws Exception {
        Member interactionMember = getInteractionMember(event);

        if (!canPlayMusic(interactionMember)) {
            if (joinVoiceChannel(event)) {
                this.playerService.playMusicURL(url);
            } else {
                throw new Exception("The bot is already in use in another VC.");
            }
        } else {
            this.playerService.playMusicURL(url);
        }
    }

    public void clearMusicQueue() {
        this.playerService.clearMusicQueue();
    }

    public void skipCurrentTrack() {
        this.playerService.skipMusic();
    }

    public String getCurrentTrack() {
        try {
            return this.playerService.getCurrentTrack();
        } catch (Exception exception) {
            return "Nothing is playing right now.";
        }
    }

    public List<EmbedCreateFields.Field> getMusicQueue() {
        List<EmbedCreateFields.Field> fieldList = this.playerService.getTrackQueue();
        fieldList = fieldList.subList(0, Math.min(fieldList.size(), 5));

        return fieldList;
    }

    public void leaveVoiceChannel() throws NullPointerException {
        this.voiceConnection.disconnect().block();
        this.playerService.clearMusicQueue();
        this.playerService.stopMusic();
        this.voiceConnection = null;
    }

}
