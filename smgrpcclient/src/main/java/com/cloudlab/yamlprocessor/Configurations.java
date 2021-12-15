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

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

}
