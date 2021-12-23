package com.cloudlab.yamlprocessor;

import java.util.ArrayList;

public class Configurations {
    private String machineID;
    private boolean autoStartup;
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;

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



    public ArrayList<Variable> getReadVariables(String fromState, String event) {
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

        return null;
    }

    public ArrayList<Variable> getWriteVariables(String fromState, String event) {
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
        return null;
    }

}
