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
import java.util.HashSet;


public class StateMachineGenerator {

    private Configurations readYamlInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        return mapper.readValue(new File("src/main/resources/statemachine.yaml"), Configurations.class);
    }

    public StateMachine<String, String> buildMachine() throws Exception {
        Configurations configurations = this.readYamlInput();
        ArrayList<State> states = configurations.getStates();
        ArrayList<Transition> transitions = configurations.getTransitions();

        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId(configurations.getMachineID())
                .autoStartup(configurations.isAutoStartup());

        builder.configureStates()
                .withStates()
                .initial(configurations.getInitialState().getName())
                .states(new HashSet<>(configurations.getAllStateNames()));

        return builder.build();
    }

}
