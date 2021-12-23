package com.cloudlab;


import com.cloudlab.grpclient.Client;

public class SmokeTest {
    public void foo() throws Exception {
        Client client = new Client();
        //client.readInput();
        client.run();
    }
}
