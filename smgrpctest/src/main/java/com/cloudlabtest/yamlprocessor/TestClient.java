package com.cloudlabtest.yamlprocessor;

public class TestClient {
    String configPath; // Config file path, in which a state machine will be configured
    String inputPath; // File path that stores event inputs
    String outputPath; // File path where the outputs will be written into

    /**
     * Builds a TestClient object with default settings
     */
    public TestClient() {
        this.configPath = null;
        this.inputPath = null;
        this.outputPath = null;
    }

    /**
     * Builds a TestClient object with parameters
     * @param configPath file path for state machine configurations
     * @param inputPath file path for event inputs
     * @param outputPath file path for test results
     */
    public TestClient(String configPath, String inputPath, String outputPath) {
        this.configPath = configPath;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }
}
