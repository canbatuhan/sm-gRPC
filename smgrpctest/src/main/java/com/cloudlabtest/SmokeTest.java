package com.cloudlabtest;

import com.cloudlab.TwoPhaseCommitServices;
import com.cloudlab.grpclient.Client;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class SmokeTest {

    /**
     * Thread for running the Client
     */
    public static class ClientThread extends Thread {
        private final String inputPath = "src\\main\\resources\\_smokeTest.txt";
        private final String configPath = "src\\main\\resources\\statemachine.yaml";

        @Override
        public void run() {
            try {
                Client client = new Client(this.configPath);
                client.readInput(this.inputPath);
                client.run();
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thread for running the Server
     */
    public static class ServerThread extends Thread {
        @Override
        public void run() {
            Server server = ServerBuilder
                    .forPort(9090)
                    .addService(new TwoPhaseCommitServices())
                    .build();

            try {
                server.start();
                server.awaitTermination();

            }

            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runner of the SmokeTest
     * @throws InterruptedException
     */
    public void runTest() throws InterruptedException {
        Thread client = new ClientThread();
        Thread server = new ServerThread();

        server.start();
        Thread.sleep(100);
        client.start();
    }
}
