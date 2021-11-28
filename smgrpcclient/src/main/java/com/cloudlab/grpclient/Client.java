package com.cloudlab.grpclient;

import com.cloudlab.grpc.Tpc.ConnectionRequest;
import com.cloudlab.grpc.Tpc.ConnectionResponse;
import com.cloudlab.grpc.Tpc.AllocationRequest;
import com.cloudlab.grpc.Tpc.AllocationResponse;
import com.cloudlab.grpc.Tpc.NotificationMessage;
import com.cloudlab.grpc.Tpc.Empty;
import com.cloudlab.grpc.tpcGrpc;
import com.cloudlab.utils.Events;
import com.cloudlab.utils.States;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;

import java.util.Random;

public class Client {

    private ManagedChannel channel;
    private tpcGrpc.tpcBlockingStub stub;
    private final String clientID;
    private Integer timestamp;
    private StateMachine<States, Events> stateMachine;

    public Client(StateMachine<States, Events> stateMachine) {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.stub = tpcGrpc.newBlockingStub(channel);
        this.clientID = new Random().toString();
        this.timestamp = 0;
        this.stateMachine = stateMachine;
    }

    public void updateTimestamp(Integer timestamp) {
        if (this.timestamp < timestamp) this.timestamp = timestamp + 1;
        else this.timestamp = this.timestamp + 1;
    }

    public ConnectionRequest generateConnectionRequest() {
        return ConnectionRequest
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .build();
    }

    public AllocationRequest generateAllocationRequest(Events event) {
        return AllocationRequest
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .setEventName(event.name())
                .build();
    }

    public NotificationMessage generateNotificationMessage(Events event) {
        return NotificationMessage
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .setEventName(event.name())
                .build();
    }

    /**
     * Sends a connection request to the server in order to introduce itself
     * @return true if it is connected, else false
     */
    public boolean sendConnectionRequest() {
        ConnectionRequest connectionRequest = this.generateConnectionRequest();
        ConnectionResponse connectionResponse = this.stub.greetingService(connectionRequest);
        this.updateTimestamp(connectionResponse.getTimestamp());
        return connectionResponse.getResponse();
    }

    /**
     * Sends a request to server in order to allocate a process time
     * @param event event that triggers the state machine
     * @return true if it is possible to allocate process time, else false
     */
    public boolean sendAllocationRequest(Events event) {
        AllocationRequest allocationRequest = this.generateAllocationRequest(event);
        AllocationResponse allocationResponse = this.stub.allocationService(allocationRequest);
        this.updateTimestamp(allocationResponse.getTimestamp());
        return allocationResponse.getResponse();
    }

    /**
     * Sends a message to server telling that the process has finished
     * @param event event that triggering the state machine
     */
    public void sendNotificationMessage(Events event) {
        NotificationMessage notificationMessage = this.generateNotificationMessage(event);
        Empty empty = this.stub.notifyingService(notificationMessage);
        this.updateTimestamp(this.timestamp);
    }

    public void allocateAndExecute(Events event) {
        boolean isAllocated = false;

        while (!isAllocated) {
            // this.poll();
            isAllocated = this.sendAllocationRequest(event);
        }

        this.stateMachine.sendEvent(event);
        this.sendNotificationMessage(event);
    }

    /**
     * Runner for the client (preferably an infinite loop)
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {
        this.stateMachine.start();

        boolean isAccepted = this.sendConnectionRequest();

        if (isAccepted) {
            this.allocateAndExecute(Events.READ);
            this.allocateAndExecute(Events.READ);
            this.allocateAndExecute(Events.WRITE);
        }

        this.channel.shutdown();
    }

}
