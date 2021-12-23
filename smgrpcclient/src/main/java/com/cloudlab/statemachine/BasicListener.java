package com.cloudlab.statemachine;


import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class BasicListener extends StateMachineListenerAdapter<String, String> {

    /**
     * Notifies the user when there is a state change
     * @param from state that the machine transited from
     * @param to state that the machine transited to
     */
    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        if (from == null) System.out.println("--> " + to.getId());
        else System.out.println(from.getId() + " --> " + to.getId());
    }

}
