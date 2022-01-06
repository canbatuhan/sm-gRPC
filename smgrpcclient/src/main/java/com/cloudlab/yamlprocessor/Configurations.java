package com.cloudlab.yamlprocessor;

import java.util.ArrayList;

public class Configurations {
    private String machineID; // Machine ID, preferably unique for each machine
    private boolean autoStartup; // Whether if the state machine start automatically or not.
    private ArrayList<State> states; // States of the state machine
    private ArrayList<Transition> transitions; // Transitions in the state machine

    /**
     * Builds a Configurations object to read state machine configuration details
     */
    public Configurations() {
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
    }

    public String getMachineID() {
        return machineID;
    }

    public boolean isAutoStartup() {
        return autoStartup;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<Transition> getTransitions() { return transitions; }

    public ArrayList<String> getReadVariables(String fromState, String event) {
        String toState;

        // Traverse all transitions, until find the fromState and event
        for (Transition transition : this.transitions) {
            if (transition.getEvent().equals(event) && transition.getFromState().equals(fromState)) {
                // Traverse in states, until find the toState, then get readVariable
                toState = transition.getToState();
                for (State state : this.states) {
                    if (state.getName().equals(toState)) {
                        return state.getReadVariables();
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    public ArrayList<String> getReadVariables(String currentState) {
        // traverse all states, until find the current state
        for (State state : this.states) {
            if (state.getName().equals(currentState)) {
                return state.getReadVariables();
            }
        }

        return new ArrayList<>();
    }

    public ArrayList<String> getWriteVariables(String fromState, String event) {
        String toState;

        // Traverse all transitions, until find the fromState and event
        for (Transition transition : this.transitions) {
            if (transition.getEvent().equals(event) && transition.getFromState().equals(fromState)) {
                // Traverse in states, until find the toState, then get writeVariable
                toState = transition.getToState();
                for (State state : this.states) {
                    if (state.getName().equals(toState)) {
                        return state.getWriteVariables();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<String> getWriteVariables(String currentState) {
        // traverse all states, until find the current state
        for (State state : this.states) {
            if (state.getName().equals(currentState)) {
                return state.getWriteVariables();
            }
        }

        return new ArrayList<>();
    }

    public String getToState(String fromState, String event) {
        for (Transition transition : this.transitions) {
            if (transition.getFromState().equals(fromState) && transition.getEvent().equals(event)) {
                return transition.getToState();
            }
        }

        return "CAN NOT FOUND";
    }
}
