package com.vaatu.bots.dixtro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduledEventsService {

    private int timeOnline = 0;

    @Scheduled(fixedRate = 1000 * 60)
    public void updateClientActivity() {
        timeOnline = timeOnline + 1;
        log.info("{} minutes alive", this.timeOnline);
    }
}
