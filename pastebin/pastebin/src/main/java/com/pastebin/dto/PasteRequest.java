package com.pastebin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasteRequest {

    private String content;

    @JsonProperty("ttl_seconds")
    private Long ttlSeconds;

    @JsonProperty("max_views")
    private Integer maxViews;
}
