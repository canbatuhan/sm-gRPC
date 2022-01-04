package com.cloudlab;


import com.cloudlab.grpclient.Client;


public class ClientRunner {

    public static void main(String[] args) throws Exception {
        Client machine = new Client("src\\resources\\_eventInputs.txt");
        machine.run();
    }

}
