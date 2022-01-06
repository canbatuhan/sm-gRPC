package com.cloudlab;


import com.cloudlab.grpclient.Client;


public class ClientRunner {

    public static void main(String[] args) throws Exception {
        Client machine = new Client(
                "src\\main\\resources\\statemachine.yaml",
                "src\\main\\resources\\_eventInputs.txt",
                "src\\main\\resources\\_eventOutputs.txt");
        machine.run();
    }

}
