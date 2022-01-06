package com.cloudlab.statemachine;

import com.cloudlab.yamlprocessor.Configurations;
import com.cloudlab.yamlprocessor.State;
import com.cloudlab.yamlprocessor.Transition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class StateMachineGenerator {
    private final Configurations configurations; // Configuration file that stores the state machine details

    /**
     * Builds a StateMachineGenerator which builds a state machine
     * @param configurations configuration details
     */
    public StateMachineGenerator(Configurations configurations) {
        this.configurations = configurations;
    }

    /**
     * Builds a state machine according to the inputs by using StateMachineBuilder
     * @return StateMachine<String, String>
     */
    public StateMachine<String, String> buildMachine() throws Exception {
        Configurations configurations = this.configurations;
        ArrayList<State> states = configurations.getStates();
        ArrayList<Transition> transitions = configurations.getTransitions();

        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId(configurations.getMachineID())
                .autoStartup(configurations.isAutoStartup())
                .listener(new BasicListener());

        /* Configuring the states */
        for (State state : states) {
            if (state.getName().equals("INITIAL")) {
                builder.configureStates()
                        .withStates()
                        .initial("INITIAL");
            }

            else if (state.getName().equals("END")) {
                builder.configureStates()
                        .withStates()
                        .end("END");
            }

            else {
                builder.configureStates()
                        .withStates()
                        .state(state.getName(), new ReadWriteAction(state.getReadVariables(), state.getWriteVariables()));
            }
        }

        /* Configuring the transitions */
        for (Transition transition : transitions) {
            builder.configureTransitions()
                    .withExternal()
                    .source(transition.getFromState())
                    .target(transition.getToState())
                    .event(transition.getEvent());
        }

        return builder.build();
    }
}
