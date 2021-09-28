package com.DistributedReadWrite.MessageBroker.Receivers;

import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;

@RabbitListener(queues = "#{commitFanoutQueue.name}")
public class CommitFanoutReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StateMachine<SMStates, SMEvents> stateMachine;

    public void receive(String messageReceived) throws InterruptedException {
        try {
            // data parsing
            String commitOrAbort = messageReceived.split("-")[0];
            String timestamp = messageReceived.split("-")[1];
            String eventName = messageReceived.split("-")[2];

            if (commitOrAbort.equals("commit")) {
                Integer stateMachineTimestamp = (Integer) stateMachine.getExtendedState().getVariables().get("Timestamp");

                if (stateMachineTimestamp < Integer.parseInt(timestamp)) {
                    stateMachine.getExtendedState().getVariables().put("Timestamp", Integer.valueOf(timestamp));
                }

                stateMachine.sendEvent(SMEvents.valueOf(eventName));
            }

        }

        catch (Exception e) {
            Thread.sleep(500);
        }
    }

}
