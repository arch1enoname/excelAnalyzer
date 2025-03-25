package com.ss.excelAnalyzer.controller.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "status", "date", "action_id", "result" })
public class ApiResponseDto {

    public static final String RESULT = "result";
    public static final String STATUS = "status";
    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";
    
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private List<Object> result;

    public ApiResponseDto(String status, Object result) {
        this.status = status;
        this.date = LocalDateTime.now();
        this.result = new ArrayList<>();
        this.result.add(result);
    }

    public List<Object> getResult() {
        return result;
    }

    public void setResult(List<Object> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}

