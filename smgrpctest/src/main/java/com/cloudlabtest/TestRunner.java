package com.cloudlabtest;

import com.cloudlabtest.tests.MultiClients;

import java.io.IOException;

public class TestRunner {
    public static void main(String[] args) throws IOException, InterruptedException {
        MultiClients multiClients = new MultiClients("src\\main\\resources\\parameters.yaml");
        multiClients.runTest();
    }
}
