package com.ss.excelAnalyzer.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.ss.Except4Support;
import com.ss.ExceptInfoUser;
import com.ss.excelAnalyzer.Msg;
import com.ss.excelAnalyzer.conf.js.ConfJsAppExcelAnalyzer;
import com.ss.excelAnalyzer.conf.js.ConfJsExcelAnalyzer;
import com.ss.excelAnalyzer.controller.api.DownloadResponseDto;
import com.ss.excelAnalyzer.dtos.AnalyzedExcelRowDto;
import com.ss.excelAnalyzer.enums.CellStatus;
import com.ss.excelAnalyzer.enums.ThreadStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AnalyzerService {

    ConfJsAppExcelAnalyzer config = ConfJsExcelAnalyzer.getInstance().getApp();

    private final String BASE_DIRECTORY = config.getBaseDirectory();
    private final int THREAD_COUNT = config.getThreadCount();
    private final Validator validator;

    private final int FIO_COLUMN = 0;
    private final int BIRTHDATE_COLUMN = 1;
    private final int YEAR_COLUMN = 2;
    private final int MONTH_COLUMN = 3;
    private final int STATUS_COLUMN = 4;
    private final int ERROR_COLUMN = 5;
    private final String XLSX_FILE_TYPE = ".xlsx";
    private final String ANALYZED_MARKER = "-analyzed";
    private final String EMPTY_STRING_VALUE = "";

    private final ConcurrentHashMap<String, String> taskStatus = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final TemplateEngine templateEngine;

    @Autowired
    public AnalyzerService(Validator validator, TemplateEngine templateEngine) {
        this.validator = validator;
        this.templateEngine = templateEngine;
    }

    public String analyzeFile(InputStream inputStream, String fileName) throws ExceptInfoUser, IOException {

        String fileId = UUID.randomUUID().toString();
        try {
            byte[] data = inputStream.readAllBytes();

            if(validator.validateExtension(fileName)) {
                throw new ExceptInfoUser(Msg.i().getMessage(Msg.CODE_FILE_NOT_VALID));
            }

            saveFile(data, fileId);

            // Передаем задачу в ExecutorService
            executorService.submit(() -> {
                try {
                    generateExcel(parseOriginalExcel(new ByteArrayInputStream(data)), fileId);
                } catch (ExceptInfoUser e) {
                    throw new Except4Support("ERR_CODE_001", "Ошибка в чтении файла. " + e);
                }
            });
        } catch (IOException e) {
            throw new Except4Support("ERR_CODE_002", "Ошибка в чтении файла. " + e);
        }

        executorService.shutdown();

        return fileId;
    }

    public List<AnalyzedExcelRowDto> parseAnalyzedExcel(String fileId) throws ExceptInfoUser {

        File file = new File(BASE_DIRECTORY + "\\" + fileId + ANALYZED_MARKER + XLSX_FILE_TYPE);
        List<AnalyzedExcelRowDto> dataList = new ArrayList<>();
        try{
            byte[] data = Files.readAllBytes(file.toPath());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

            try (Workbook workbook = new XSSFWorkbook(byteArrayInputStream)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue; // Пропускаем заголовок
                    }

                    Cell fioCell = row.getCell(FIO_COLUMN);
                    Cell dateCell = row.getCell(BIRTHDATE_COLUMN);
                    Cell years = row.getCell(YEAR_COLUMN);
                    Cell months = row.getCell(MONTH_COLUMN);
                    Cell status = row.getCell(STATUS_COLUMN);
                    Cell errorDetails = row.getCell(ERROR_COLUMN);

                    dataList.add(AnalyzedExcelRowDto.builder()
                            .fio(fioCell.getStringCellValue())
                            .birthDate(dateCell.getStringCellValue())
                            .years(years.getStringCellValue())
                            .months(months.getStringCellValue())
                            .status(status.getStringCellValue())
                            .errorDetails(errorDetails.getStringCellValue())
                            .build());
                }

            } catch (IOException e) {
                throw new Except4Support("ERR_CODE_003", "Could not get input stream from multipart file. " + e);
            }
        } catch (IOException e) {
            throw new Except4Support("ERR_CODE_004", "Ошибка в преобразовании файла в массив байтов", e);
        }

        return dataList;
    }

    public List<List<String>> parseOriginalExcel(ByteArrayInputStream inputStream) throws ExceptInfoUser {

        List<List<String>> dataList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Пропускаем заголовок
                }

                Cell fioCell = row.getCell(FIO_COLUMN);
                Cell dateCell = row.getCell(BIRTHDATE_COLUMN);

                List<String> rowData = new ArrayList<>();

                String fio = (fioCell != null) ? fioCell.getStringCellValue() : EMPTY_STRING_VALUE;
                rowData.add(fio);

                String date = (dateCell != null) ? dateCell.toString() : EMPTY_STRING_VALUE;
                rowData.add(date);

                dataList.add(rowData);
            }

        } catch (IOException e) {
            throw new Except4Support("ERR_CODE_005", "Could not get input stream from multipart file. " + e);
        }

        return dataList;
    }

    public void saveFile(byte[] data, String fileName) {
        try (FileOutputStream outputStream = new FileOutputStream(BASE_DIRECTORY+"/"+ fileName + XLSX_FILE_TYPE)) {
            outputStream.write(data);
        } catch (IOException e) {
            throw new Except4Support("ERR_CODE_006", "Could not save file. " + fileName + " data: " + data.length);
        }
    }

    public String htmlToPdf(String html) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            DefaultFontProvider defaultFontProvider = new DefaultFontProvider(false, true, false);
            ConverterProperties converterProperties = new ConverterProperties();

            converterProperties.setFontProvider(defaultFontProvider);

            HtmlConverter.convertToPdf(html, pdfWriter, converterProperties);

            FileOutputStream fileOutputStream = new FileOutputStream("");
            byteArrayOutputStream.writeTo(fileOutputStream);
            byteArrayOutputStream.close();

            byteArrayOutputStream.flush();
            fileOutputStream.close();
            return null;


        } catch (FileNotFoundException e) {
            throw new Except4Support("ERR_CODE_013", "Невозможно найти файл", e);
        } catch (IOException e) {
            throw new Except4Support("ERR_CODE_013", "Ошибка при получении массива байтов", e);
        }
    }

    public void generateExcel(List<List<String>> fileData, String fileId){
        taskStatus.put(fileId, ThreadStatus.IN_PROGRESS.toString());

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Age Calculation");
            Row headerRow = sheet.createRow(0); //Заголовки таблицы

            //Создание заголовков для таблицы
            headerRow.createCell(FIO_COLUMN).setCellValue("ФИО");
            headerRow.createCell(BIRTHDATE_COLUMN).setCellValue("Дата рождения");
            headerRow.createCell(YEAR_COLUMN).setCellValue("Возраст в годах");
            headerRow.createCell(MONTH_COLUMN).setCellValue("Возраст в месяцах");
            headerRow.createCell(STATUS_COLUMN).setCellValue("Статус");
            headerRow.createCell(ERROR_COLUMN).setCellValue("Детализация ошибки");

            int rowNum = 1; // вторая строчка таблицы
            for (List<String> data : fileData) {
                Row row = sheet.createRow(rowNum++);
                createRow(data, row);
            }

            try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                saveFile(out.toByteArray(), fileId + ANALYZED_MARKER);
            } catch (IOException e) {
                throw new Except4Support("ERR_CODE_008", "Could write output byte array to workbook. " + e);
            }

            taskStatus.put(fileId, ThreadStatus.COMPLETED.toString());
        } catch (IOException e) {
            throw new Except4Support("ERR_CODE_009", "Can`t create new XSSFWorkbook. " + e);
        }
    }

    public DownloadResponseDto getByteArrayByFileId(String fileId) throws ExceptInfoUser{
        String filePath = BASE_DIRECTORY + "\\" + fileId + ANALYZED_MARKER + XLSX_FILE_TYPE;

        File file = new File(filePath);

        if (!file.exists()) {
            throw new ExceptInfoUser(Msg.i().getMessage(Msg.CODE_FILE_NOT_EXIST));
        }

        try {
            return DownloadResponseDto.builder()
                    .data(Files.readAllBytes(file.toPath()))
                    .fileName(fileId + ANALYZED_MARKER + XLSX_FILE_TYPE)
                    .build();
        } catch (IOException e) {
            throw new Except4Support("ERR_CODE_010", "Ошибка в чтении файла. " + fileId);
        }
    }

    private void createRow(List<String> data, Row row) {

        boolean valid = true;
        StringBuilder errorDetails = new StringBuilder();

        String fio = data.get(FIO_COLUMN);
        String birthDate = data.get(BIRTHDATE_COLUMN);

        row.createCell(FIO_COLUMN).setCellValue(fio);
        row.createCell(BIRTHDATE_COLUMN).setCellValue(birthDate);

        if (!validator.validateFio(fio)) {
            errorDetails.append("Error in fio. ФИО не должно быть пустым\n");
            valid = false;
        }

        if (validator.validateParseBirthDate(birthDate)) {
            setYearAndMonth(row, birthDate);
            if (validator.validateBirthDateIsPast(birthDate)) {
                setYearAndMonth(row, birthDate);
            } else {
                errorDetails.append("Error in birthDate. Дата рождения должна быть раньше нынешнего \n");
                valid = false;
            }
        } else {
            errorDetails.append("Error in birthDate. Неверный формат, дата рождения должна быть формата dd.mm.yyyy \n");
            valid = false;
        }

        if (valid) {
            row.createCell(STATUS_COLUMN).setCellValue(CellStatus.OK.toString());
            row.createCell(ERROR_COLUMN).setCellValue(EMPTY_STRING_VALUE);
        } else  {
            row.createCell(YEAR_COLUMN).setCellValue(EMPTY_STRING_VALUE);
            row.createCell(MONTH_COLUMN).setCellValue(EMPTY_STRING_VALUE);
            row.createCell(STATUS_COLUMN).setCellValue(CellStatus.ERROR.toString());
            row.createCell(ERROR_COLUMN).setCellValue(errorDetails.toString());
        }

    }

    private void setYearAndMonth(Row row, String birthDate) {
        row.createCell(YEAR_COLUMN).setCellValue(calculateAgeYears(birthDate)+"");
        row.createCell(MONTH_COLUMN).setCellValue(calculateAgeMonth(birthDate)+"");
    }

    private int calculateAgeYears(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalDate birth = LocalDate.parse(birthDate, formatter);

            LocalDate now = LocalDate.now();
            return Period.between(birth, now).getYears();
        } catch (DateTimeParseException e) {
            throw new Except4Support("ERR_CODE_011", "Error in parse date. " + birthDate);
        }
    }

    private int calculateAgeMonth(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalDate birth = LocalDate.parse(birthDate, formatter);
            LocalDate now = LocalDate.now();

            return Period.between(birth, now).getMonths();
        } catch (DateTimeParseException e) {
            throw new Except4Support("ERR_CODE_012", "Error in parse date. " + birthDate);
        }
    }

    public String getStatusByFileId(String fileId) {
        return taskStatus.get(fileId);
    }
}
