/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 *
 * @author vlitenko
 */
public interface IComId 
{
    Long getId();
    void setId(Long id);

    LocalDateTime getCreated();
    void setCreated(LocalDateTime created);

    long getCax();
    void setCax(long cax);

    BigInteger getEax();
    void setEax(BigInteger eax);

    String getTechComm();
    void setTechComm(String techComm);

    String getState();
    void setState(String state);

    LocalDateTime getStateDate();
    void setStateDate(LocalDateTime stateDate);
    
}
