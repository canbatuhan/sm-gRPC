package com.cloudlab.yamlprocessor;

import java.util.ArrayList;


public class State {
    private String name;
    private ArrayList<String> readVariables;
    private ArrayList<String> writeVariables;

    public State() {
        this.name = null;
        this.readVariables = null;
        this.writeVariables = null;
    }

    public State(String name, ArrayList<String> readVariables, ArrayList<String>  writeVariables) {
        this.name = name;
        this.readVariables = readVariables;
        this.writeVariables = writeVariables;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getReadVariables() {
        if (this.readVariables == null) return new ArrayList<> ();
        return this.readVariables;
    }

    public ArrayList<String> getWriteVariables() {
        if (this.writeVariables == null) return new ArrayList<> ();
        return this.writeVariables;
    }
}
