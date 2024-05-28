package com.vaatu.bots.dixtro.config;

import com.vaatu.bots.dixtro.command.IExecuteCommand;
import com.vaatu.bots.dixtro.listener.CommandListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class BotConfiguration {

    @Value("${TOKEN}")
    private String token;

    @Bean
    public JDA jda(List<IExecuteCommand> commands) {
        JDA bot = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES)
                .build();

        bot.upsertCommand("join", "Joins your voice channel").queue();
        bot.upsertCommand("disconnect", "Disconnects from the current voice channel").queue();
        bot.updateCommands().addCommands(
                Commands.slash("play", "Plays a song.")
                        .addOption(OptionType.STRING, "source", "keyword of the song.")
        ).queue();

        bot.addEventListener(new CommandListener(commands));
        return bot;
    }
}
