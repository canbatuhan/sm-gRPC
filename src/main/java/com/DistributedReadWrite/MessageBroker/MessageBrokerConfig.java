package com.DistributedReadWrite.MessageBroker;

import com.DistributedReadWrite.MessageBroker.Receivers.CommitFanoutReceiver;
import com.DistributedReadWrite.MessageBroker.Receivers.InputQueueReceiver;
import com.DistributedReadWrite.MessageBroker.Receivers.QueryFanoutReceiver;
import com.DistributedReadWrite.MessageBroker.Receivers.ResponseFanoutReceiver;
import com.DistributedReadWrite.MessageBroker.Senders.CommitFanoutSender;
import com.DistributedReadWrite.MessageBroker.Senders.InputQueueSender;
import com.DistributedReadWrite.MessageBroker.Senders.QueryFanoutSender;
import com.DistributedReadWrite.MessageBroker.Senders.ResponseFanoutSender;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MessageBrokerConfig {

    @Bean
    public Queue inputQueue() {
        return new Queue("InputQueue");
    }

    @Profile("input")
    private static class InputMachineConfig {

        @Bean
        public InputQueueSender inputQueueSender() {
            return new InputQueueSender();
        }

    }

    @Profile("client")
    private static class ClientMachineConfig {

        /* InputQueue Config */
        @Bean
        public InputQueueReceiver inputQueueReceiver() {
            return new InputQueueReceiver();
        }


        /* QueryFanout Config */
        @Bean
        public FanoutExchange queryFanout() {
            return new FanoutExchange("query.fanout");
        }

        @Bean
        public Queue queryFanoutQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding queryFanoutBinding() {
            return BindingBuilder.bind(queryFanoutQueue()).to(queryFanout());
        }

        @Bean
        public QueryFanoutSender queryFanoutSender() {
            return new QueryFanoutSender();
        }

        @Bean
        public QueryFanoutReceiver queryFanoutReceiver() {
            return new QueryFanoutReceiver();
        }


        /* ResponseFanout Config */
        @Bean
        public FanoutExchange responseFanout() {
            return new FanoutExchange("response.fanout");
        }

        @Bean
        public Queue responseFanoutQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding responseFanoutBinding() {
            return BindingBuilder.bind(responseFanoutQueue()).to(responseFanout());
        }

        @Bean
        public ResponseFanoutSender responseFanoutSender() {
            return new ResponseFanoutSender();
        }

        @Bean
        public ResponseFanoutReceiver responseFanoutReceiver() {
            return new ResponseFanoutReceiver();
        }


        /* CommitFanout Config */
        @Bean
        public FanoutExchange commitFanout() {
            return new FanoutExchange("commit.fanout");
        }

        @Bean
        public Queue commitFanoutQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding commitFanoutBinding() {
            return BindingBuilder.bind(commitFanoutQueue()).to(commitFanout());
        }

        @Bean
        public CommitFanoutSender commitFanoutSender() {
            return new CommitFanoutSender();
        }

        @Bean
        public CommitFanoutReceiver commitFanoutReceiver() {
            return new CommitFanoutReceiver();
        }

    }

}
