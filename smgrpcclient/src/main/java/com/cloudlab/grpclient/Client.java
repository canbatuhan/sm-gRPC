package com.cloudlab.grpclient;

import com.cloudlab.grpc.Tpc;
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
     * Generates a ID for the client
     * @return unique ID
     */
    private String generateID() {
        String base = "Client #";
        int lowerLimit = 1773;
        int upperLimit = 9999;
        return base + ThreadLocalRandom.current().nextInt(lowerLimit, upperLimit+1);
    }

    /**
     * Builds a Client with default configs and given input file path
     * @param inputPath file that includes the event inputs
     */
    public Client(String inputPath) throws Exception {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.stub = tpcGrpc.newBlockingStub(channel);
        this.clientID = this.generateID();
        this.timestamp = 0;
        this.configurations = this.readYamlInput("src\\main\\resources\\statemachine.yaml");
        this.stateMachine = new StateMachineGenerator(this.configurations).buildMachine();
        this.inputPath = inputPath;
        this.inputQueue = new ConcurrentLinkedQueue<>();
        this.outputPath = "src\\main\\resources\\output.txt";
    }

    /**
     * Builds a Client with given state machine configs and input file path
     * @param configPath file that includes state machine configuration details
     * @param inputPath file that includes the event inputs
     */
    public Client(String configPath, String inputPath) throws Exception {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        this.stub = tpcGrpc.newBlockingStub(channel);
        this.clientID = this.generateID();
        this.timestamp = 0;
        this.configurations = this.readYamlInput(configPath);
        this.stateMachine = new StateMachineGenerator(this.configurations).buildMachine();
        this.inputPath = inputPath;
        this.inputQueue = new ConcurrentLinkedQueue<>();
        this.outputPath = "src\\main\\resources\\output.txt";
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
        this.clientID = this.generateID();
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
        AllocationRequest.Builder allocationRequest = AllocationRequest
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp);

        /* Adding readFrom Variables To Request */
        int index = 0;
        for (String readVariable : readVariables) {
            allocationRequest
                    .addReadFrom(readVariable);
            index += 1;
        }

        /* Adding writeTo Variables To Request */
        index = 0;
        for (String writeVariable : writeVariables) {
            allocationRequest
                    .addWriteTo(writeVariable);
            index += 1;
        }

        return allocationRequest.build();
    }

    /**
     * Generates a notification message to send to server
     * @param readVariables array storing the variables which will be read
     * @param writeVariables array storing the variables which will be written
     * @return NotificationRequest
     */
    private NotificationMessage generateNotificationMessage(ArrayList<String> readVariables, ArrayList<String> writeVariables) {
        NotificationMessage.Builder notificationMessage = NotificationMessage
                .newBuilder()
                .setClientID(this.clientID)
                .setTimestamp(this.timestamp);

        /* Adding readFrom Variables To Message */
        int index = 0;
        for (String readVariable : readVariables) {
            notificationMessage
                    .addReadFrom(readVariable);
            index += 1;
        }

        /* Adding writeTo Variables To Message */
        index = 0;
        for (String writeVariable : writeVariables) {
            notificationMessage
                    .addWriteTo(writeVariable);
            index += 1;
        }

        return notificationMessage.build();
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

        /* Getting Read And Write Variables */
        ArrayList<String> readVariables = this.configurations.getReadVariables(fromState, event);
        ArrayList<String> writeVariables = this.configurations.getWriteVariables(fromState, event);

        /* Generating And Sending The Request, Receiving Response */
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
        /* Getting Read And Write Variables */
        ArrayList<String> readVariables = this.configurations.getReadVariables(currentState);
        ArrayList<String> writeVariables = this.configurations.getWriteVariables(currentState);

        /* Generating And Sending The Message */
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
    private void recordEvent(String event, String successOrAttempt) throws IOException {
        FileWriter fileWriter = new FileWriter(this.outputPath, true);
        String fromState = this.stateMachine.getState().getId();
        String toState = this.configurations.getToState(fromState, event);

        /* Building Log String */
        StringBuilder clientEventLog = new StringBuilder();
        clientEventLog
                .append("[x] ")
                .append(this.timestamp).append(" | ")
                .append(this.clientID).append(" | ")
                .append(successOrAttempt).append(" | ")
                .append(toState);

        /* Adding Read Variables */
        clientEventLog.append(" | Read Variables: ");
        for (String readVariable : this.configurations.getReadVariables(fromState, event)) {
            clientEventLog.append(readVariable).append(" ");
        }

        /* Adding Write Variables */
        clientEventLog.append("| Write Variables: ");
        for (String writeVariable : this.configurations.getWriteVariables(fromState, event)) {
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
        int turn = 0;
        boolean firstAttempt = true; // First attempt (if any), will be logged

        /* Polling */
        while (!this.sendAllocationRequest(event)) {
            if (firstAttempt) {
                this.recordEvent(event, "Attempt"); // Record the first attempt
                firstAttempt = false;
            }
            this.backoffPoll(turn);
            turn = turn + 1;
        }

        /* Recording The Successful Allocation Request, Running The State Machine */
        this.recordEvent(event, "Success");
        this.stateMachine.sendEvent(event);

        /* Sending A Notification Message */
        this.sendNotificationMessage(this.stateMachine.getState().getId());
    }

    /**
     * Runner for the client (preferably an infinite loop)
     */
    public void run() throws InterruptedException, IOException {
        this.readEventInput();
        this.stateMachine.start();

        if (this.sendConnectionRequest()) {
            while (!inputQueue.isEmpty()) {
                String event = inputQueue.poll();
                this.allocateAndExecute(event);
            }
        }

        this.channel.shutdown();
    }

}
