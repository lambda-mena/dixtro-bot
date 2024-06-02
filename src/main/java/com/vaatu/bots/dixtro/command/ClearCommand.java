package com.vaatu.bots.dixtro.command;

import com.vaatu.bots.dixtro.audio.GuildTrackManager;
import com.vaatu.bots.dixtro.embed.EmbedFactory;
import com.vaatu.bots.dixtro.exception.BotNotInVoiceException;
import com.vaatu.bots.dixtro.exception.UserException;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ClearCommand implements ISlashCommand {
    private final TrackService trackService;

    @Override
    public String getDescription() {
        return "Clear the track queue.";
    }

    @Override
    public void execute(SlashCommandInteraction interaction) throws UserException {
        try {
            Guild guild = Objects.requireNonNull(interaction.getGuild());
            GuildTrackManager trackManager = trackService.getAudioManager(guild.getId());

            trackManager.getQueue().clear();
            MessageEmbed embed = EmbedFactory.createDefault("✅ Cleared!");
            interaction.getHook().sendMessageEmbeds(embed).queue();
        } catch (NoSuchElementException ex) {
            throw new BotNotInVoiceException();
        }
    }
}
