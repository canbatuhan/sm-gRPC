package com.cloudlabtest.tests;

import com.cloudlabtest.threads.ClientThread;
import com.cloudlabtest.threads.ServerThread;


public class SmokeTest {

    /**
     * Runner of the SmokeTest
     */
    public void runTest() throws InterruptedException {
        Thread client = new ClientThread(
                "C:\\Users\\Batuhan\\Documents\\GitHub\\DistributedReadWrite\\smgrpctest\\src\\main\\resources\\statemachine.yaml",
                "C:\\Users\\Batuhan\\Documents\\GitHub\\DistributedReadWrite\\smgrpctest\\src\\main\\resources\\_smokeInput.txt",
                "C:\\Users\\Batuhan\\Documents\\GitHub\\DistributedReadWrite\\smgrpctest\\src\\main\\resources\\_smokeOutput.txt"
        );

        Thread server = new ServerThread();

        server.start();
        Thread.sleep(100);
        client.start();
    }
}
