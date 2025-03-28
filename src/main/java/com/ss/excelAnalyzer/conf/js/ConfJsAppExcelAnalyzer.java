/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.conf.js;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ss.config.js.ExceptConf;
import com.fasterxml.jackson.databind.JsonNode;
import com.ss.config.js.ConfJsApp;
import com.ss.config.js.ConfJsDb;

/**
 * @author vlitenko
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConfJsAppExcelAnalyzer extends ConfJsApp {

    private String nameServer;
    private String urlBase;
    private String serverType;
    private int hikariPoolMaxSize;
    private String domain;
    private String fileDirectory;
    private String fontsDirectory;
    private String resourcesDirectory;

    private int threadCount;
    private String baseDirectory;


    public static final String SERVER_TYPE_DEV = "dev";
    public static final String SERVER_TYPE_TEST = "test";
    public static final String SERVER_TYPE_PREPROD = "preprod";
    public static final String SERVER_TYPE_PROD = "prod";

    public ConfJsAppExcelAnalyzer() {
        super(ConfJsDb.knownDb);
    }

    public ConfJsAppExcelAnalyzer(ConfJsApp p_kCopy) {
        super(p_kCopy);
    }

    @Override
    protected void initApp(JsonNode p_xParser) throws ExceptConf {
        try {

            // TECHNICAL
            nameServer = getStringRequired(p_xParser, "name");
            urlBase = getStringRequired(p_xParser, "url_base");
            serverType = getStringRequired(p_xParser, "server_type");
            domain = getStringRequired(p_xParser, "domain");
            hikariPoolMaxSize = getIntRequired(p_xParser, "hikari_pool_max_size");
            baseDirectory =  getStringRequired(p_xParser, "baseDirectory");
            threadCount = getIntRequired(p_xParser, "threadCount");
            fileDirectory = getStringRequired(p_xParser,"fileDirectory");
            fontsDirectory = getStringRequired(p_xParser, "fontsDirectory");
            resourcesDirectory = getStringRequired(p_xParser, "resourcesDirectory");

        } catch (RuntimeException ex) {
            throw new ExceptConf("ErrConfA1", "Can't process project configuration",
                    ex.getMessage(), ex);
        }
    }

    public String getNameServer() {
        return nameServer;
    }

    public String getUrlBase() {
        return urlBase;
    }


    public String getServerType() {
        return serverType;
    }

    public String getDomain() {
        return domain;
    }

    public int getHikariPoolMaxSize() {
        return hikariPoolMaxSize;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public String getResourcesDirectory() {
        return resourcesDirectory;
    }

    public String getFontsDirectory() {
        return fontsDirectory;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }
}
