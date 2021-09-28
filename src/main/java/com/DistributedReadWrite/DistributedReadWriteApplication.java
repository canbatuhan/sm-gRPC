package com.DistributedReadWrite;

import com.DistributedReadWrite.Runners.ClientRunner;
import com.DistributedReadWrite.Runners.InputRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class DistributedReadWriteApplication {

	@Profile("usage_message")
	@Bean
	public CommandLineRunner usageRunner() {
		return args -> {
			// Usage Message
			System.out.println("This app uses Spring Profiles to control its behavior.");
			System.out.println("Sample usage to start the client machines,");
			System.out.println("java -jar JAR_FILE_NAME.jar --spring.profiles.active=client");
		};
	}

	@Profile("input")
	@Bean
	public CommandLineRunner inputRunner() {
		return new InputRunner();
	}

	@Profile("client")
	@Bean
	public CommandLineRunner clientRunner() {
		return new ClientRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(DistributedReadWriteApplication.class, args);
	}

}
