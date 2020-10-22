package com.xkf.cashbook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xkf.cashbook.web.mapper")
public class CashBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashBookApplication.class, args);
	}
}
