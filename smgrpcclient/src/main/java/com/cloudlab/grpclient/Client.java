package com.cloudlab.grpclient;

import com.cloudlab.grpc.Tpc.ConnectionRequest;
import com.cloudlab.grpc.Tpc.ConnectionResponse;
import com.cloudlab.grpc.Tpc.AllocationRequest;
import com.cloudlab.grpc.Tpc.AllocationResponse;
import com.cloudlab.grpc.Tpc.NotificationMessage;
import com.cloudlab.grpc.Tpc.Empty;
import com.cloudlab.grpc.tpcGrpc;
import com.cloudlab.statemachine.StateMachineGenerator;
import com.cloudlab.utils.Events;
import com.cloudlab.utils.States;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.statemachine.StateMachine;

import java.util.concurrent.ThreadLocalRandom;

public class Client {

    private ManagedChannel channel;
    private tpcGrpc.tpcBlockingStub stub;
    private final String clientID;
    private Integer timestamp;
    private StateMachine<String, String> stateMachine;

    public Client() throws Exception {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.stub = tpcGrpc.newBlockingStub(channel);
        this.clientID = ThreadLocalRandom.current().toString();
        this.timestamp = 0;
        this.stateMachine = new StateMachineGenerator("src/main/resources/statemachine.yaml").buildMachine();
    }

    /**
     * Updates the timestamp according to the incoming response
     * @param timestamp timestamp of the server
     */
    public void updateTimestamp(Integer timestamp) {
        if (this.timestamp < timestamp) this.timestamp = timestamp + 1;
        else this.timestamp = this.timestamp + 1;
    }

    /**
     * Generates a connection request by using clientID and timestamp
     * @return ConnectionRequest
     */
    public ConnectionRequest generateConnectionRequest() {
        return ConnectionRequest
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .build();
    }

    /**
     * Generates an allocation request to send to server
     * @param event event that the client wants to trigger
     * @return AllocationRequest
     */
    public AllocationRequest generateAllocationRequest(Events event) {
        return AllocationRequest
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .setEventName(event.name())
                .build();
    }

    /**
     * Generates a notification message to say the job is done
     * @param event event that has been triggered by the client
     * @return NotificationMessage
     */
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

    /**
     * Waits for a random time to send a allocation request again
     * @param turn number of collisions (non-accepted allocation requests)
     * @throws InterruptedException if thread fails to sleep
     */
    public void poll(int turn) throws InterruptedException {
        double base = 0.0512;
        double constant = ThreadLocalRandom
                .current()
                .nextDouble(0, Math.pow(2, turn) - 1);

        double waitTime = base * constant;
        Thread.sleep((long) waitTime);
    }

    /**
     * Tries to allocate process time from server, then executes it
     * @param event event that will trigger the state machine
     */
    public void allocateAndExecute(Events event) throws InterruptedException {
        boolean isAllocated = false;
        int turn = 0;

        while (!isAllocated) {
            this.poll(turn);
            turn = turn + 1;
            isAllocated = this.sendAllocationRequest(event);
        }

        this.stateMachine.sendEvent(event.name());
        this.sendNotificationMessage(event);
    }

    /**
     * Runner for the client (preferably an infinite loop)
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
