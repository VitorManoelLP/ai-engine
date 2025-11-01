package com.engine.ai;

import org.springframework.boot.SpringApplication;

public class TestAiApplication {

	public static void main(String[] args) {
		SpringApplication.from(AiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
