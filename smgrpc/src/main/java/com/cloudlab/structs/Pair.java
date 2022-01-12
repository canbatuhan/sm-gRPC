package com.cloudlab.structs;

public class Pair {
    private int numOfRead; // Number of read operations on variable
    private int numOfWrite; // Number of write operations on variable

    /**
     * Builds a Pair object with given parameters
     * @param numOfRead number of read operations
     * @param numOfWrite number of write operations
     */
    public Pair(int numOfRead, int numOfWrite) {
        this.numOfRead = numOfRead;
        this.numOfWrite = numOfWrite;
    }

    public int getNumOfRead() {
        return numOfRead;
    }

    public int getNumOfWrite() {
        return numOfWrite;
    }

    public boolean isRead() {
        if (this.numOfRead < 0) this.numOfRead = 0;
        return this.numOfRead != 0;
    }

    public boolean isWrite() {
        if (this.numOfWrite < 0) this.numOfWrite = 0;
        return this.numOfWrite != 0;
    }
}