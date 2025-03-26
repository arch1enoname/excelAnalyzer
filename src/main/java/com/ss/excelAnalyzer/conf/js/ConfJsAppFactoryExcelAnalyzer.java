/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.conf.js;

import com.ss.config.js.ConfJsApp;
import com.ss.config.js.ConfJsAppFactory_I;
import com.ss.config.js.ConfJsDbFactory_I;
import java.util.HashMap;

/**
 *
 * @author vlitenko
 */
public class ConfJsAppFactoryExcelAnalyzer implements ConfJsAppFactory_I
{
    private static final ConfJsAppFactoryExcelAnalyzer instance = new ConfJsAppFactoryExcelAnalyzer();

    public static ConfJsAppFactoryExcelAnalyzer getInstance() {
        return instance;
    }
    
    @Override
    public ConfJsApp newObj(HashMap<String, ConfJsDbFactory_I> factoriesDb) {
        return new ConfJsAppExcelAnalyzer();
    }
    
}
