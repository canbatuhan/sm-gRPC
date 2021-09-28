package com.DistributedReadWrite.MessageBroker.Receivers;

import com.DistributedReadWrite.MessageBroker.Senders.QueryFanoutSender;
import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.transition.Transition;

import java.util.Collection;


@RabbitListener(queues = "InputQueue")
public class InputQueueReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StateMachine<SMStates, SMEvents> stateMachine;

    @Autowired
    private QueryFanoutSender queryFanoutSender;

    private Boolean isTriggering(SMEvents eventData) {
        Collection<Transition<SMStates, SMEvents>> transitionCollection = stateMachine.getTransitions();
        SMStates currentStateID = stateMachine.getState().getId();

        for (Transition<SMStates, SMEvents> transition : transitionCollection) {
            SMStates sourceStateID = transition.getSource().getId();
            if (currentStateID == sourceStateID) {
                SMEvents triggeringEvent = transition.getTrigger().getEvent();
                if (eventData == triggeringEvent) {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    @RabbitHandler
    public void receive(String messageReceived) throws InterruptedException {
        SMEvents eventData = SMEvents.valueOf(messageReceived);

        // initially state machine is ready for input
        Boolean readyForInput = (Boolean) stateMachine.getExtendedState().getVariables().get("ReadyForInput");

        // initially state machine's timestamp is 0
        Integer timestamp = (Integer) stateMachine.getExtendedState().getVariables().get("Timestamp");

        // if event WRITE has received set the machine as it can query
        if (eventData == SMEvents.WRITE) stateMachine.getExtendedState().getVariables().put("CanQuery", true);

        // wait until the machine get out of the action (commit-abort)
        while (!readyForInput || !isTriggering(eventData)) {
            Thread.sleep(500);
            System.out.println("waiting.......");
            readyForInput = (Boolean) stateMachine.getExtendedState().getVariables().get("ReadyForInput");
        }

        // message is ready to be processed
        System.out.println("[x] InputQueue -->'" + messageReceived + "'");

        // if READ, operate locally
        if (eventData == SMEvents.READ) {
            stateMachine.sendEvent(SMEvents.READ);
        }

        // if WRITE, start the commit-abort action
        else if (eventData == SMEvents.WRITE) {
            // from now on the machine is not ready for an input
            stateMachine.getExtendedState().getVariables().put("ReadyForInput", false);

            // it is the proposer of this "term"
            stateMachine.getExtendedState().getVariables().put("Proposer", true);

            // data format: timestamp-event
            String messageToSend = timestamp + "-" + eventData;
            queryFanoutSender.send(messageToSend);
        }

        else {
            System.out.println("Invalid event has received.");
        }
    }

}
