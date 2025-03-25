/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.controller.api.response;

/**
 * @author Marina
 */
public class SuccessResponse<T> implements Response {

    private T data;
    private String message;

    public SuccessResponse() {
    }

    public SuccessResponse(T data, String message) {
        this.message = message;
        this.data = data;
    }

    public SuccessResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
