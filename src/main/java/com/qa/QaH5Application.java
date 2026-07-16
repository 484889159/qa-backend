package com.qa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qa.mapper")
public class QaH5Application {

    public static void main(String[] args) {
        SpringApplication.run(QaH5Application.class, args);
        System.out.println("========================================");
        System.out.println("  ✅ QA-H5 后端服务启动成功！");
        System.out.println("  🚀 访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}