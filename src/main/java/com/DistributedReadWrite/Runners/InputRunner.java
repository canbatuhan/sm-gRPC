package com.DistributedReadWrite.Runners;

import com.DistributedReadWrite.MessageBroker.Senders.InputQueueSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class InputRunner implements CommandLineRunner {

    @Autowired
    private InputQueueSender inputQueueSender;

    @Override
    public void run(String... args) throws Exception {

        try {
            String fileName = "C:\\Users\\Batuhan\\IdeaProjects\\DistributedReadWrite\\_eventInputs.txt";
            FileReader fileReader = new FileReader(fileName);
            Scanner scannerReader = new Scanner(fileReader);

            while (scannerReader.hasNextLine()) {
                String eventData = scannerReader.nextLine();
                inputQueueSender.send(eventData);
            }

            scannerReader.close();
            fileReader.close();
        }

        catch (FileNotFoundException e) {
            System.out.println("File could not opened");
            e.printStackTrace();
        }

        catch (IOException e) {
            System.out.println("File could not closed.");
            e.printStackTrace();
        }

    }

}
