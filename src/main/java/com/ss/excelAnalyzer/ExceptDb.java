/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer;

import com.ss.Except4Support;

/**
 *
 * @author vlitenko
 */
public class ExceptDb extends Except4Support
{
    private static final String MEG_DB_ERROR = "Ошибка базы данных";
    private String host;
    private String username;

    private ExceptDb(String errorCode, String message, String extendedMessage, Throwable cause) {
        super(errorCode, message, extendedMessage, cause);
        host = System.getenv("topt_Db_url"); //getProperty() for server. getenv() for local.
        username = System.getenv("topt_Db_user"); //getProperty() for server. getenv() for local.
    }
    public ExceptDb(String errorCode, String extendedMessage, Throwable cause) {
        this(errorCode, MEG_DB_ERROR, extendedMessage, cause);
    }

//    public ExceptDb(String errorCode, String message, String extendedMessage) {
//        super(errorCode, message, extendedMessage);
//    }

    @Override
    public String getMessage4Support() 
    {
        return super.getMessage4Support() + String.format(" Host: %s username: %s", host, username);
    }
}
