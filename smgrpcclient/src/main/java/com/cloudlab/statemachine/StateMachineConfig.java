package com.cloudlab.statemachine;

import com.cloudlab.utils.Events;
import com.cloudlab.utils.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachine(name = "stateMachine")
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config
                .withConfiguration()
                .machineId("gRPC-StateMachine")
                .autoStartup(false)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .state(States.S_init)
                .state(States.S_waiting)
                .state(States.S_reading, readingAction())
                .state(States.S_writing, writingAction())
                .initial(States.S_init);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal().source(States.S_init).target(States.S_waiting).event(Events.READ)
                .and()
                .withExternal().source(States.S_waiting).target(States.S_reading).event(Events.READ)
                .and()
                .withExternal().source(States.S_waiting).target(States.S_writing).event(Events.WRITE)
                .and()
                .withExternal().source(States.S_reading).target(States.S_waiting).event(Events.DONE)
                .and()
                .withExternal().source(States.S_writing).target(States.S_waiting).event(Events.DONE);
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                if (from == null) System.out.println("-->" + to.getId());
                else System.out.println(from.getId() + "-->" + to.getId());
            }
        };
    }

    @Bean
    public Action<States, Events> readingAction() {
        return new Action<States, Events>() {
            @Override
            public void execute(StateContext<States, Events> stateContext) {
                System.out.println("Reading...");
                try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
                stateContext.getStateMachine().sendEvent(Events.DONE);
            }
        };
    }

    @Bean
    public Action<States, Events> writingAction() {
        return new Action<States, Events>() {
            @Override
            public void execute(StateContext<States, Events> stateContext) {
                System.out.println("Writing...");
                try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
                stateContext.getStateMachine().sendEvent(Events.DONE);
            }
        };
    }

}
