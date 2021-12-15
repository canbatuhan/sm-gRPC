package com.cloudlab;


import com.cloudlab.grpclient.Client;
import com.cloudlab.statemachine.StateMachineGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientRunner implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClientRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Client machine = new Client();
        machine.run();
    }
}
