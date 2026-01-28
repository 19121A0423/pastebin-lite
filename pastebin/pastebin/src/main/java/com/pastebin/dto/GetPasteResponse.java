package com.pastebin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class GetPasteResponse {

    private String content;
    private Integer remainingViews;
    private String expiresAt;

}
