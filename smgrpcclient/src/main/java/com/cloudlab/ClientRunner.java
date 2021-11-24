package com.cloudlab;


import com.cloudlab.grpclient.Client;
import com.cloudlab.utils.Events;
import com.cloudlab.utils.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
public class ClientRunner implements CommandLineRunner {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    public static void main(String[] args) {
        SpringApplication.run(ClientRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Client machine = new Client(stateMachine);
        machine.run();
    }
}
