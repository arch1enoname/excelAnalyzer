package com.ss.config.monitor;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MonitorErrorsDto {
    private String serverStarted;
    private String lastMonitor;
    private String healthState;
    private ArrayList<String> last5 = new ArrayList<>();
    private String lastFix;

    public MonitorErrorsDto(String lastFix, String healthState, String lastMonitor, String serverStarted) {
        this.lastFix = lastFix;
        this.healthState = healthState;
        this.lastMonitor = lastMonitor;
        this.serverStarted = serverStarted;
    }

    public String getServerStarted() {
        return serverStarted;
    }

    public String getLastMonitor() {
        return lastMonitor;
    }

    public String getHealthState() {
        return healthState;
    }

    public ArrayList<String> getLast5() {
        return last5;
    }

    public String getLastFix() {
        return lastFix;
    }

    public void addError(String error) {
        last5.add(error);
    }


}
