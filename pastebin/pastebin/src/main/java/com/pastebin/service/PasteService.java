package com.pastebin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pastebin.model.Paste;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class PasteService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecureRandom random = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(PasteService.class);

    public PasteService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String createPaste(String content, Long ttlSeconds, Integer maxViews) throws Exception {

        log.info("Creating paste: ttlSeconds={}, maxViews={}", ttlSeconds, maxViews);

        String id = generateId();
        String key = "paste:" + id;

        long now = Instant.now().toEpochMilli();
        long expiresAt = ttlSeconds != null
                ? now + (ttlSeconds * 1000)
                : Long.MAX_VALUE;

        Paste paste = new Paste(content, now, expiresAt, maxViews);

        String json = objectMapper.writeValueAsString(paste);

        redisTemplate.opsForValue().set(key, json);

        if (ttlSeconds != null) {
            redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
            log.info("TTL set for paste id={} ({} seconds)", id, ttlSeconds);
        }

        log.info("Paste stored successfully in Redis, id={}", id);

        return id;
    }

    private String generateId() {
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }


    public Paste getPaste(String id) throws Exception {

        log.info("Fetching paste from Redis, id={}", id);

        String key = "paste:" + id;

        String json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            log.warn("Paste not found in Redis, id={}", id);
            return null;
        }

        Paste paste = objectMapper.readValue(json, Paste.class);

        long now = Instant.now().toEpochMilli();

        // expired (extra safety)
        if (paste.getExpiresAt() != Long.MAX_VALUE && paste.getExpiresAt() < now) {
            log.warn("Paste expired, id={}", id);
            return null;
        }

        // max views exceeded
        if (paste.getMaxViews() != null && paste.getViews() >= paste.getMaxViews()) {
            log.warn("Paste view limit exceeded, id={}, views={}",
                    id, paste.getViews());
            return null;
        }

        // increment view count
        paste.incrementViews();

        redisTemplate.opsForValue().set(
                key,
                objectMapper.writeValueAsString(paste)
        );

        log.info("Paste served successfully, id={}, currentViews={}",
                id, paste.getViews());

        return paste;
    }
}