package com.DistributedReadWrite.MessageBroker.Receivers;

import com.DistributedReadWrite.MessageBroker.Senders.ResponseFanoutSender;
import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.transition.Transition;

import java.util.Collection;

@RabbitListener(queues = "#{queryFanoutQueue.name}")
public class QueryFanoutReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StateMachine<SMStates, SMEvents> stateMachine;

    @Autowired
    private ResponseFanoutSender responseFanoutSender;

    private Boolean isTriggering(SMEvents eventData) {
        Collection<Transition<SMStates, SMEvents>> transitionCollection = stateMachine.getTransitions();
        SMStates currentStateID = stateMachine.getState().getId();

        // checking each transition
        for (Transition<SMStates, SMEvents> transition : transitionCollection) {

            // check if the source of the is equal to current state
            SMStates sourceStateID = transition.getSource().getId();
            if (currentStateID == sourceStateID) {

                // check if the triggering event of the transition is equal to event data received
                SMEvents triggeringEvent = transition.getTrigger().getEvent();
                if (eventData == triggeringEvent) {
                    return Boolean.TRUE;
                }

            }

        }

        return Boolean.FALSE;
    }

    /*
        data format: %TIMESTAMP%-%EVENT%
    */
    @RabbitHandler
    public void receive(String messageReceived) throws InterruptedException {
        try {
            // acceptors should be closed to other inputs for a while
            stateMachine.getExtendedState().getVariables().replace("ReadyForInput", false);

            // data
            Integer receivedTimestamp = Integer.valueOf(messageReceived.split("-")[0]);
            SMEvents eventData = SMEvents.valueOf(messageReceived.split("-")[1]);

            // machine's local variables
            Integer localTimestamp = (Integer) stateMachine.getExtendedState().getVariables().get("Timestamp");
            Boolean canQuery = (Boolean) stateMachine.getExtendedState().getVariables().get("CanQuery");
            String commitOrAbort;

            if (!isTriggering(eventData)) commitOrAbort = "abort";
            else if (canQuery && (localTimestamp < receivedTimestamp)) commitOrAbort = "abort";
            else commitOrAbort = "commit";

            // data format: response-timestamp-event
            String messageToSend = commitOrAbort + "-" + messageReceived;
            responseFanoutSender.send(messageToSend);
        }

        catch (Exception e) {
            Thread.sleep(500);
        }
    }

}
