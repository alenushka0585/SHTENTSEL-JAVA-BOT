package de.tel_ran.shtentsel_java_telegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class ShtentselJavaBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShtentselJavaBotApplication.class, args);
	}

}
