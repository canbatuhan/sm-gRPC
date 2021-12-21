package com.cloudlab.grpclient;

import com.cloudlab.grpc.Tpc.ConnectionRequest;
import com.cloudlab.grpc.Tpc.ConnectionResponse;
import com.cloudlab.grpc.Tpc.AllocationRequest;
import com.cloudlab.grpc.Tpc.AllocationResponse;
import com.cloudlab.grpc.Tpc.NotificationMessage;
import com.cloudlab.grpc.Tpc.Empty;
import com.cloudlab.grpc.tpcGrpc;
import com.cloudlab.statemachine.StateMachineGenerator;
import com.cloudlab.yamlprocessor.Configurations;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.statemachine.StateMachine;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    private ManagedChannel channel;
    private tpcGrpc.tpcBlockingStub stub;
    private final String clientID;
    private Integer timestamp;
    private StateMachine<String, String> stateMachine;


    /**
     * Build Client object
     */
    public Client() throws Exception {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.stub = tpcGrpc.newBlockingStub(channel);
        this.clientID = ThreadLocalRandom.current().toString();
        this.timestamp = 0;
        this.stateMachine = new StateMachineGenerator("src/main/resources/statemachine.yaml").buildMachine();
    }


    /**
     * Updates the timestamp according to the incoming timestamp
     * @param timestamp incoming timestamp
     */
    public void updateTimestamp(Integer timestamp) {
        if (this.timestamp < timestamp) this.timestamp = timestamp + 1;
        else this.timestamp = this.timestamp + 1;
    }


    /**
     * Generates a connection request message to send to server
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
     * Generates an allocation request message to send to server
     * @param readVariables array storing the variables which will be read
     * @param writeVariables array storing the variables which will be written
     * @return AllocationRequest
     */
    public AllocationRequest generateAllocationRequest(ArrayList<String> readVariables, ArrayList<String> writeVariables) {
        AllocationRequest allocationRequest = AllocationRequest
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .buildPartial();

        /* Adding readFrom variables to the request */
        int index = 0;
        for (String readVariable : readVariables) {
            allocationRequest
                    .toBuilder()
                    .setReadFrom(0, readVariable)
                    .buildPartial();
            index += 1;
        }

        /* Adding writeTo variables to the request */
        index = 0;
        for (String writeVariable : writeVariables) {
            allocationRequest
                    .toBuilder()
                    .setWriteTo(index, writeVariable)
                    .buildPartial();
            index += 1;
        }

        return allocationRequest;
    }


    /**
     * Generates a notification message to send to server
     * @param readVariables array storing the variables which will be read
     * @param writeVariables array storing the variables which will be written
     * @return NotificationRequest
     */
    public NotificationMessage generateNotificationMessage(ArrayList<String> readVariables, ArrayList<String> writeVariables) {
        NotificationMessage notificationMessage = NotificationMessage
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .buildPartial();

        /* Adding readFrom variables to the message */
        int index = 0;
        for (String readVariable : readVariables) {
            notificationMessage
                    .toBuilder()
                    .setReadFrom(0, readVariable)
                    .buildPartial();
            index += 1;
        }

        /* Adding writeTo variables to the message */
        index = 0;
        for (String writeVariable : writeVariables) {
            notificationMessage
                    .toBuilder()
                    .setWriteTo(index, writeVariable)
                    .buildPartial();
            index += 1;
        }

        return notificationMessage;
    }


    /**
     * Sends a connection request to the server
     * @return response, answer of the server
     */
    public boolean sendConnectionRequest() {
        ConnectionRequest connectionRequest = this.generateConnectionRequest();
        ConnectionResponse connectionResponse = this.stub.greetingService(connectionRequest);
        this.updateTimestamp(connectionResponse.getTimestamp());
        return connectionResponse.getResponse();
    }


    /**
     * Sends an allocation request to the server
     * @param event event that will trigger the client
     * @return response, answer of the server about allocation request
     */
    public boolean sendAllocationRequest(String event) {
        Configurations configurations = new Configurations();
        String fromState = this.stateMachine.getState().getId();

        /* Get read and write variables */
        ArrayList<String> readVariables = configurations.getReadVariables(fromState, event);
        ArrayList<String> writeVariables = configurations.getWriteVariables(fromState, event);

        /* Generating and sending the request, receiving the response */
        AllocationRequest allocationRequest = this.generateAllocationRequest(readVariables, writeVariables);
        AllocationResponse allocationResponse = this.stub.allocationService(allocationRequest);

        this.updateTimestamp(allocationResponse.getTimestamp());
        return allocationResponse.getResponse();
    }


    /**
     * Sends a notification message to the server
     * @param event event that triggered the client
     */
    public void sendNotificationMessage(String event) {
        Configurations configurations = new Configurations();
        String fromState = this.stateMachine.getState().getId();

        /* Get read and write variables */
        ArrayList<String> readVariables = configurations.getReadVariables(fromState, event);
        ArrayList<String> writeVariables = configurations.getWriteVariables(fromState, event);

        NotificationMessage notificationMessage = this.generateNotificationMessage(readVariables, writeVariables);
        Empty empty = this.stub.notifyingService(notificationMessage);
        this.updateTimestamp(this.timestamp);
    }


    /**
     * Waits for a random time to send a request again
     * @param turn number of turn the client have polled
     */
    public void poll(int turn) throws InterruptedException {
        double base = 0.0512;
        double constant = ThreadLocalRandom
                .current()
                .nextDouble(0, Math.pow(2, turn));

        double waitTime = base * constant;
        Thread.sleep((long) waitTime);
    }


    /**
     * Tries to allocate from server and execute its incoming event
     */
    public void allocateAndExecute(String event) throws InterruptedException {
        boolean isAllocated = false;
        int turn = 0;

        while (!isAllocated) {
            this.poll(turn);
            turn = turn + 1;
            isAllocated = this.sendAllocationRequest(event);
        }

        this.stateMachine.sendEvent(event);
        this.sendNotificationMessage(event);
    }

    /**
     * Runner for the client (preferably an infinite loop)
     */
    public void run() throws InterruptedException {
        this.stateMachine.start();

        if (this.sendConnectionRequest()) {
            System.out.println("GO!");
            this.allocateAndExecute("EI1"); Thread.sleep(500);
            this.allocateAndExecute("E12"); Thread.sleep(500);
            this.allocateAndExecute("E21"); Thread.sleep(500);
        }

        this.channel.shutdown();
    }

}
