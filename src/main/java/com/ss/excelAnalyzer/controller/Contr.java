/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer.controller;

import jakarta.servlet.http.HttpServletRequest;

public abstract class Contr {
    public static final String ROUTE_BASE = "/";
    public static final String PARAMETER_URL = "url";
    public static final String REQ_ACTION_ID = "ACTION-ID";
    public static final String DIR_PUB = "p";
    public static final String ROUTE_PUB = "/" + DIR_PUB + "/";
    public static final String ROUTE_ANONYM = "/n/";
    public static final String ROUTE_AUTHORIZED = "/a/";
    public static final String ROUTE_CUSTOMER_ADMIN = ROUTE_AUTHORIZED + "c/";
    public static final String ROUTE_INTERNAL = ROUTE_AUTHORIZED + "i/";
    public static final String ROUTE_INTERNAL_ADMIN = ROUTE_INTERNAL + "a/";
    public static final String PARAMETER_ERROR_MESSAGE = "errorMessage";
    public static final String PARAMETER_STATUS_CODE = "statusCode";

    /**
     *
     * @param req
     * @return actionId from Http request
     */
    public static Long getActionIdFromReq(HttpServletRequest req) {
        String actionIdFromRequest = req.getHeader(Contr.REQ_ACTION_ID) != null
                ? req.getHeader(Contr.REQ_ACTION_ID) : (String)req.getAttribute(Contr.REQ_ACTION_ID);
        Long actionId = null;
        // если не нашли action ни в header, ни в аттрибутах, скорее
        // всего ошибка упала до создания action и проблемы тут нет
        try {
            if (actionIdFromRequest != null) {
                actionId = Long.valueOf(actionIdFromRequest);
            }
        } catch (NumberFormatException e) {
            //невалидный запрос, пропускаем
        }
        return actionId;
    }

}
