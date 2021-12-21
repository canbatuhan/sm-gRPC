package com.cloudlab.yamlprocessor;

import java.util.ArrayList;
import java.util.Arrays;

public class State {
    private String name;
    private String readVariables;
    private String writeVariables;

    public State() {
        this.name = null;
        this.readVariables = null;
        this.writeVariables = null;
    }

    public State(String name, String readVariables, String writeVariables) {
        this.name = name;
        if (readVariables == null) this.readVariables = "";
        else this.readVariables = readVariables;
        if (writeVariables == null) this.writeVariables = "";
        this.writeVariables = writeVariables;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getReadVariables() { return (ArrayList<String>) Arrays.asList(readVariables.split(" ")); }

    public ArrayList<String> getWriteVariables() { return (ArrayList<String>) Arrays.asList(writeVariables.split(" ")); }
}
