/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.controller.api.response;

import com.ss.Except4Support;
import com.ss.Except4SupportDocumented;
import com.ss.ExceptInfoUser;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Marina
 */
@Getter
@Setter
public class ErrorResponse implements Response {
    private Long id;
    private String errorMessage;
    private Except4Support except4Support;
    private ExceptInfoUser except;
    private Except4SupportDocumented except4SupportDocumented;
    public ErrorResponse() {
    }

    public ErrorResponse(Long id, String errorMessage) {
        this.id = id;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(ExceptInfoUser except) {
        this.except = except;
    }

    public ErrorResponse(Except4Support except4Support) {this.except4Support = except4Support;}

    public ErrorResponse(Except4SupportDocumented except4SupportDocumented) {
        this.except4SupportDocumented = except4SupportDocumented;
    }
}
