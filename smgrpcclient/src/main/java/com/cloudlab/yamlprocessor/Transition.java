package com.cloudlab.yamlprocessor;

public class Transition {
    private String event;
    private String fromState;
    private String toState;

    public Transition() {
        this.event = null;
        this.fromState = null;
        this.toState = null;
    }

    public Transition(String event, String fromState, String toState) {
        this.event = event;
        this.fromState = fromState;
        this.toState = toState;
    }

    public String getEvent() {
        return event;
    }

    public String getFromState() {
        return fromState;
    }

    public String getToState() {
        return toState;
    }
}
