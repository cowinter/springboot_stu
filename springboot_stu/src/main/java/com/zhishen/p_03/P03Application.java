package com.zhishen.p_03;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@MapperScan("com.zhishen.p_03.mapper")
public class P03Application {
	public static void main(String[] args) {
        SpringApplication.run(P03Application.class, args);
	}
}
