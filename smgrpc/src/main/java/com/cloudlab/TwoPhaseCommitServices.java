package com.cloudlab;

import com.cloudlab.grpc.Tpc.ConnectionRequest;
import com.cloudlab.grpc.Tpc.ConnectionResponse;
import com.cloudlab.grpc.Tpc.AllocationRequest;
import com.cloudlab.grpc.Tpc.AllocationResponse;
import com.cloudlab.grpc.Tpc.NotificationMessage;
import com.cloudlab.grpc.Tpc.Empty;
import com.cloudlab.grpc.tpcGrpc.tpcImplBase;
import com.cloudlab.utils.Events;
import com.cloudlab.utils.States;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;

/**
 * TwoPhaseCommitServices
 * will be rewritten in a more generic way
 */
public class TwoPhaseCommitServices extends tpcImplBase {
    private Integer timestamp;
    private HashMap<String, States> clientMap;
    private HashMap<States, Boolean> stateMap;

    public TwoPhaseCommitServices() {
        this.timestamp = 0;
        this.clientMap = new HashMap<>();
        this.stateMap = new HashMap<>();
        for (States state : States.values()) this.stateMap.put(state, false);
    }

    public void updateTimestamp(Integer timestamp) {
        if (this.timestamp < timestamp) this.timestamp = timestamp + 1;
        else this.timestamp = this.timestamp + 1;
    }

    public ConnectionResponse generateConnectionResponse(boolean response) {
        return ConnectionResponse
                .newBuilder()
                .setTimestamp(this.timestamp)
                .setResponse(response)
                .build();
    }

    public AllocationResponse generateAllocationResponse(String eventName, boolean response) {
        return AllocationResponse
                .newBuilder()
                .setTimestamp(this.timestamp)
                .setEventName(eventName)
                .setResponse(response)
                .build();
    }

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

        if (!clientMap.containsKey(clientID)) {
            System.out.println("Client " + clientID + " has connected.");
            this.clientMap.put(clientID, States.S_init);
            this.stateMap.replace(States.S_init, true);
            response = true;
        }

        else {
            System.out.println("Client " + clientID + " is already connected.");
            response = false;
        }

        /* generate and send the response */
        this.updateTimestamp(timestamp);
        ConnectionResponse connectionResponse = this.generateConnectionResponse(response);
        responseObserver.onNext(connectionResponse);
        responseObserver.onCompleted();
    }

    /**
     * allocationService, used when a client wants to allocate a process time from the server
     * @param request, allocation message includes clientID, timeStamp and eventName
     * @param responseObserver sender of the response
     */
    @Override
    public void allocationService(AllocationRequest request, StreamObserver<AllocationResponse> responseObserver) {
        String clientID = request.getClientID();
        Integer timestamp = request.getTimestamp();
        String eventName = request.getEventName();

        /* response logic (temporary) of allocation service */
        Events event = Events.valueOf(eventName);
        boolean response;

        if (event == Events.WRITE && this.stateMap.get(States.S_writing)) {
            System.out.println("There is already a " + event + " operation...");
            response = false;
        }

        else {
            System.out.println("Client can execute a " + event + " operation...");

            if (event == Events.WRITE) {
                this.stateMap.replace(States.S_writing, true);
                this.clientMap.replace(clientID, States.S_writing);
            }

            else if (event == Events.READ) {
                this.stateMap.replace(States.S_reading, true);
                this.clientMap.replace(clientID, States.S_reading);
            }

            response = true;
        }

        /* generate and send the response */
        this.updateTimestamp(timestamp);
        AllocationResponse allocationResponse = this.generateAllocationResponse(eventName, response);
        responseObserver.onNext(allocationResponse);
        responseObserver.onCompleted();
    }

    /**
     * notifyingService, used when a client done with its process and release the allocation.
     * @param request, notifying message includes clientID, timeStamp and eventName
     * @param responseObserver, sender of the response
     */
    @Override
    public void notifyingService(NotificationMessage request, StreamObserver<Empty> responseObserver) {
        String clientID = request.getClientID();
        Integer timestamp = request.getTimestamp();
        String eventName = request.getEventName();

        /* temporary operations in notifying service */
        Events event = Events.valueOf(eventName);
        if (event == Events.WRITE) {
            this.stateMap.replace(States.S_writing, false);
            this.clientMap.replace(clientID, States.S_waiting);
        }

        else if (event == Events.READ) {
            this.stateMap.replace(States.S_reading, false);
            this.clientMap.replace(clientID, States.S_waiting);
        }

        /* generate and send the response */
        this.updateTimestamp(timestamp);
        Empty empty = this.generateEmpty();
        responseObserver.onNext(empty);
        responseObserver.onCompleted();
    }
}
