package com.ss.excelAnalyzer.service;

import com.ss.Except4Support;
import com.ss.ExceptInfoUser;
import com.ss.excelAnalyzer.Msg;
import com.ss.excelAnalyzer.conf.js.ConfJsAppTopt;
import com.ss.excelAnalyzer.conf.js.ConfJsTopt;
import com.ss.excelAnalyzer.controller.api.DownloadResponseDto;
import com.ss.excelAnalyzer.enums.CellStatus;
import com.ss.excelAnalyzer.enums.ThreadStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ss.excelAnalyzer.Msg.CODE_FILE_NOT_EXIST;
import static com.ss.excelAnalyzer.Msg.CODE_FILE_NOT_VALID;

@Service
@Slf4j
public class AnalyzerService {

    private final String ERR_CODE_001 = "ERR_CODE_001";
    private final String ERR_CODE_002 = "ERR_CODE_002";
    private final String ERR_CODE_003 = "ERR_CODE_003";
    private final String ERR_CODE_004 = "ERR_CODE_004";
    private final String ERR_CODE_005 = "ERR_CODE_005";
    private final String ERR_CODE_006 = "ERR_CODE_006";

    ConfJsAppTopt config = ConfJsTopt.getInstance().getApp();

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

    private final ConcurrentHashMap<String, String> taskStatus = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public AnalyzerService(Validator validator) {
        this.validator = validator;
    }

    public String analyzeFile(MultipartFile multipartFile) throws ExceptInfoUser {
        String fileId = UUID.randomUUID().toString();

        if(validator.validateExtension(multipartFile)) {
            throw new ExceptInfoUser(Msg.i().getMessage(CODE_FILE_NOT_VALID));
        }

        try {
            saveFile(multipartFile.getBytes(), fileId);

            // Передаем задачу в ExecutorService
            executorService.submit(() -> {
                try {
                    generateExcel(parseExcel(multipartFile), fileId);
                } catch (ExceptInfoUser e) {
                    throw new Except4Support(ERR_CODE_005, "Ошибка в чтении файла. " + fileId);
                }
            });

            executorService.shutdown();
        } catch (IOException e) {
            throw new Except4Support(ERR_CODE_001, "Could not get bytes from multipart file. " + multipartFile);
        }

        return fileId;
    }

    private List<List<String>> parseExcel(MultipartFile file) throws ExceptInfoUser {

        List<List<String>> dataList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Пропускаем заголовок
                }

                Cell fioCell = row.getCell(0);
                Cell dateCell = row.getCell(1);

                if (fioCell != null && dateCell != null) {
                    List<String> rowData = new ArrayList<>();

                    String fio = fioCell.getStringCellValue();
                    String date = dateCell.toString();

                    rowData.add(fio);
                    rowData.add(date);

                    dataList.add(rowData);
                }
            }

        } catch (IOException e) {
            throw new Except4Support(ERR_CODE_002, "Could not get input stream from multipart file. " + file);
        }
        return dataList;
    }

    public void saveFile(byte[] data, String fileName) {
        try (FileOutputStream outputStream = new FileOutputStream(BASE_DIRECTORY+"/"+ fileName + XLSX_FILE_TYPE)) {
            outputStream.write(data);
        } catch (IOException e) {
            throw new Except4Support(ERR_CODE_003, "Could not save file. " + fileName + " data: " + data.length);
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
                throw new Except4Support(ERR_CODE_004, "Could write output byte array to workbook. ");
            }
            taskStatus.put(fileId, ThreadStatus.COMPLETED.toString());
        } catch (IOException e) {
            throw new Except4Support(ERR_CODE_004, "Can`t create new XSSFWorkbook. ");
        }
    }

    public DownloadResponseDto getByteArrayByFileId(String fileId) throws ExceptInfoUser{
        String filePath = BASE_DIRECTORY + "\\" + fileId + ANALYZED_MARKER + XLSX_FILE_TYPE;

        File file = new File(filePath);

        if (!file.exists()) {
            throw new ExceptInfoUser(Msg.i().getMessage(CODE_FILE_NOT_EXIST));
        }

        try {

            return DownloadResponseDto.builder()
                    .data(Files.readAllBytes(file.toPath()))
                    .fileName(fileId + ANALYZED_MARKER + XLSX_FILE_TYPE)
                    .build();
        } catch (IOException e) {
            throw new Except4Support(ERR_CODE_005, "Ошибка в чтении файла. " + fileId);
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
            row.createCell(FIO_COLUMN).setCellValue("123");
            errorDetails.append("Error in FIO. ФИО не должно быть пустым \n");
            valid = false;
        }

        if (validator.validateBirthDate(birthDate)) {
            row.createCell(YEAR_COLUMN).setCellValue(calculateAgeYears(birthDate));
            row.createCell(MONTH_COLUMN).setCellValue(calculateAgeMonth(birthDate));
        } else {
            errorDetails.append("Error in birthDate. Неверный формат, дата рождения должна быть формата dd.mm.yyyy \n");
            valid = false;
        }

        if (valid) {
            row.createCell(STATUS_COLUMN).setCellValue(CellStatus.OK.toString());
        } else  {
            row.createCell(STATUS_COLUMN).setCellValue(CellStatus.ERROR.toString());
            row.createCell(ERROR_COLUMN).setCellValue(errorDetails.toString());
        }
    }

    private int calculateAgeYears(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalDate birth = LocalDate.parse(birthDate, formatter);

            LocalDate now = LocalDate.now();
            return Period.between(birth, now).getYears();
        } catch (DateTimeParseException e) {
            throw new Except4Support(ERR_CODE_006, "Error in parse date. " + birthDate);
        }
    }

    private int calculateAgeMonth(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalDate birth = LocalDate.parse(birthDate, formatter);

            LocalDate now = LocalDate.now();
            return Period.between(birth, now).getMonths();
        } catch (DateTimeParseException e) {
            throw new Except4Support(ERR_CODE_006, "Error in parse date. " + birthDate);
        }
    }

    public String getStatusByFileId(String fileId) {
        return taskStatus.get(fileId);
    }



}
