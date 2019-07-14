package com.baichen.base;

import com.baichen.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @Program: BaseApplication
 * @Author: baichen
 * @Description: 启动类
 */
@SpringBootApplication
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class);
    }

    // 自动生成ID,返回一个IdWorker对象
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }
}
