/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author user
 */
@Controller
public class ErrorContr extends Contr implements ErrorController{
    
    public static final String JSP_ERROR = "error";
    public static final String DEFAULT_URL_ERROR =ROUTE_BASE+ JSP_ERROR;

    @GetMapping(DEFAULT_URL_ERROR)
    public String handleDefaultError(HttpServletRequest request, Model model) {
        return JSP_ERROR;
    }
}
