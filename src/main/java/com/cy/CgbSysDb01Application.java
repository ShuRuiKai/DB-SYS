package com.cy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching  //启动业务层缓存配置
@EnableAsync   //启动异步配置(底层会初始化一个线程池)
@SpringBootApplication
public class CgbSysDb01Application {

	public static void main(String[] args) {
		SpringApplication.run(CgbSysDb01Application.class, args);
	}

}
