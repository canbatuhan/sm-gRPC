package com.cloudlab.statemachine;

import com.cloudlab.yamlprocessor.Configurations;
import com.cloudlab.yamlprocessor.State;
import com.cloudlab.yamlprocessor.Transition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.state.PseudoStateKind;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class StateMachineGenerator {
    private final String configPath;

    /**
     * Builds a StateMachineGenerator which builds a state machine
     * @param path input file which stores configuration details
     */
    public StateMachineGenerator(String path) {
        this.configPath = path;
    }

    /**
     * Reads .yaml file to get the configurations
     * @return Configuration object, filled with the yaml input
     * @throws IOException readValue can give an error
     */
    private Configurations readYamlInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        return mapper.readValue(new File(this.configPath), Configurations.class);
    }

    /**
     * Builds a state machine according to the inputs by using StateMachineBuilder
     * @return StateMachine<String, String>
     * @throws Exception readYamlInput can give an error
     */
    public StateMachine<String, String> buildMachine() throws Exception {
        Configurations configurations = this.readYamlInput();
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
                        .initial("INITIAL", new ReadWriteAction(state.getReadVariables(), state.getWriteVariables()));
                continue;
            }

            builder.configureStates()
                    .withStates()
                    .state(state.getName(), new ReadWriteAction(state.getReadVariables(), state.getWriteVariables()));
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
