package com.vaatu.bots.dixtro.exception;

public class UserNotInVoiceException extends UserException{
    @Override
    public String getMessage() {
        return "❌ User is not in a voice channel.";
    }
}
