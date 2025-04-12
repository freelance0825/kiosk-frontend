package com.fmv.healthkiosk.feature.telemedicine.domain.model;

public class ChatMessage {
    private String message;
    private boolean isFromUser; // true = user, false = assistant

    public ChatMessage(String message, boolean isFromUser) {
        this.message = message;
        this.isFromUser = isFromUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFromUser() {
        return isFromUser;
    }
}
