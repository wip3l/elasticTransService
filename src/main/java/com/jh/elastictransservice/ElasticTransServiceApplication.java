package com.jh.elastictransservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

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
