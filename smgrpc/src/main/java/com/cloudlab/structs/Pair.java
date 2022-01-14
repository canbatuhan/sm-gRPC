package com.cloudlab.structs;

import java.util.concurrent.atomic.AtomicInteger;

public class Pair {
    private AtomicInteger numOfRead; // Number of read operations on variable
    private AtomicInteger numOfWrite; // Number of write operations on variable

    /**
     * Builds a Pair object with given parameters
     * @param numOfRead number of read operations
     * @param numOfWrite number of write operations
     */
    public Pair(AtomicInteger numOfRead, AtomicInteger numOfWrite) {
        this.numOfRead = numOfRead;
        this.numOfWrite = numOfWrite;
    }

    public AtomicInteger getNumOfRead() {
        return numOfRead;
    }

    public AtomicInteger getNumOfWrite() {
        return numOfWrite;
    }

    /*
    public boolean isRead() {
        if (this.numOfRead < 0) this.numOfRead = 0;
        return this.numOfRead != 0;
    }

    public boolean isWrite() {
        if (this.numOfWrite < 0) this.numOfWrite = 0;
        return this.numOfWrite != 0;
    }
    */
}
