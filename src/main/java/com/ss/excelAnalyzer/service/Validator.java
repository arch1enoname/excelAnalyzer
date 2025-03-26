package com.ss.excelAnalyzer.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class Validator {

    private final String[] ALLOWED_EXTENSION = new String[]{".xlsx", ".xls"};
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public boolean validateFio(String fio) {
        if (fio.isBlank() || fio.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validateParseBirthDate(String date) {

        try {
            LocalDate birth = LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean validateBirthDateIsPast(String date) {
        LocalDate birth = LocalDate.parse(date, formatter);
        if (birth.isAfter(LocalDate.now())) {
            return false;
        }
        return true;
    }

    public boolean validateExtension(String fileName) {
        if (fileName.isBlank() || fileName.isEmpty()) {
            return false;
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();


        for (String allowedExtension : ALLOWED_EXTENSION) {
            if (fileExtension.equalsIgnoreCase(allowedExtension)) {
                return true;
            }
        }
        return false;
    }
}
