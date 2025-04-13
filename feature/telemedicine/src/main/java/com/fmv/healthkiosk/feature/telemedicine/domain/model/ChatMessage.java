package com.fmv.healthkiosk.feature.telemedicine.domain.model;

public class ChatMessage {
    private int id;
    private String message;
    private boolean isFromUser; // true = user, false = assistant

    public ChatMessage(int id, String message, boolean isFromUser) {
        this.id = id;
        this.message = message;
        this.isFromUser = isFromUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFromUser() {
        return isFromUser;
    }

    public int getId() {
        return this.id;
    }
}
