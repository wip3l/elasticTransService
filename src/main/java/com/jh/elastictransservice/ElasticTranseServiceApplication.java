package com.jh.elastictransservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author liqijian
 */
@SpringBootApplication
@EnableAsync
public class ElasticTranseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticTranseServiceApplication.class, args);
    }

}
