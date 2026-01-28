package com.pastebin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Paste {

    private String content;
    private long createdAt;
    private long expiresAt;
    private int views;
    private Integer maxViews;

    public Paste(String content, long createdAt, long expiresAt, Integer maxViews) {
        this.content = content;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.views = 0;
        this.maxViews = maxViews;
    }

    public void incrementViews() {
        this.views++;
    }
}
