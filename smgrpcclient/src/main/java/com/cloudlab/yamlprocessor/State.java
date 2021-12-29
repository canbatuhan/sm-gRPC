package com.cloudlab.yamlprocessor;

import java.util.ArrayList;


public class State {
    private String name; // State ID
    private ArrayList<String> readVariables; // Variables that the client executes read operations
    private ArrayList<String> writeVariables; // Variables that the client executes write operations

    /**
     * Builds a State object, their attributes are assigned as null, by default
     */
    public State() {
        this.name = null;
        this.readVariables = null;
        this.writeVariables = null;
    }

    /**
     * Builds a State object by using parameters
     * @param name unique ID of the state
     * @param readVariables variables in which read operations will be held
     * @param writeVariables variables in which write operations will be held
     */
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
