package com.cloudlab.yamlprocessor;

import java.util.ArrayList;
import java.util.Arrays;

public class State {
    private String name;
    private ArrayList<Variable> readVariables;
    private ArrayList<Variable> writeVariables;

    public State() {
        this.name = null;
        this.readVariables = null;
        this.writeVariables = null;
    }

    public State(String name, ArrayList<Variable> readVariables, ArrayList<Variable>  writeVariables) {
        this.name = name;
        this.readVariables = readVariables;
        this.writeVariables = writeVariables;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Variable> getReadVariables() {
        if (this.readVariables == null) return new ArrayList<> ();
        return this.readVariables;
    }

    public ArrayList<Variable> getWriteVariables() {
        if (this.writeVariables == null) return new ArrayList<> ();
        return this.writeVariables;
    }
}
