package com.cloudlab;

import com.cloudlab.grpc.Tpc.ConnectionRequest;
import com.cloudlab.grpc.Tpc.ConnectionResponse;
import com.cloudlab.grpc.Tpc.AllocationRequest;
import com.cloudlab.grpc.Tpc.AllocationResponse;
import com.cloudlab.grpc.Tpc.NotificationMessage;
import com.cloudlab.grpc.Tpc.Empty;
import com.cloudlab.grpc.tpcGrpc.tpcImplBase;
import com.cloudlab.utils.States;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * TwoPhaseCommitServices
 * will be rewritten in a more generic way
 */
public class TwoPhaseCommitServices extends tpcImplBase {
    private Integer timestamp;
    private HashMap<String, Boolean> variableMap;
    private HashSet<String> clientMap;


    /**
     * Builds TwoPhaseCommitServices object
     */
    public TwoPhaseCommitServices() {
        this.timestamp = 0;
        this.variableMap = new HashMap<>();
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

        /* response logic of greeting service */
        boolean response;

        // if the client is already connected, ignore message
        if (this.clientMap.contains(clientID)) {
            System.out.println("Client " + clientID + " is already connected.");
            response = false;
        }

        // otherwise, say hello
        else {
            this.clientMap.add(clientID);
            System.out.println("Hello " + clientID + ".");
            response = true;
        }

        /* generating and sending the response */
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

        /* response logic of allocation service */
        boolean response = true; // initially true
        String writeVariable;

        int numOfWriteVariables = request.getWriteToCount();
        for (int index=0; index<numOfWriteVariables; index++) {
            writeVariable = request.getWriteTo(index);

            // if the server face with this variable for the firs time, add it to map
            if (!this.variableMap.containsKey(writeVariable)) {
                this.variableMap.put(writeVariable, false);
            }

            // if the variable is "in-use", then server can not allocate for this event
            else if (this.variableMap.get(writeVariable)){
                response = false;
                break;
            }
        }

        // if the server is going to allocate for the event, set the variables as "in-use"
        if (response) {
            for (int index=0; index<numOfWriteVariables; index++) {
                writeVariable = request.getWriteTo(index);
                this.variableMap.replace(writeVariable, true);
            }
        }

        /* generating and sending the response */
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

        /* operations in notifying service */
        String writeVariable;

        int numOfWriteVariable = request.getWriteToCount();
        for (int index=0; index<numOfWriteVariable; index++) {
            writeVariable = request.getWriteTo(numOfWriteVariable);
            this.variableMap.replace(writeVariable, false);
        }

        /* generating and sending the response */
        this.updateTimestamp(timestamp);
        Empty empty = this.generateEmpty();
        responseObserver.onNext(empty);
        responseObserver.onCompleted();
    }
}
