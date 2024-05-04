package com.vaatu.bots.dixtro.service;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Slf4j
@Service
public class ScheduledEventsService {

    private final List<String> clientStates = Arrays.asList("Catching some food.", "Sealed away in the depths",
            "Waiting for the new dungeon lord...");
    private final GatewayDiscordClient client;

    @Scheduled(fixedRate = 1000 * 60)
    public void updateClientActivity() {
        try {
            int max = 2;
            int min = 0;
            int randomNumber = (int) (Math.random() * (max - min + 1)) + min;

            client.updatePresence(ClientPresence.idle(ClientActivity.custom(this.clientStates.get(randomNumber))))
                    .block();
        } catch (IndexOutOfBoundsException exception) {
            log.error("Unable to get a number");
        }
    }

}
