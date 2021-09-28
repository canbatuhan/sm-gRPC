package com.DistributedReadWrite.Actions;

import com.DistributedReadWrite.StateMachine.SMEvents;
import com.DistributedReadWrite.StateMachine.SMStates;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class BasicListener extends StateMachineListenerAdapter<SMStates, SMEvents> {

    @Override
    public void stateChanged(State<SMStates, SMEvents> from, State<SMStates, SMEvents> to) {
        if (from == null) System.out.println("-->" + to.getId());
        else System.out.println(from.getId() + "-->" + to.getId());
    }

}
