package com.cloudlab;


import com.cloudlab.grpclient.Client;


public class ClientRunner {

    public static void main(String[] args) throws Exception {
        Client machine = new Client();
        machine.readInput("C:\\Users\\Batuhan\\Documents\\GitHub\\DistributedReadWrite\\smgrpcclient\\eventInputs.txt");
        machine.run();
    }

}
