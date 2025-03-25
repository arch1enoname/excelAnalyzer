package com.ss.excelAnalyzer.controller.api;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

public class ErrorDetailsDto {

    public static final String ERROR_ID = "error_id";
    public static final String ERROR_CODE = "error_code";
    public static final String ERROR_MESSAGE = "error_message";
    private static final Random random = new Random(System.currentTimeMillis());
    
    @JsonProperty("error_id")
    private String errorId;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_message")
    private String errorMessage;

    public ErrorDetailsDto(String errorId, String errorCode, String errorMessage) {
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorDetailsDto(String errorCode, String errorMessage) {
        this.errorId = String.format("Ed%d", Math.abs(random.nextLong()));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public ErrorDetailsDto(String errorMessage){
        this.errorMessage = errorMessage;
    }


    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

