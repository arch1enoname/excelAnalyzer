package com.ss.excelAnalyzer;

import com.ss.ExceptInfoUser;
import com.ss.Message4User_I;

import java.util.Map;

public class FileNameNotValidException extends ExceptInfoUser {
    public FileNameNotValidException(Message4User_I message) {
        super(message);
    }

    public FileNameNotValidException(Message4User_I message, String message4support) {
        super(message, message4support);
    }

    public FileNameNotValidException(Map<String, String> errors) {
        super(errors);
    }
}
