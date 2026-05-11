package com.fnk.app.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 后台管理端启动入口。
 *
 * @author Enigma
 */
@SpringBootApplication(scanBasePackages = "com.fnk")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
