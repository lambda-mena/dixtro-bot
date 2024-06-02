package com.vaatu.bots.dixtro.message;

public class NotFoundMessage implements IBotMessage{
    @Override
    public String getMessage() {
        return "❌ Song not found";
    }
}
