package com.vaatu.bots.dixtro.config;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaatu.bots.dixtro.command.SlashCommand;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import jakarta.annotation.PreDestroy;

@Slf4j
@Configuration
public class DixtroConfig {

    @Value("${secrets.TOKEN}")
    private String token;

    private GatewayDiscordClient client;

    @Value("${secrets.DEV_SERVER}")
    private Long devServer;

    @PreDestroy
    public void logoutGateway() {
        System.out.println("Disconnecting client...");
        this.client.logout().block();
    }

    private void registerGuildCommands(GatewayDiscordClient client, Long applicationId, List<ApplicationCommandRequest> commands) {
        try {
            client.getRestClient().getApplicationService()
                    .bulkOverwriteGuildApplicationCommand(applicationId, devServer, commands)
                    .doOnNext(ignore -> log.info(ignore.name(), " loaded."))
                    .doOnError(e -> log.error(e.getMessage()))
                    .subscribe();
        } catch (NullPointerException exception) {
            log.error("Dixtro was not able to load guild commands.");
        }
    }

    private void registerGlobalCommands(GatewayDiscordClient client, Long applicationId) {
        try {
            ArrayList<ApplicationCommandRequest> globalCommands = new ArrayList<>();

            client.getRestClient().getApplicationService()
                    .bulkOverwriteGlobalApplicationCommand(applicationId, globalCommands)
                    .doOnNext(ignore -> log.info(ignore.name(), " globally loaded."))
                    .doOnError(e -> log.error(e.getMessage(), " globally"))
                    .subscribe();
        } catch (NullPointerException exception) {
            log.error("Dixtro was not able to update global commands.");
        }
    }

    @Bean
    public GatewayDiscordClient gatewayDiscordClient(final List<SlashCommand> localCommands) {
        GatewayDiscordClient client = DiscordClientBuilder.create(this.token)
                .build()
                .gateway()
                .setInitialPresence(ignore -> ClientPresence.doNotDisturb(ClientActivity.custom("Booting..."))).login()
                .block();

        assert client != null;
        Long applicationId = client.getRestClient().getApplicationId().block();

        ArrayList<ApplicationCommandRequest> commands = new ArrayList<>();

        for (SlashCommand command : localCommands) {
            ApplicationCommandRequest appCommand = ApplicationCommandRequest.builder()
                    .name(command.getName())
                    .description(command.getDescription())
                    .addAllOptions(command.getOptions())
                    .build();
            commands.add(appCommand);
        }

        registerGuildCommands(client, applicationId, commands);
        registerGlobalCommands(client, applicationId);

        this.client = client;

        return client;
    }

    @Bean
    public RestClient restClient(GatewayDiscordClient client) {
        return client.getRestClient();
    }

}
