package com.codegnan.shopnest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ShopNestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopNestApplication.class, args);
		
		
	}

}
