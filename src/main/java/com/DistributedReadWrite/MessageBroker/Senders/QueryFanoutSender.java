package com.DistributedReadWrite.MessageBroker.Senders;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryFanoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange queryFanout;

    public void send(String messageToSend) throws InterruptedException {
        // sends the event data to QueryFanout
        System.out.println("[x] '" + messageToSend + "' --> QueryFanout");
        rabbitTemplate.convertAndSend(queryFanout.getName(), "", messageToSend);
    }
}
