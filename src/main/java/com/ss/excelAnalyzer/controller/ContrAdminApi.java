/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.controller;

import com.ss.excelAnalyzer.conf.js.ConfJsExcelAnalyzer;

import com.ss.config.monitor.MonitorErrorsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author marina
 */
@RequestMapping(Contr.ROUTE_INTERNAL_ADMIN)
@RestController
public class ContrAdminApi extends Contr {

    public static final String URL_CONFIG = "/config";
    public static final String URL_ERROR = "/error";


    private final MonitorErrorsService serviceMonitorErrors;

    public ContrAdminApi(MonitorErrorsService serviceHealth) {
        this.serviceMonitorErrors = serviceHealth;
    }


    @DeleteMapping(URL_ERROR)
    public ResponseEntity<?> clearErrors() {
        serviceMonitorErrors.clean();

        return ResponseEntity.ok().build();
    }

    @GetMapping(URL_CONFIG)
    public ResponseEntity<?> getConfig() {
        return ResponseEntity.ok(ConfJsExcelAnalyzer.getInstance().getApp());
    }



}
