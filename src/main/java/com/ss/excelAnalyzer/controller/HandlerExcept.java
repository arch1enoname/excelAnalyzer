/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.controller;

import com.ss.Except4Support;
import com.ss.config.js.ExceptConf;
import com.ss.config.monitor.MonitorErrorsService;
import com.ss.excelAnalyzer.service.AnalyzerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;


/**
 * @author michael
 */
@Order(1)
@ControllerAdvice
public class HandlerExcept {

    private final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";

    @Autowired
    private AnalyzerService actionService;

    private final static Logger logger = Logger.getLogger(HandlerExcept.class.getName());
    @Autowired
    private MonitorErrorsService monitorErrorsService;

    public HandlerExcept() {
    }

    @ExceptionHandler({ExceptConf.class})
    public void onFatal(HttpServletRequest req, ExceptConf ex) {
        logger.severe("[--FATAL--] " + "[Configuration Exception" + "] " + ex.getMessage4Support());
        monitorErrorsService.addError(ex.getMessage4Monitor());
        System.exit(-1);
    }
}
