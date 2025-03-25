package com.ss.excelAnalyzer.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class Validator {

    private final String[] ALLOWED_EXTENSION = new String[]{".xlsx", ".xls"};

    public boolean validateFio(String fio) {
        if (fio.isBlank() || fio.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validateBirthDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate birth = LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean validateExtension(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            return false;
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();


        for (String allowedExtension : ALLOWED_EXTENSION) {
            if (fileExtension.equalsIgnoreCase(allowedExtension)) {
                return true;
            }
        }
        return false;
    }
}
