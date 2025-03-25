package com.ss.excelAnalyzer.controller;


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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@Slf4j
@RequestMapping
public class ExcelAnalyzerController extends ControllerBase{
    private final String ROUTE_UPLOAD = ROUTE_BASE + "upload";
    private final String ROUTE_DOWNLOAD = ROUTE_BASE + "download/{fileId}";
    private final String ROUTE_STATUS = ROUTE_BASE + "status";
    private final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";

    @Autowired
    private AnalyzerService analyzerService;

    @GetMapping("/")
    public String showUploadPage() {
        return "upload";
    }

    @GetMapping(ROUTE_DOWNLOAD)
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
            //
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(ROUTE_UPLOAD)
    public String uploadExcel(Model model, @RequestParam("file") MultipartFile multipartFile,
                              ModelMap modelMap){
        try {

            String fileId = analyzerService.analyzeFile(multipartFile);
            model.addAttribute("fileId", fileId);

        } catch (ExceptInfoUser e) {
            model.addAttribute(ERROR_MESSAGE_ATTRIBUTE, e.getMessage());
            return "upload";
        }

        return "upload";
    }

    @GetMapping(ROUTE_STATUS + "/{fileId}")
    public String status(Model model, @PathVariable String fileId){

        String status = analyzerService.getStatusByFileId(fileId);
        model.addAttribute("status", status);

        return "status";
    }



}
