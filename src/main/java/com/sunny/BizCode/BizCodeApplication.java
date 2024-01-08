package com.sunny.BizCode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.sunny.BizCode.dao"})
public class BizCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BizCodeApplication.class, args);
	}

}
