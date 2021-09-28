package com.DistributedReadWrite.Actions;

import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class ReadingAction implements Action<SMStates, SMEvents> {

    @Override
    public void execute(StateContext<SMStates, SMEvents> stateContext) {
        System.out.println("actual reading...");

        Integer timestamp = (Integer) stateContext.getExtendedState().getVariables().get("Timestamp");
        stateContext.getExtendedState().getVariables().put("Timestamp", timestamp+1);
        stateContext.getStateMachine().sendEvent(SMEvents.DONE);
    }

}
