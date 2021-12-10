package com.cloudlab.yamlprocessor;

import com.cloudlab.utils.Variables;

public class State {
    private String name;
    private Variables readVariable;
    private Variables writeVariable;

    public State(String name, Variables readVariable, Variables writeVariable) {
        this.name = name;
        this.readVariable = readVariable;
        this.writeVariable = writeVariable;
    }

    public String getName() {
        return name;
    }

    public Variables getReadVariable() {
        return readVariable;
    }

    public Variables getWriteVariable() {
        return writeVariable;
    }
}
