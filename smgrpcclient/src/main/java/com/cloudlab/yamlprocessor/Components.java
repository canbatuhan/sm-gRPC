package com.cloudlab.yamlprocessor;

import java.util.ArrayList;

public class Components {
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;

    public Components() {
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }
}
