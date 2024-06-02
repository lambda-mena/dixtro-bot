package com.vaatu.bots.dixtro.exception;

public class BotInOtherVoiceException extends UserException {
    @Override
    public String getMessage() {
        return "❌ Bot is in another voice channel.";
    }
}
