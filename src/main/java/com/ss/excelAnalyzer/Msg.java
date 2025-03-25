/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.excelAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import vl.utils.MapList;
import vl.utils.MapMap;

/**
 *
 * @author vlitenko
 */
public class Msg {

    public static final String US_en = "US_en";
    public static final String RU_ru = "RU_ru";

    public static final String CODE_FILE_NOT_VALID = "CODE_FILE_NOT_VALID";
    private static final String RU_MSG_FILE_NOT_VALID = "Расширение файла не корректное. ";

    public static final String CODE_FILE_NOT_EXIST = "CODE_FILE_NOT_EXIST";
    private static final String RU_MSG_CODE_FILE_NOT_EXIST = "Файл с таким id не существует. ";

    private static final HashMap<String, Msg> map = new HashMap<>();
    private static final MapMap<String, String, String> msg = new MapMap<>();   // locale, code, translation
    private static final MapList<String, String> months = new MapList<>();   // locale, List of month

    static {
        map.put(RU_ru, new Msg(RU_ru));

        HashMap<String, String> mMess;

        mMess = msg.getOrCreate(RU_ru);
        mMess.put(CODE_FILE_NOT_VALID, RU_MSG_FILE_NOT_VALID);
        mMess.put(CODE_FILE_NOT_EXIST, RU_MSG_CODE_FILE_NOT_EXIST);


        ArrayList<String> aMonth = months.getOrCreateList(RU_ru);
        aMonth.addAll(Arrays.asList("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"));
    }

    private static Msg instance = map.get(RU_ru);
    private String locale;

    public Msg(String locale) {
        this.locale = locale;
    }

    public static Msg i() {
        return instance;
    }

    public static void changeLocale(String locale) {
        instance = map.get(locale);
        instance.locale = locale;
    }

    public Message4User getMessage(String messageCode) {
        String sRes = msg.get(locale, messageCode);
        return (sRes != null) ? new Message4User(sRes) : new Message4User(messageCode);
    }
    public Message4User getMessageFileNotExist(String method) {
        return new Message4User(String
                .format(getMessage(CODE_FILE_NOT_EXIST).toString(),
                        method));
    }

    public Message4User getMessageFileNotValid(String method) {
        return new Message4User(String
                .format(getMessage(CODE_FILE_NOT_VALID).toString(),
                        method));
    }


}
