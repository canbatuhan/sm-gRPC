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


    public AllocationRequest generateAllocationRequest() {
        return AllocationRequest
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .build();
    }


    public NotificationMessage generateNotificationMessage() {
        return NotificationMessage
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp)
                .build();
    }


    public boolean sendConnectionRequest() {
        ConnectionRequest connectionRequest = this.generateConnectionRequest();
        ConnectionResponse connectionResponse = this.stub.greetingService(connectionRequest);
        this.updateTimestamp(connectionResponse.getTimestamp());
        return connectionResponse.getResponse();
    }


    public boolean sendAllocationRequest() {
        AllocationRequest allocationRequest = this.generateAllocationRequest();
        AllocationResponse allocationResponse = this.stub.allocationService(allocationRequest);
        this.updateTimestamp(allocationResponse.getTimestamp());
        return allocationResponse.getResponse();
    }


    public void sendNotificationMessage() {
        NotificationMessage notificationMessage = this.generateNotificationMessage();
        Empty empty = this.stub.notifyingService(notificationMessage);
        this.updateTimestamp(this.timestamp);
    }


    public void poll(int turn) throws InterruptedException {
        double base = 0.0512;
        double constant = ThreadLocalRandom
                .current()
                .nextDouble(0, Math.pow(2, turn) - 1);

        double waitTime = base * constant;
        Thread.sleep((long) waitTime);
    }


    public void allocateAndExecute() throws InterruptedException {
        boolean isAllocated = false;
        int turn = 0;

        while (!isAllocated) {
            this.poll(turn);
            turn = turn + 1;
            isAllocated = this.sendAllocationRequest();
        }

        this.stateMachine.sendEvent("E");
        this.sendNotificationMessage();
    }

    /**
     * Runner for the client (preferably an infinite loop)
     */
    public void run() throws InterruptedException {
        this.stateMachine.start();

        boolean isAccepted = this.sendConnectionRequest();

        if (isAccepted) {
            this.allocateAndExecute();
            this.allocateAndExecute();
            this.allocateAndExecute();

        }

        this.channel.shutdown();
    }

}
