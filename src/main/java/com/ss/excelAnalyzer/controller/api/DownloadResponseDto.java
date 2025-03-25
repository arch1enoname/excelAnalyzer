package com.ss.excelAnalyzer.controller.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DownloadResponseDto {
    String fileName;
    byte[] data;
}
