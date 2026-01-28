package com.pastebin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/healthz")
public class HealthController {

    private final StringRedisTemplate redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    public HealthController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Boolean>> healthCheck() {

        log.info("Health check request received");

        redisTemplate.opsForValue().set("healthz:test", "ok");
        String value = redisTemplate.opsForValue().get("healthz:test");

        boolean isHealthy = "ok".equals(value);

        log.info("Health check completed: redisHealthy={}", isHealthy);

        Map<String, Boolean> response = new HashMap<>();
        response.put("ok", isHealthy);

        return ResponseEntity.ok(response);
    }

}

