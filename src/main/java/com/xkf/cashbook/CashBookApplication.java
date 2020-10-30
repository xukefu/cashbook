package com.xkf.cashbook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** 记账吧
 * @author xukf01
 */
@SpringBootApplication
@MapperScan("com.xkf.cashbook.web.mapper")
@EnableScheduling
public class CashBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashBookApplication.class, args);
	}
}
