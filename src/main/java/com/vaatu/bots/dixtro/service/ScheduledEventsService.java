package com.vaatu.bots.dixtro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduledEventsService {

    @Scheduled(fixedRate = 1000 * 60)
    public void updateClientActivity() {
        log.info("Keeping bot alive...");
    }
}
