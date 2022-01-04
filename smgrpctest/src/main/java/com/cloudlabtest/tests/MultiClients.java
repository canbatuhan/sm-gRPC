package com.cloudlabtest.tests;

import com.cloudlabtest.threads.ClientThread;
import com.cloudlabtest.threads.ServerThread;
import com.cloudlabtest.yamlprocessor.Parameters;
import com.cloudlabtest.yamlprocessor.TestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MultiClients {
    Parameters params;
    ArrayList<ClientThread> clientThreads;

    /**
     * Build MultiClient test with given parameters
     * @param paramsPath file path includes test parameters
     */
    public MultiClients(String paramsPath) throws IOException {
        this.params = this.readYamlInput(paramsPath);
        this.clientThreads = new ArrayList<>();
    }

    /**
     * Reads yaml input
     * @param paramsPath file path including test params
     * @return Parameters object
     */
    private Parameters readYamlInput(String paramsPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        return mapper.readValue(new File(paramsPath), Parameters.class);
    }

    /**
     * Build list of ClientThreads in order to run in test
     */
    private void buildClientThreads() {
        for (TestClient testClient : this.params.getClients()) {
            clientThreads.add(new ClientThread(
                    testClient.getConfigPath(),
                    testClient.getInputPath(),
                    testClient.getOutputPath()
                )
            );
        }
    }

    /**
     * Runner of the MultiClient test
     */
    public void runTest() throws InterruptedException {
        ServerThread server = new ServerThread();
        buildClientThreads();

        server.start();
        Thread.sleep(100);
        for (ClientThread clientThread : this.clientThreads) clientThread.start();
    }
}
