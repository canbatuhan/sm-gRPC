package com.cloudlab;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerRunner {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(9090)
                .addService(new TwoPhaseCommitServices())
                .build();

        server.start();
        server.awaitTermination();
    }

}
