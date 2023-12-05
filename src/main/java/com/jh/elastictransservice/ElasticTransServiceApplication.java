package com.jh.elastictransservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author liqijian
 */
@SpringBootApplication
@MapperScan("com.jh.elastictransservice.mapper")
@EnableAsync
public class ElasticTransServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticTransServiceApplication.class, args);
    }

}
