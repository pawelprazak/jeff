package com.bluecatcode.jeff.notifier;

public class Event {

    private String targetClassName;
    private String targetMethodName;
    private EventType eventType;

    public Event(String targetClassName, String targetMethodName, EventType eventType) {
        this.targetClassName = targetClassName;
        this.targetMethodName = targetMethodName;
        this.eventType = eventType;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public String getTargetMethodName() {
        return targetMethodName;
    }

    public void setTargetMethodName(String targetMethodName) {
        this.targetMethodName = targetMethodName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
