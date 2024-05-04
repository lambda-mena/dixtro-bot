package com.vaatu.bots.dixtro.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaatu.bots.dixtro.command.SlashCommand;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import jakarta.annotation.PreDestroy;

@Configuration
public class DixtroConfig {

    @Value("${token}")
    private String token;

    private GatewayDiscordClient client = null;

    @Value("${devServer}")
    private Long devServer;

    @PreDestroy
    public void logoutGateway() {
        System.out.println("Disconnecting client...");
        this.client.logout().block();
    }

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(final List<SlashCommand> localCommands) {
        GatewayDiscordClient client = DiscordClientBuilder.create(this.token)
                .build()
                .gateway()
                .setInitialPresence(ignore -> ClientPresence.doNotDisturb(ClientActivity.custom("Booting..."))).login()
                .block();

        Long applicationId = client.getRestClient().getApplicationId().block();

        ArrayList<ApplicationCommandRequest> commands = new ArrayList<>();

        // Translate from SlashCommand interface to AppCommandRequest.
        for (SlashCommand command : localCommands) {
            ApplicationCommandRequest appCommand = ApplicationCommandRequest.builder()
                    .name(command.getName())
                    .description(command.getDescription())
                    .addAllOptions(command.getOptions())
                    .build();

            commands.add(appCommand);
        }

        // Add Dev guild commands to the bot.
        client.getRestClient().getApplicationService()
                .bulkOverwriteGuildApplicationCommand(applicationId, devServer, commands)
                .doOnNext(ignore -> System.out.println(String.format("%s loaded.", ignore.name())))
                .doOnError(e -> System.out.println(e.getMessage()))
                .subscribe();

        ArrayList<ApplicationCommandRequest> globalCommands = new ArrayList<>();

        // Add Global commands to the bot.
        client.getRestClient().getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(applicationId, globalCommands)
                .doOnNext(ignore -> System.out.println("Global commands loaded."))
                .doOnError(e -> System.out.println(e.getMessage()))
                .subscribe();

        this.client = client;

        return client;
    }

    @Bean
    public RestClient restClient(GatewayDiscordClient client) {
        return client.getRestClient();
    }

}
