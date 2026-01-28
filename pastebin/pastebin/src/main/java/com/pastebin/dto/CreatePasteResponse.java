package com.pastebin.dto;

import lombok.Getter;

@Getter
public class CreatePasteResponse {

    private String id;
    private String url;

    public CreatePasteResponse(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
