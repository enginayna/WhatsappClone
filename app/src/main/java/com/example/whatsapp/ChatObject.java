package com.example.whatsapp;

public class ChatObject {
    private String senderUID;
    private String textChat;

    public ChatObject(String senderUID, String textChat) {
        this.senderUID = senderUID;
        this.textChat = textChat;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getTextChat() {
        return textChat;
    }

    public void setTextChat(String textChat) {
        this.textChat = textChat;
    }
}
