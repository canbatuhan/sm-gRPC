package com.DistributedReadWrite.MessageBroker.Senders;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class ResponseFanoutSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange responseFanout;

    public void send(String messageToSend) throws InterruptedException {
        // sends the event data to ResponseFanout
        System.out.println("[x] '" + messageToSend + "' --> ResponseFanout");
        rabbitTemplate.convertAndSend(responseFanout.getName(), "", messageToSend);
    }

}
