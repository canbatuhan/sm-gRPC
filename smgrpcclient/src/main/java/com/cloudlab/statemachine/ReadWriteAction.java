package com.cloudlab.statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.ArrayList;

public class ReadWriteAction implements Action<String, String> {
    private ArrayList<String> readVariables;
    private ArrayList<String> writeVariables;

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
        Thread readThread = new ReadThread(this.readVariables);
        Thread writeThread = new WriteThread(this.writeVariables);

        readThread.start();
        writeThread.start();
    }

    /**
     * Class for running a read thread
     */
    public static class ReadThread extends Thread {
        private ArrayList<String> readVariables;

        /**
         * Builds a ReadThread object to read from variables
         * @param readVariables list of variables to read data from
         */
        public ReadThread(ArrayList<String> readVariables) {
            super();
            this.readVariables = readVariables;
        }

        /**
         * Runs the thread
         */
        @Override
        public void run() {
            for (String readVariable : this.readVariables) {
                System.out.println("Pseudo-reading on " + readVariable + "...");
            }
        }
    }

    /**
     * Class for running a write thread
     */
    public static class WriteThread extends Thread {
        private ArrayList<String> writeVariables;

        /**
         * Builds a WriteThread object to write into variables
         * @param writeVariables list of variables to write data into
         */
        public WriteThread(ArrayList<String> writeVariables) {
            super();
            this.writeVariables = writeVariables;
        }

        /**
         * Runs the thread
         */
        @Override
        public void run() {
            for (String writeVariable : this.writeVariables) {
                System.out.println("Pseudo-writing on " + writeVariable + "...");
            }
        }
    }
}
