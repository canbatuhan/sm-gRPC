package com.cloudlab.statemachine;

import com.cloudlab.yamlprocessor.Components;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;


public class StateMachineGenerator {

    private Components readYamlInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        return mapper.readValue(new File("src/main/resources/statemachine.yaml"), Components.class);
    }

    public StateMachine<String, String> buildMachine() throws Exception {
        Components components = this.readYamlInput();

        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
        builder.configureStates()
                .withStates()
                .initial("S1")
                .end("SF")
                .states(new HashSet<String>(Arrays.asList("S1","S2","S3","S4")));
        return builder.build();
    }

}
