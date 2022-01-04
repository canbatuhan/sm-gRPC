package com.cloudlabtest.yamlprocessor;

import java.util.ArrayList;

public class Parameters {
    ArrayList<TestClient> clients; // List of clients running in test

    /**
     * Builds a Parameters class to read test parameters for each client
     */
    public Parameters() {
        this.clients = new ArrayList<>();
    }

    public ArrayList<TestClient> getClients() {
        return clients;
    }
}
