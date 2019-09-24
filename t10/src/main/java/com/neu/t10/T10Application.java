package com.neu.t10;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 启动类加 @MapperScan  
 * @author admin
 *
 */
@SpringBootApplication
@MapperScan("com.neu.t10.dao")
public class T10Application {

	public static void main(String[] args) {
		SpringApplication.run(T10Application.class, args);
	}

}
