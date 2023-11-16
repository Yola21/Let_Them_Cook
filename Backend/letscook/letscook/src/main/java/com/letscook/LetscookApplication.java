package com.letscook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LetscookApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetscookApplication.class, args);
        System.out.println("Hello World, Let's Cook");
    }

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest()
//						.authenticated())
//				.httpBasic(withDefaults())
//				.formLogin(withDefaults())
//				.csrf(AbstractHttpConfigurer::disable);
//		return http.build();
//	}

}
