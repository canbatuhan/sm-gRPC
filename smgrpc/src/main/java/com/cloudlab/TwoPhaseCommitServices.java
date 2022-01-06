package com.cloudlab;

import com.cloudlab.grpc.Tpc.ConnectionRequest;
import com.cloudlab.grpc.Tpc.ConnectionResponse;
import com.cloudlab.grpc.Tpc.AllocationRequest;
import com.cloudlab.grpc.Tpc.AllocationResponse;
import com.cloudlab.grpc.Tpc.NotificationMessage;
import com.cloudlab.grpc.Tpc.Empty;
import com.cloudlab.grpc.tpcGrpc.tpcImplBase;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.HashSet;


public class TwoPhaseCommitServices extends tpcImplBase {
    private Integer timestamp; // Timestamp of the server
    private HashMap<String, Pair> variableTable; // Map to check the variable status (isReading, isWriting)
    private HashSet<String> clientMap; // Set to see which clients are connected to the server

    /**
     * Class for storing the status of a variable (is on-read or is on-write)
     */
    private static class Pair {
        public boolean readStatus; // Flag for read operations on variable
        public boolean writeStatus; // Flag for write operations on variable

        /**
         * Builds a Pair object with given parameters
         * @param readStatus flag for read operations
         * @param writeStatus flag for write operations
         */
        public Pair(boolean readStatus, boolean writeStatus) {
            this.readStatus = readStatus;
            this.writeStatus = writeStatus;
        }
    }

    /**
     * Builds TwoPhaseCommitServices object
     */
    public TwoPhaseCommitServices() {
        this.timestamp = 0;
        this.variableTable = new HashMap<>();
        this.clientMap = new HashSet<>();
    }

    /**
     * Updates its timestamp according to the incoming message
     * @param timestamp incoming timestamp
     */
    public void updateTimestamp(Integer timestamp) {
        if (this.timestamp < timestamp) this.timestamp = timestamp + 1;
        else this.timestamp = this.timestamp + 1;
    }

    /**
     * Applies the logic for allocation, basically checks if any of the variables
     * is in-use by another process in the server
     * @param request incoming request
     * @return response, answer of the server on allocation request
     */
    public boolean allocationResponseLogic(AllocationRequest request) {
        boolean response = true; // initially true
        String readVariable;
        String writeVariable;

        /* Checking readFrom Variables */
        int numOfReadVariables = request.getReadFromCount();
        for (int index=0; index<numOfReadVariables; index++) {
            readVariable = request.getReadFrom(index);

            // if the server face with this variable for the first time, add it to table
            // also set it has no read and write operations on this variable
            if (!this.variableTable.containsKey(readVariable)) {
                this.variableTable.put(readVariable, new Pair(false, false));
            }

            // if the variable is being written, then server can not allocate read operation
            else if (this.variableTable.get(readVariable).writeStatus){
                response = false;
                break;
            }
        }

        /* Checking writeTo Variables */
        int numOfWriteVariables = request.getWriteToCount();
        for (int index=0; index<numOfWriteVariables; index++) {
            writeVariable = request.getWriteTo(index);
            Pair currentPair = this.variableTable.get(writeVariable);

            // if the server face with this variable for the first time, add it to map
            // set it has no read and write operations on this variable
            if (!this.variableTable.containsKey(writeVariable)) {
                this.variableTable.put(writeVariable, new Pair(false, false));
            }

            // if the variable is being read or written, then server can not allocate write operation
            else if (currentPair.readStatus || currentPair.writeStatus){
                response = false;
                break;
            }
        }

        /* Setting the Variable Flags */
        if (response) {

            // Set read flag of variables if allocated
            for (int index=0; index<numOfReadVariables; index++) {
                readVariable = request.getReadFrom(index);
                Pair currentPair = this.variableTable.get(readVariable);
                this.variableTable.replace(readVariable, new Pair(true, currentPair.writeStatus));
            }

            // Set write flag of variables if allocated
            for (int index=0; index<numOfWriteVariables; index++) {
                writeVariable = request.getWriteTo(index);
                Pair currentPair = this.variableTable.get(writeVariable);
                this.variableTable.replace(writeVariable, new Pair(currentPair.readStatus, true));
            }
        }

        return response;
    }

    /**
     * Generates connection response message for client
     * @param response boolean value, server's on greeting the client
     * @return ConnectionResponse message
     */
    public ConnectionResponse generateConnectionResponse(boolean response) {
        return ConnectionResponse
                .newBuilder()
                .setTimestamp(this.timestamp)
                .setResponse(response)
                .build();
    }

    /**
     * Generates allocation response message to send to client
     * @param response boolean value, server's answer on allocation request
     * @return AllocationResponse message
     */
    public AllocationResponse generateAllocationResponse(boolean response) {
        return AllocationResponse
                .newBuilder()
                .setTimestamp(this.timestamp)
                .setResponse(response)
                .build();
    }

    /**
     * Generates empty message (response of notifyingService)
     * @return Empty message
     */
    public Empty generateEmpty() {
        return Empty
                .newBuilder()
                .build();
    }

    /**
     * greetingService, used when a client first connects to a server
     * @param request, connection message includes clientID and timeStamp
     * @param responseObserver, sender of the response
     */
    @Override
    public void greetingService(ConnectionRequest request, StreamObserver<ConnectionResponse> responseObserver) {
        String clientID = request.getClientID();
        Integer timestamp = request.getTimestamp();

        /* Response Logic Of Greeting Service */
        boolean response;

        // if the client is already connected, ignore the request
        if (this.clientMap.contains(clientID)) {
            response = false;
        }

        // otherwise, say hello
        else {
            this.clientMap.add(clientID);
            response = true;
        }

        /* Generating And Sending The Response */
        this.updateTimestamp(timestamp);
        ConnectionResponse connectionResponse = this.generateConnectionResponse(response);
        responseObserver.onNext(connectionResponse);
        responseObserver.onCompleted();
    }

    /**
     * allocationService, used when a client wants to allocate a process time from the server
     * @param request, allocation message includes clientID, timeStamp, readFrom and writeTo
     * @param responseObserver sender of the response
     */
    @Override
    public void allocationService(AllocationRequest request, StreamObserver<AllocationResponse> responseObserver) {
        String clientID = request.getClientID();
        Integer timestamp = request.getTimestamp();

        /* Response Logic Of Allocation Service */
        boolean response = allocationResponseLogic(request);

        /* Generating And Sending The Response */
        this.updateTimestamp(timestamp);
        AllocationResponse allocationResponse = this.generateAllocationResponse(response);
        responseObserver.onNext(allocationResponse);
        responseObserver.onCompleted();
    }

    /**
     * notifyingService, used when a client done with its process and release the allocation.
     * @param request, notifying message includes clientID, timeStamp, readFrom and writeTo
     * @param responseObserver, sender of the response
     */
    @Override
    public void notifyingService(NotificationMessage request, StreamObserver<Empty> responseObserver) {
        String clientID = request.getClientID();
        Integer timestamp = request.getTimestamp();
        Pair currentPair;

        /* Clearing readFrom flag */
        String readVariable;
        int numOfReadVariable = request.getReadFromCount();

        for (int index=0; index<numOfReadVariable; index++) {
            readVariable = request.getReadFrom(index);
            currentPair = this.variableTable.get(readVariable);
            this.variableTable.replace(readVariable, new Pair(false, currentPair.writeStatus));
        }

        /* Clearing writeTo flag */
        String writeVariable;
        int numOfWriteVariable = request.getWriteToCount();

        for (int index=0; index<numOfWriteVariable; index++) {
            writeVariable = request.getWriteTo(index);
            currentPair = this.variableTable.get(writeVariable);
            this.variableTable.replace(writeVariable, new Pair(currentPair.readStatus, false));
        }

        /* generating and sending the response */
        this.updateTimestamp(timestamp);
        Empty empty = this.generateEmpty();
        responseObserver.onNext(empty);
        responseObserver.onCompleted();
    }
}
