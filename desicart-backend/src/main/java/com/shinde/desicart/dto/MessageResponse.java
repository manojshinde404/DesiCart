package com.shinde.desicart.dto;

public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
