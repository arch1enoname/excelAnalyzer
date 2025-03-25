/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.config.sequence;

/**
 *
 * @author Marina
 */
public class SequenceCache {

    public static final String STATE_NEW = "new";
    public static final String STATE_LOADED = "loaded";

    private String entityName;
    private String sequenceName;
    private String state = STATE_NEW;
    private long valueMin = 0;
    private int step;
    private long valueMax = 0;
    private long valueCurrent = 0;

    public SequenceCache(String entityName, String sequenceName, int step) {
        this.entityName = entityName;
        this.sequenceName = sequenceName;
        this.step = step;
    }

    public long nextVal() throws Exception {

        if (!state.equals(STATE_LOADED) || valueCurrent == valueMax) {
            throw new Exception();
        }
        if (valueCurrent % 50 == 0) {
            int ii = 0;
        }
        return valueCurrent++;
    }

    public long setNewVal(long p_lVal) {
        state = STATE_LOADED;
        valueMin = p_lVal;
        valueMax = valueMin + step;
        valueCurrent = p_lVal;

        return valueCurrent++;
    }

    public String getSequenceName() {
        return sequenceName;
    }
}

