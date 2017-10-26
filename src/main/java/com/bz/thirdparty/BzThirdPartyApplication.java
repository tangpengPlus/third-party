package com.bz.thirdparty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.bz.dao.mapper")
public class BzThirdPartyApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(BzThirdPartyApplication.class, args);
	}
}
