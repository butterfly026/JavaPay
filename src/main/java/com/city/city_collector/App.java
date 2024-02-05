package com.city.city_collector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ComponentScan(basePackages = {"com.city.city_collector.runner", "com.city.city_collector.interceptor",
        "com.city.city_collector.configuration", "com.city.city_collector.freemark", "com.city.city_collector.service",
        "com.city.city_collector.admin.*.*", "com.city.city_collector.admin.*.service.impl", "com.city.city_collector.task",
        "com.city.city_collector.web.*"})
@MapperScan({"com.city.city_collector.admin.*.dao"})
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableScheduling
@ServletComponentScan(basePackages = {"com.city.city_collector.service"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
