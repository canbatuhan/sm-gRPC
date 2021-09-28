package com.DistributedReadWrite.Actions;

import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class InitializingAction implements Action<SMStates, SMEvents> {

    @Override
    public void execute(StateContext<SMStates, SMEvents> stateContext) {
        stateContext.getExtendedState().getVariables().put("ReadyForInput", true);
        stateContext.getExtendedState().getVariables().put("Proposer", false);
        stateContext.getExtendedState().getVariables().put("CanQuery", false);
    }

}
