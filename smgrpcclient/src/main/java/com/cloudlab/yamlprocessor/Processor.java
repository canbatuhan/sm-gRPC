package com.cloudlab.yamlprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.List;

public class Processor {
    private List<State> states;
    private List<Transitions> transitions;
}
