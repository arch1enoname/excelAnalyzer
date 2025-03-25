package com.ss.excelAnalyzer.controller.api;

import com.ss.Except4Support;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
@ControllerAdvice(annotations = RestController.class)
public class HandlerExceptApi {
    Random random = new Random();

    private final static Logger logger = Logger.getLogger(HandlerExceptApi.class.getName());

    @ExceptionHandler(Except4Support.class)
    public ModelAndView handleExcept4Support(Except4Support exception) {
        long errorId = random.nextLong();
        ModelAndView modelAndView = new ModelAndView("upload");
        logger.info("ErrorId=" + errorId + "Описание" + exception.getMessage4Support());
        modelAndView.addObject("errorMessage", "Ошибка на стороне сервера id: " + errorId);
        return modelAndView;
    }

}
