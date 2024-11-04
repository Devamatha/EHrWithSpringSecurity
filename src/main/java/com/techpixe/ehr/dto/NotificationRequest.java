package com.techpixe.ehr.dto;

import java.util.List;

public class NotificationRequest {
    private List<Long> receivers;
    private String messageContent;
    private String messageType;

    public NotificationRequest(List<Long> receivers, String messageContent, String messageType) {
        super();
        this.receivers = receivers;
        this.messageContent = messageContent;
        this.messageType = messageType;
    }

    public NotificationRequest() {
        super();
    }

    public List<Long> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Long> receivers) {
        this.receivers = receivers;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "NotificationRequest [receivers=" + receivers + ", messageContent=" + messageContent + ", messageType="
                + messageType + "]";
    }


}