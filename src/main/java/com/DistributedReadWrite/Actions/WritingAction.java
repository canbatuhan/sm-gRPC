package com.DistributedReadWrite.Actions;

import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class WritingAction implements Action<SMStates, SMEvents> {

    @Override
    public void execute(StateContext<SMStates, SMEvents> stateContext) {
        Boolean isProposer = (Boolean) stateContext.getExtendedState().getVariables().get("Proposer");
        if (isProposer) System.out.println("actual writing...");
        else System.out.println("pseudo writing...");

        Integer timestamp = (Integer) stateContext.getExtendedState().getVariables().get("Timestamp");
        stateContext.getExtendedState().getVariables().put("Timestamp", timestamp+1);
        stateContext.getStateMachine().sendEvent(SMEvents.DONE);
    }

}
