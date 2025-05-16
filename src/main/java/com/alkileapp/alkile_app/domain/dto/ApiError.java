package com.alkileapp.alkile_app.domain.dto;

import java.time.LocalDateTime;

public class ApiError {
    private String backendMessage;
    private String url;
    private String method;
    private LocalDateTime timestamp;
    private String message;

    public ApiError(String message, String backendMessage, String url, String method) {
        this.message = message;
        this.backendMessage = backendMessage;
        this.url = url;
        this.method = method;
        this.timestamp = LocalDateTime.now();
    }

    public String getBackendMessage() {
        return backendMessage;
    }

    public void setBackendMessage(String backendMessage) {
        this.backendMessage = backendMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}