package com.DistributedReadWrite.StateMachine;

import com.DistributedReadWrite.Actions.BasicListener;
import com.DistributedReadWrite.Actions.InitializingAction;
import com.DistributedReadWrite.Actions.ReadingAction;
import com.DistributedReadWrite.Actions.WritingAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<SMStates, SMEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<SMStates, SMEvents> config) throws Exception {
        config
                .withConfiguration()
                .machineId("Read Write Machine")
                .autoStartup(false)
                .listener(basicListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<SMStates, SMEvents> states) throws Exception {
        states
                .withStates()
                .state(SMStates.S_waiting, initializingAction(), null)
                .state(SMStates.S_reading, readingAction())
                .state(SMStates.S_writing, writingAction())
                .initial(SMStates.S_waiting);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SMStates, SMEvents> transitions) throws Exception {
        transitions
                .withExternal().source(SMStates.S_waiting).target(SMStates.S_reading).event(SMEvents.READ)
                .and()
                .withExternal().source(SMStates.S_waiting).target(SMStates.S_writing).event(SMEvents.WRITE)
                .and()
                .withExternal().source(SMStates.S_reading).target(SMStates.S_waiting).event(SMEvents.DONE)
                .and()
                .withExternal().source(SMStates.S_writing).target(SMStates.S_waiting).event(SMEvents.DONE);
    }

    @Bean
    public StateMachineListener<SMStates, SMEvents> basicListener() {
        return new BasicListener();
    }

    @Bean
    public Action<SMStates, SMEvents> initializingAction() {
        return new InitializingAction();
    }

    @Bean
    public Action<SMStates, SMEvents> readingAction() {
        return new ReadingAction();
    }

    @Bean
    public Action<SMStates, SMEvents> writingAction() {
        return new WritingAction();
    }

}
