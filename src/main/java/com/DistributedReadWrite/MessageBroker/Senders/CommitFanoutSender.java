package com.DistributedReadWrite.MessageBroker.Senders;

import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;

public class CommitFanoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange commitFanout;

    @Autowired
    private StateMachine<SMStates, SMEvents> stateMachine;

    private String booleanToString(Boolean booleanValue) {
        if (booleanValue) return "commit";
        else return "abort";
    }

    public void send(Boolean response, String message) throws InterruptedException {
        // sends the data to CommitFanout
        String messageToSend = booleanToString(response) + "-" + message;
        rabbitTemplate.convertAndSend(commitFanout.getName(), "", messageToSend);
        System.out.println("[x] '"  + "' --> CommitFanout");
    }

}
