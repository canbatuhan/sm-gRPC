package com.DistributedReadWrite.MessageBroker.Senders;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class InputQueueSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue inputQueue;

    public void send(String messageToSend) {
        // sends the event data to InputQueue
        System.out.println("[x] '" + messageToSend + "' --> InputQueue");
        rabbitTemplate.convertAndSend(inputQueue.getName(), messageToSend);
    }

}
