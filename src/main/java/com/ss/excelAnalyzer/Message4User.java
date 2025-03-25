/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer;

import com.ss.Message4User_I;

/**
 *
 * @author vlitenko
 */
class Message4User implements Message4User_I
{
    private String message;

    public Message4User(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
    
    
}
