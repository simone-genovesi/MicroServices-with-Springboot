package it.cgmconsulting.ms_post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsPostApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPostApplication.class, args);
	}

}
