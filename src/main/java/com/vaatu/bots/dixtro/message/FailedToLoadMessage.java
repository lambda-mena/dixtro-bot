package com.vaatu.bots.dixtro.message;

public class FailedToLoadMessage implements IBotMessage {
    @Override
    public String getMessage() {
        return "❌ Failed to load song.";
    }
}
