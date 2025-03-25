package com.ss.excelAnalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ss")
public class ExcelAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelAnalyzerApplication.class, args);
    }
}
