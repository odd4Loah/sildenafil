package com.jnshu.sildenafil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lihoo
 */
@MapperScan("com.jnshu.sildenafil.system.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class SildenafilApplication {
    public static void main(String[] args) {
        SpringApplication.run(SildenafilApplication.class, args);
        System.out.println("******* spring boot sildenafil start success *******");
    }
}
