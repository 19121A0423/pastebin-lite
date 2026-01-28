package com.pastebin.controller;


import com.pastebin.dto.GetPasteResponse;
import com.pastebin.dto.PasteRequest;
import com.pastebin.dto.CreatePasteResponse;
import com.pastebin.model.Paste;
import com.pastebin.service.PasteService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.time.Instant;
import java.util.Map;


@RestController
public class PasteController {

    @Autowired
    private final PasteService pasteService;
    private static final Logger log = LoggerFactory.getLogger(PasteController.class);

    public PasteController(PasteService pasteService) {
        this.pasteService = pasteService;
    }

    @PostMapping("/pastes")
    public ResponseEntity<?> createPaste(@RequestBody PasteRequest request, HttpServletRequest httpRequest) throws Exception {

        log.info("Create paste request received");

        // 1️⃣ Validate content
        if (request.getContent() == null || request.getContent().isBlank()) {
            log.warn("Create paste failed: content is empty or null");
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "content must be a non-empty string"));
        }

        // 2️⃣ Validate ttl_seconds
        if (request.getTtlSeconds() != null && request.getTtlSeconds() < 1) {
            log.warn("Create paste failed: invalid ttl_seconds={}", request.getTtlSeconds());
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "ttl_seconds must be >= 1"));
        }

        // 3️⃣ Validate max_views
        if (request.getMaxViews() != null && request.getMaxViews() < 1) {
            log.warn("Create paste failed: invalid max_views={}", request.getMaxViews());
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "max_views must be >= 1"));
        }

        // 4️⃣ Create paste
        String id = pasteService.createPaste(
                request.getContent(),
                request.getTtlSeconds(),
                request.getMaxViews()
        );

        String baseUrl =
                httpRequest.getScheme() + "://" +
                        httpRequest.getServerName() +
                        ":" +
                        httpRequest.getServerPort();

        String url = baseUrl + "/p/" + id;

        log.info("Paste created successfully: id={}, ttl={}, maxViews={}",
                id, request.getTtlSeconds(), request.getMaxViews());

        return ResponseEntity.ok(
                new CreatePasteResponse(id, url)
        );
    }


    @GetMapping("/pastes/{id}")
    public ResponseEntity<?> getPaste(@PathVariable String id) throws Exception {

        log.info("Fetching paste via API, id={}", id);

        Paste paste = pasteService.getPaste(id);

        if (paste == null) {
            log.warn("Paste not found or unavailable, id={}", id);
            return ResponseEntity
                    .status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Paste not found or expired"));
        }

        Integer remainingViews = null;
        if (paste.getMaxViews() != null) {
            remainingViews = paste.getMaxViews() - paste.getViews();
        }

        String expiresAt = null;
        if (paste.getExpiresAt() != Long.MAX_VALUE) {
            expiresAt = Instant.ofEpochMilli(paste.getExpiresAt()).toString();
        }

        log.info("Paste fetched successfully, id={}, remainingViews={}",
                id, remainingViews);

        return ResponseEntity.ok(
                new GetPasteResponse(
                        paste.getContent(),
                        remainingViews,
                        expiresAt
                )
        );
    }


    @GetMapping(value = "/p/{id}", produces = "text/html")
    public ResponseEntity<String> viewPasteHtml(@PathVariable String id) throws Exception {

        log.info("Fetching paste for HTML view, id={}", id);

        Paste paste = pasteService.getPaste(id);

        if (paste == null) {
            log.warn("HTML view requested for unavailable paste, id={}", id);
            return ResponseEntity
                    .status(404)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<h1>404 - Paste not found or expired</h1>");
        }

        log.info("HTML view served for paste id={}", id);

        String escapedContent = HtmlUtils.htmlEscape(paste.getContent());

        String html = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Paste</title>
            <meta charset="UTF-8">
        </head>
        <body>
            <pre>%s</pre>
        </body>
        </html>
        """.formatted(escapedContent);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }


}
