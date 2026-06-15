package com.university.repair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 宿舍报修与维修管理系统 - 启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.university.repair"})
public class RepairSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepairSystemApplication.class, args);
        System.out.println("================================================");
        System.out.println("    宿舍报修与维修管理系统已启动");
        System.out.println("    访问地址: http://localhost:8080");
        System.out.println("================================================");
    }
}
