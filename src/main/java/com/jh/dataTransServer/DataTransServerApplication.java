package com.jh.dataTransServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author liqijian
 */
@SpringBootApplication
@MapperScan("com.jh.dataTransServer.mapper")
@EnableAsync
public class DataTransServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataTransServerApplication.class, args);
    }

}
