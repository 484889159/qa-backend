package com.qa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
public class AIConfig {

    @Value("${ai.deepseek.api-key}")
    private String apiKey;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getApiKey() {
        return apiKey;
    }

    @PostConstruct
    public void init() {
        System.out.println("========================================");
        System.out.println("🔑 API Key 状态: " + (apiKey != null && !apiKey.isEmpty() ? "✅ 已加载 (" + apiKey.substring(0, 10) + "...)" : "❌ 未加载"));
        System.out.println("========================================");
    }
}