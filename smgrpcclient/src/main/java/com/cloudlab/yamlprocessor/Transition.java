package com.cloudlab.yamlprocessor;

public class Transition {
    private String event; // Name of the event
    private String fromState; // Name of the source state
    private String toState; // Name of the target state

    /**
     * Builds a Transition object, all the attributes are assigned as null, by default
     */
    public Transition() {
        this.event = null;
        this.fromState = null;
        this.toState = null;
    }

    /**
     * Builds a Transition object by using parameters
     * @param event name of the event
     * @param fromState id of the source state
     * @param toState id of the target state
     */
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
