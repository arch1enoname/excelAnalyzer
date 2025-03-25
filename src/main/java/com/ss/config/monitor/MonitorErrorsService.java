/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.config.monitor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author marina
 */
@Service
public class MonitorErrorsService {
    
    private final ArrayList<String> errors = new ArrayList<>();
    private final int capacity = 5;
    private final LocalDateTime serverStarted = LocalDateTime.now();
    private LocalDateTime lastMonitor = LocalDateTime.now();
    private LocalDateTime lastFix = LocalDateTime.now();

    public LocalDateTime getServerStarted() {
        return serverStarted;
    }

    public LocalDateTime getLastMonitor() {
        return lastMonitor;
    }

    public LocalDateTime getLastFix() {
        return lastFix;
    }
    
     
    public synchronized void addError(String error) {
        if (errors.size() >= capacity) {
            return;
        }
        errors.add(error);
    }

    public synchronized List<String> readAndUpdate() {
        lastMonitor = LocalDateTime.now(); 
        List<String> copy = new LinkedList<>(errors);
        return copy;
    }
    
    public synchronized void clean() {
        lastFix = LocalDateTime.now(); 
        errors.clear();
    }

    public synchronized List<String> read() {
        return new LinkedList<>(errors);
    }
    
}
