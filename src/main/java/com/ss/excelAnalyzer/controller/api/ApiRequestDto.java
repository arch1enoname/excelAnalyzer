package com.ss.excelAnalyzer.controller.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ApiRequestDto {

    public static final String DATE = "date";
    public static final String DATA = "data";    
    
    @NotNull(message = "Date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @NotNull(message = "Datа cannot be null")
    private Data data;

}