package com.cloudlab.statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.ArrayList;

public class ReadWriteAction implements Action<String, String> {
    private ArrayList<String> readVariables; // Variables that are used for reading
    private ArrayList<String> writeVariables; // Variables that are used for writing

    /**
     * Builds a ReadWriteAction class, which runs read and write threads
     * @param readVariables list of variables which the machine read data from
     * @param writeVariables list of variables which the machine write data into
     */
    public ReadWriteAction(ArrayList<String> readVariables, ArrayList<String> writeVariables) {
        super();
        this.readVariables = readVariables;
        this.writeVariables = writeVariables;
    }

    /**
     * Executes the read and write operations concurrently
     * @param stateContext current context of the mechanism
     */
    @Override
    public void execute(StateContext<String, String> stateContext) {
        for (String readVariable : this.readVariables) {
            System.out.println("Reading from " + readVariable + "...");
        }

        for (String writeVariable : this.writeVariables) {
            System.out.println("Writing into " + writeVariable + "...");
        }
    }
}
