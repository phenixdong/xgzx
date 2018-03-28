package com.xgzx.admin;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
 

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.xgzx.admin", "com.xgzx.interceptor"})
public class AdminApplication {	
	
	@Value("${uploadPath}")
    private String location;
	
	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
	
//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setLocation(location + "tmp/");
//        return factory.createMultipartConfig();
//    }
	 
}
