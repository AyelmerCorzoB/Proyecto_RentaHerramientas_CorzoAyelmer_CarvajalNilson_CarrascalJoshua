package com.alkileapp.alkile_app.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ApiError implements Serializable {
    
    private String backedMessage;
    private String message;
    private int httpCode;
    private LocalDateTime time;
    public String getBackedMessage() {
        return backedMessage;
    }
    public void setBackedMessage(String backedMessage) {
        this.backedMessage = backedMessage;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getHttpCode() {
        return httpCode;
    }
    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
