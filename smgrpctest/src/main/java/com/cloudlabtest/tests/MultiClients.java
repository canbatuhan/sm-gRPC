package com.cloudlabtest.tests;

import com.cloudlabtest.yamlprocessor.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class MultiClients {
    String paramsPath;

    public MultiClients(String paramsPath) {
        this.paramsPath = paramsPath;
    }

    public Parameters readParams() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        return mapper.readValue(new File(this.paramsPath), Parameters.class);
    }

}
