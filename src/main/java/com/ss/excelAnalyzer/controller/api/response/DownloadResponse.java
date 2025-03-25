package com.ss.excelAnalyzer.controller.api.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DownloadResponse {
    String fileName;
    byte[] fileContent;
}
