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
import com.cloudlab.yamlprocessor.Transition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.statemachine.StateMachine;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    private ManagedChannel channel; // Channel in which the client communicates with the server
    private tpcGrpc.tpcBlockingStub stub; // Stub storing the gRPC service methods
    private final String clientID; // Unique ID for each client
    private Integer timestamp; // Timestamp of the client
    private Configurations configurations; // Configurations of the client's state machine
    private StateMachine<String, String> stateMachine; // State machine in which client executes its tasks
    private String inputPath; // File path in where the inputs will be read
    private ConcurrentLinkedQueue<String> inputQueue; // Queue storing the inputs coming to the client
    private String outputPath; // File path in where the results will be read (log)

    /**
     * Reads .yaml file to get the configurations
     * @return Configuration object, filled with the yaml input
     * @throws IOException readValue can give an error
     */
    private Configurations readYamlInput(String configPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        return mapper.readValue(new File(configPath), Configurations.class);
    }

    /**
     * Builds a Client with default configs and given input file path
     * @param inputPath file that includes the event inputs
     */
    public Client(String inputPath) throws Exception {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.stub = tpcGrpc.newBlockingStub(channel);
        this.clientID = ThreadLocalRandom.current().toString();
        this.timestamp = 0;
        this.configurations = this.readYamlInput("src\\resources\\statemachine.yaml");
        this.stateMachine = new StateMachineGenerator(this.configurations).buildMachine();
        this.inputPath = inputPath;
        this.inputQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Builds a Client with given state machine configs and input file path
     * @param configPath file that includes state machine configuration details
     * @param inputPath file that includes the event inputs
     */
    public Client(String configPath, String inputPath) throws Exception {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.stub = tpcGrpc.newBlockingStub(channel);
        this.clientID = ThreadLocalRandom.current().toString();
        this.timestamp = 0;
        this.configurations = this.readYamlInput(configPath);
        this.stateMachine = new StateMachineGenerator(this.configurations).buildMachine();
        this.inputPath = inputPath;
        this.inputQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Builds a Client with given state machine configs and input file path
     * @param configPath file that includes state machine configuration details
     * @param inputPath file that includes the event inputs
     * @param outputPath file that the results will be written into
     */
    public Client(String configPath, String inputPath, String outputPath) throws Exception {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.stub = tpcGrpc.newBlockingStub(channel);
        this.clientID = ThreadLocalRandom.current().toString();
        this.timestamp = 0;
        this.configurations = this.readYamlInput(configPath);
        this.stateMachine = new StateMachineGenerator(this.configurations).buildMachine();
        this.inputPath = inputPath;
        this.inputQueue = new ConcurrentLinkedQueue<>();
        this.outputPath = outputPath;
    }


    /**
     * Reads input from a file (for now)
     * @throws FileNotFoundException when there is a file error (can not opened)
     */
    private void readEventInput() throws FileNotFoundException {
        FileReader fileReader = new FileReader(this.inputPath);
        Scanner scanner = new Scanner(fileReader);

        while (scanner.hasNextLine()) {
            String eventData = scanner.nextLine();
            this.inputQueue.add(eventData);
        }
    }

    /**
     * Updates the timestamp according to the incoming timestamp
     * @param timestamp incoming timestamp
     */
    private void updateTimestamp(Integer timestamp) {
        if (this.timestamp < timestamp) this.timestamp = timestamp + 1;
        else this.timestamp = this.timestamp + 1;
    }

    /**
     * Generates a connection request message to send to server
     * @return ConnectionRequest
     */
    private ConnectionRequest generateConnectionRequest() {
        return ConnectionRequest.newBuilder()
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
    private AllocationRequest generateAllocationRequest(ArrayList<String> readVariables, ArrayList<String> writeVariables) {
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
                    .addReadFrom(readVariable)
                    .buildPartial();
            index += 1;
        }

        /* Adding writeTo variables to the request */
        index = 0;
        for (String writeVariable : writeVariables) {
            allocationRequest
                    .toBuilder()
                    .addWriteTo(writeVariable)
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
    private NotificationMessage generateNotificationMessage(ArrayList<String> readVariables, ArrayList<String> writeVariables) {
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
                    .addReadFrom(readVariable)
                    .buildPartial();
            index += 1;
        }

        /* Adding writeTo variables to the message */
        index = 0;
        for (String writeVariable : writeVariables) {
            notificationMessage
                    .toBuilder()
                    .addWriteTo(writeVariable)
                    .buildPartial();
            index += 1;
        }

        return notificationMessage;
    }

    /**
     * Sends a connection request to the server
     * @return response, answer of the server
     */
    private boolean sendConnectionRequest() {
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
    private boolean sendAllocationRequest(String event) {
        String fromState = this.stateMachine.getState().getId();

        /* Get read and write variables */
        ArrayList<String> readVariables = this.configurations.getReadVariables(fromState, event);
        ArrayList<String> writeVariables = this.configurations.getWriteVariables(fromState, event);

        /* Generating and sending the request, receiving the response */
        AllocationRequest allocationRequest = this.generateAllocationRequest(readVariables, writeVariables);
        AllocationResponse allocationResponse = this.stub.allocationService(allocationRequest);

        this.updateTimestamp(allocationResponse.getTimestamp());
        return allocationResponse.getResponse();
    }

    /**
     * Sends a notification message to the server
     * @param currentState current state of the statemachine
     */
    private void sendNotificationMessage(String currentState) {
        /* Get read and write variables */
        ArrayList<String> readVariables = this.configurations.getReadVariables(currentState);
        ArrayList<String> writeVariables = this.configurations.getWriteVariables(currentState);

        NotificationMessage notificationMessage = this.generateNotificationMessage(readVariables, writeVariables);
        Empty empty = this.stub.notifyingService(notificationMessage);
        this.updateTimestamp(this.timestamp);
    }

    /**
     * Waits for a random time to send a request again
     * @param turn number of turn the client have polled
     */
    private void backoffPoll(int turn) throws InterruptedException {
        double base = 0.0512;
        double constant = ThreadLocalRandom
                .current()
                .nextDouble(0, Math.pow(2, turn));

        double waitTime = base * constant;
        Thread.sleep((long) waitTime);
    }

    /**
     * Builds string for logging of reading and writing actions
     */
    private void recordEvent() throws IOException {
        FileWriter fileWriter = new FileWriter(this.outputPath, true);
        String currentState = this.stateMachine.getState().getId();

        StringBuilder log;
        StringBuilder clientEventLog = new StringBuilder();

        clientEventLog
                .append("[x] ")
                .append(this.timestamp)
                .append("\t")
                .append(this.clientID)
                .append("\t")
                .append(this.stateMachine.getState().getId());

        clientEventLog.append("\tRead Variables: ");
        for (String readVariable : this.configurations.getReadVariables(currentState)) {
            clientEventLog.append(readVariable).append(" ");
        }

        clientEventLog.append("\tWrite Variables: ");
        for (String writeVariable : this.configurations.getWriteVariables(currentState)) {
            clientEventLog.append(writeVariable).append(" ");
        }

        clientEventLog.append("\n");
        fileWriter.write(String.valueOf(clientEventLog));
        fileWriter.close();
    }

    /**
     * Tries to allocate from server and execute its incoming event
     */
    private void allocateAndExecute(String event) throws InterruptedException, IOException {
        boolean isAllocated = false;
        int turn = 0;

        while (!isAllocated) {
            this.backoffPoll(turn);
            turn = turn + 1;
            isAllocated = this.sendAllocationRequest(event);
        }

        this.recordEvent();
        this.stateMachine.sendEvent(event);
        this.sendNotificationMessage(this.stateMachine.getState().getId());
    }

    /**
     * Runner for the client (preferably an infinite loop)
     */
    public void run() throws InterruptedException, IOException {
        this.readEventInput();
        this.stateMachine.start();

        if (this.sendConnectionRequest()) {
            System.out.println(this.clientID + " GOGOGO!");
            while (!inputQueue.isEmpty()) {
                String event = inputQueue.poll();
                this.allocateAndExecute(event);
                Thread.sleep(500);
            }

        }

        this.channel.shutdown();
    }

}
