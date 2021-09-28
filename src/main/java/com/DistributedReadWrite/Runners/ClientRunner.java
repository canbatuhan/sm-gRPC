package com.DistributedReadWrite.Runners;

import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.statemachine.StateMachine;

public class ClientRunner implements CommandLineRunner {

    @Autowired
    private StateMachine<SMStates, SMEvents> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        stateMachine.getExtendedState().getVariables().put("Timestamp", 0);
        stateMachine.getExtendedState().getVariables().put("InitialMessage", true);

        stateMachine.start();
    }

}
