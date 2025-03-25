package com.ss.excelAnalyzer.controller.api;

import com.ss.ExceptInfoUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@ControllerAdvice
public class HandlerExceptFront {
    private final static Logger logger = Logger.getLogger(HandlerExceptFront.class.getName());

    @ExceptionHandler(ExceptInfoUser.class)
    public ModelAndView handleExcept4Support(ExceptInfoUser exception) {
        ModelAndView modelAndView = new ModelAndView("upload");
        modelAndView.addObject("errorMessage", exception.getMessage());
        return modelAndView;
    }
}
