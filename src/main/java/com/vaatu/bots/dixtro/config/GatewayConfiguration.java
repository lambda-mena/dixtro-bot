package com.vaatu.bots.dixtro.config;

import com.vaatu.bots.dixtro.command.IOptionsCommand;
import com.vaatu.bots.dixtro.command.ISlashCommand;
import com.vaatu.bots.dixtro.listener.CommandListener;
import com.vaatu.bots.dixtro.listener.ReadyListener;
import com.vaatu.bots.dixtro.listener.VoiceUpdateListener;
import com.vaatu.bots.dixtro.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class GatewayConfiguration {
    @Value("${TOKEN}")
    private String token;

    private String getCommandName(ISlashCommand command) {
        String className = command.getClass().getSimpleName();
        String commandName = className.split("Command")[0];
        return commandName.toLowerCase();
    }

    private List<CommandData> transformCommands(List<ISlashCommand> commands) {
        List<CommandData> gatewayCommands = new ArrayList<>();
        for (ISlashCommand command : commands) {
            String commandName = getCommandName(command);
            CommandDataImpl transformedCommand = new CommandDataImpl(commandName, command.getDescription());
            if (command instanceof IOptionsCommand) {
                transformedCommand.addOptions(((IOptionsCommand) command).getOptions());
            }
            gatewayCommands.add(transformedCommand);
            log.info("Registered command: {}", transformedCommand.getName());
        }
        return gatewayCommands;
    }

    @Bean
    public JDA jda(List<ISlashCommand> commands, TrackService trackService) {
        JDA bot = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES)
                .build();

        bot.updateCommands().addCommands(transformCommands(commands)).queue();

        Presence botPresence = bot.getPresence();
        botPresence.setPresence(OnlineStatus.IDLE, Activity.customStatus("Sealed away"));

        bot.addEventListener(new ReadyListener());
        bot.addEventListener(new VoiceUpdateListener(trackService));
        bot.addEventListener(new CommandListener(commands));
        return bot;
    }
}
