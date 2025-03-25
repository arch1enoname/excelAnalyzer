/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.config.monitor;



import static com.ss.config.monitor.MonitorErrorsContr.URL_MONITOR_ERRORS;
import com.ss.excelAnalyzer.controller.Contr;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vl.utils.FormatsDate;

/**
 *
 * @author marina
 */
@RequestMapping(URL_MONITOR_ERRORS)
@RestController
public class MonitorErrorsContr {

    public static final String ROUTE_MONITOR = "m/";
    public static final String URL_MONITOR_ERRORS = Contr.ROUTE_PUB + MonitorErrorsContr.ROUTE_MONITOR;

    public static final String SUB_URL_HEALTH = "health";
    public static final String SUB_URL_MONITOR = "monitor";
    public static final String SUB_URL_HEALTH_JSON = SUB_URL_HEALTH + "/json";
    public static final String SUB_URL_MONITOR_JSON = SUB_URL_MONITOR + "/json";
    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    public static final String FIELD_SERVER_STARTED = "server_started";
    public static final String FIELD_LAST_MONITOR = "last_monitor";
    public static final String FIELD_HEALTH_STATE = "health_state";
    public static final String FIELD_LAST5_ERRORS = "last5";
    public static final String FIELD_LAST_FIX = "last_fix";

    private final MonitorErrorsService monitorErrorsService;

    public MonitorErrorsContr(MonitorErrorsService serviceHealth) {
        this.monitorErrorsService = serviceHealth;
    }

    @GetMapping(SUB_URL_MONITOR)
    public String monitorText(HttpServletRequest request) {
        List<String> errors = monitorErrorsService.readAndUpdate();
        return formatTextResponse(errors);
    }

    @GetMapping(SUB_URL_MONITOR_JSON)
    public ResponseEntity<?> monitorJson(HttpServletRequest request) {
        List<String> errors = monitorErrorsService.readAndUpdate();
        return ResponseEntity.ok().body(formatJsonResponse(errors));
    }

    @GetMapping(SUB_URL_HEALTH)
    public String healthText(HttpServletRequest request) {
        List<String> errors = monitorErrorsService.read();
        return formatTextResponse(errors);
    }

    @GetMapping(SUB_URL_HEALTH_JSON)
    public ResponseEntity<?> healthJson(HttpServletRequest request) {
        List<String> errors = monitorErrorsService.read();
        return ResponseEntity.ok().body(formatJsonResponse(errors));
    }

    private String formatTextResponse(List<String> errors) {
        StringJoiner sj = new StringJoiner("<br />");
        sj.add(FIELD_SERVER_STARTED + ": " + monitorErrorsService.getServerStarted().format(FormatsDate.DTF_DOT_DATE_TIME));
        sj.add(FIELD_LAST_MONITOR + ": " + monitorErrorsService.getLastMonitor().format(FormatsDate.DTF_DOT_DATE_TIME));
        sj.add(FIELD_HEALTH_STATE + ": " + (errors.isEmpty() ? STATUS_OK : STATUS_ERROR));
        sj.add(FIELD_LAST_FIX + ": " + monitorErrorsService.getLastFix().format(FormatsDate.DTF_DOT_DATE_TIME));
        sj.add(FIELD_LAST5_ERRORS + ":");
        int count = 1;
        for (String error : errors) {
            sj.add((count++) + ". " + error);
        }
        String response = sj.toString();
        return response;
    }

    private Map<String, Object> formatJsonResponse(List<String> errors) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(FIELD_SERVER_STARTED, monitorErrorsService.getServerStarted().format(FormatsDate.DTF_DOT_DATE_TIME));
        response.put(FIELD_LAST_MONITOR, monitorErrorsService.getLastMonitor().format(FormatsDate.DTF_DOT_DATE_TIME));
        response.put(FIELD_HEALTH_STATE, errors.isEmpty() ? STATUS_OK : STATUS_ERROR);
        response.put(FIELD_LAST_FIX, monitorErrorsService.getLastFix().format(FormatsDate.DTF_DOT_DATE_TIME));
        response.put(FIELD_LAST5_ERRORS, errors);

        return response;
    }
}
