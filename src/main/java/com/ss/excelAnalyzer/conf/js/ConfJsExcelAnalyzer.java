/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.conf.js;

import com.ss.config.js.ExceptConf;
import com.ss.config.js.ConfJs;
import com.ss.config.js.ExceptCJsNoObject;
import com.ss.config.js.ExceptCJsUnsupported;
import java.io.FileNotFoundException;

/**
 *
 * @author vlitenko
 */
public class ConfJsExcelAnalyzer extends ConfJs {

    public static final String APP_NAME = "excelAnalyzer_server";
    private static final ConfJsExcelAnalyzer instance = new ConfJsExcelAnalyzer();
    private static final String CONF_FILE_NMAE = "conf_excelAnalyzer_serv.json";

    private ConfJsExcelAnalyzer() {
        super(APP_NAME, ConfJsAppFactoryExcelAnalyzer.getInstance());
        try {
            load(CONF_FILE_NMAE, "../" + CONF_FILE_NMAE);
        } catch (FileNotFoundException ex) {
            throw new ExceptConf("ErrConf1", "Can't load project configuration", "Can't find configuration file " + CONF_FILE_NMAE, ex);
        } catch (ExceptCJsUnsupported ex) {
            throw new ExceptConf("ErrConf2", "Can't process project configuration", "Cant't parse configuration file " + CONF_FILE_NMAE, ex);
        }
    }

    public void updateConf() {
        try {
            load(CONF_FILE_NMAE, "../" + CONF_FILE_NMAE);
        } catch (FileNotFoundException ex) {
            throw new ExceptConf("ErrConf1", "Can't load project configuration", "Can't find configuration file " + CONF_FILE_NMAE, ex);
        } catch (ExceptCJsUnsupported ex) {
            throw new ExceptConf("ErrConf2", "Can't process project configuration", "Cant't parse configuration file " + CONF_FILE_NMAE, ex);
        }
    }

    public static ConfJsExcelAnalyzer getInstance() {
        return instance;
    }

    public ConfJsAppExcelAnalyzer getApp() {
        try {
            return (ConfJsAppExcelAnalyzer) super.getApp(APP_NAME);
        } catch (ExceptCJsNoObject ex) {
            throw new ExceptConf("ErrConf3", "Can't process project configuration",
                     String.format("Cant't get app %s in file %s", APP_NAME, CONF_FILE_NMAE), ex);
        }
    }

}
