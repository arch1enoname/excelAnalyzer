package com.ss.excelAnalyzer.dtos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AnalyzedExcelRowDto {
    String fio;
    String birthDate;
    String years;
    String months;
    String status;
    String errorDetails;
}
