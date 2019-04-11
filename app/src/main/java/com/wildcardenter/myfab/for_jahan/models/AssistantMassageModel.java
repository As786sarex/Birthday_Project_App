package com.wildcardenter.myfab.for_jahan.models;

public class AssistantMassageModel {
    String massage;
    boolean isMe;

    public AssistantMassageModel(String massage, boolean isMe) {
        this.massage = massage;
        this.isMe = isMe;
    }

    public AssistantMassageModel() {
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
