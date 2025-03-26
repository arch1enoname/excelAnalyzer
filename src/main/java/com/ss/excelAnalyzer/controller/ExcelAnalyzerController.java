package com.ss.excelAnalyzer.controller;

import com.ss.Except4Support;
import com.ss.ExceptInfoUser;
import com.ss.excelAnalyzer.controller.api.DownloadResponseDto;
import com.ss.excelAnalyzer.service.AnalyzerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@Slf4j
@RequestMapping
public class ExcelAnalyzerController extends ControllerBase{



    private final String ROUTE_UPLOAD = ROUTE_BASE + "upload";
    private final String ROUTE_VIEW_HTML = ROUTE_BASE + "view/html/{fileId}";
    private final String ROUTE_DOWNLOAD_XLSX = ROUTE_BASE + "download/xlsx/{fileId}";
    private final String ROUTE_DOWNLOAD_PDF = ROUTE_BASE + "download/pdf/{fileId}";
    private final String ROUTE_STATUS = ROUTE_BASE + "status/{fileId}";
    private final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";

    @Autowired
    private AnalyzerService analyzerService;

    @GetMapping(ROUTE_BASE)
    public String showUploadPage() {
        return "upload";
    }

    @GetMapping(ROUTE_DOWNLOAD_XLSX)
    public ResponseEntity<?> downloadExcel(HttpServletRequest request, @PathVariable String fileId){
        try {
            DownloadResponseDto downloadResponseDto = analyzerService.getByteArrayByFileId(fileId);
            byte[] response = downloadResponseDto.getData();
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", downloadResponseDto.getFileName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response);

        } catch (ExceptInfoUser e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(ROUTE_UPLOAD)
    public String uploadExcel(Model model, @RequestParam("file") MultipartFile multipartFile){
        try {

            String fileId = analyzerService.analyzeFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
            model.addAttribute("fileId", fileId);

        } catch (ExceptInfoUser e) {
            model.addAttribute(ERROR_MESSAGE_ATTRIBUTE, e.getMessage());
            return "upload";
        } catch (IOException e) {
            model.addAttribute(ERROR_MESSAGE_ATTRIBUTE, "Ошибка на стороне сервера");
            throw new Except4Support("ERR010", "Ошибка при попытке получить inputStream", e);
        }

        return "upload";
    }

    @GetMapping(ROUTE_STATUS)
    public String status(Model model, @PathVariable String fileId){

        String status = analyzerService.getStatusByFileId(fileId);
        model.addAttribute("status", status);

        return "status";
    }

    @GetMapping(ROUTE_VIEW_HTML)
    public String showHtmlViewPage(Model model, @PathVariable String fileId) {
        try {
            model.addAttribute("dataList", analyzerService.parseAnalyzedExcel(fileId));
        } catch (ExceptInfoUser e) {
            model.addAttribute(ERROR_MESSAGE_ATTRIBUTE, e.getMessage());
            return "upload";
        }
        return "view-html";
    }

    @GetMapping(ROUTE_DOWNLOAD_PDF)
    public ResponseEntity<?> renderPage(@PathVariable String fileId){

        DownloadResponseDto downloadResponseDto = analyzerService.generatePdf(fileId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", downloadResponseDto.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(downloadResponseDto.getData());
    }

}
