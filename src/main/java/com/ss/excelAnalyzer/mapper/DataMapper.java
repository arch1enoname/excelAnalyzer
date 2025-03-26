package com.ss.excelAnalyzer.mapper;

import com.ss.excelAnalyzer.dtos.AnalyzedExcelRowDto;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataMapper {

    public Context setData(List<AnalyzedExcelRowDto> analyzedExcelRowDtos) {

        Context context = new Context();

        Map<String, Object> data = new HashMap<>();
        data.put("dataList", analyzedExcelRowDtos);
        context.setVariables(data);

        return context;
    }
}
