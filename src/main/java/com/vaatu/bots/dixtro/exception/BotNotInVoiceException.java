package com.vaatu.bots.dixtro.exception;

public class BotNotInVoiceException extends UserException{
    @Override
    public String getMessage() {
        return "Bot is not in a voice channel.";
    }
}
