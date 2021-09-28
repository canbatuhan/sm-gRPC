package com.DistributedReadWrite.MessageBroker.Receivers;

import com.DistributedReadWrite.MessageBroker.Senders.CommitFanoutSender;
import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;

@RabbitListener(queues = "#{responseFanoutQueue.name}")
public class ResponseFanoutReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StateMachine<SMStates, SMEvents> stateMachine;

    @Autowired
    private CommitFanoutSender commitFanoutSender;

    private Boolean stringToBoolean(String string) {
        return string.equals("commit");
    }

    /*
        data format: %RESPONSE%-%TIMESTAMP%-%EVENT%
    */
    @RabbitHandler
    public void receive(String messageReceived) throws InterruptedException {
        try {
            // data
            String commitOrAbort = messageReceived.split("-")[0];
            String timestamp = messageReceived.split("-")[1];
            String eventName = messageReceived.split("-")[2];
            Boolean isProposer = (Boolean) stateMachine.getExtendedState().getVariables().get("Proposer");
            Boolean isInitialMessage = (Boolean) stateMachine.getExtendedState().getVariables().get("InitialMessage");

            // if the machine is proposer, then it can evaluate the responses
            if (isProposer) {
                // current message and response
                String currentMessage = timestamp + "-" + eventName;
                Boolean currentResponse = stringToBoolean(commitOrAbort);

                // if it is the initial message
                if (isInitialMessage) {
                    stateMachine.getExtendedState().getVariables().put("LastMessage", currentMessage);
                    stateMachine.getExtendedState().getVariables().put("LastResponse", currentResponse);
                    stateMachine.getExtendedState().getVariables().put("InitialMessage", false);
                }

                else {
                    // last message and response received
                    String lastMessage = stateMachine.getExtendedState().getVariables().get("LastMessage").toString();
                    Boolean lastResponse = (Boolean) stateMachine.getExtendedState().getVariables().get("LastResponse");

                    // if they are the same, continue to collect responses for the propose
                    if (lastMessage.equals(currentMessage)) {
                        stateMachine.getExtendedState().getVariables().put("LastResponse", currentResponse && lastResponse);
                    }

                    // else, send the commit/abort command
                    else {
                        commitFanoutSender.send(lastResponse, lastMessage);
                        stateMachine.getExtendedState().getVariables().put("LastMessage", currentMessage);
                        stateMachine.getExtendedState().getVariables().put("LastResponse", currentResponse);
                    }
                }
            }
        }

        catch (Exception e) {
            Thread.sleep(500);
        }
    }

}
