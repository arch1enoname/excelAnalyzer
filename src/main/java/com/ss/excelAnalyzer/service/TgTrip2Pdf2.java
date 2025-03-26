package com.ss.excelAnalyzer.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.ss.Except4Support;
import com.ss.Except4SupportDocumented;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Slf4j
public class TgTrip2Pdf2 {

    private static final float SCALE_FACTOR = 0.79f;

    public static void generatePdfFromString(String htmlContent, String outputPath, String fileName) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(outputPath + fileName + DirManager.FILE_EXTENSION_PDF);
            String baseUrl = FileSystems.getDefault().getPath(outputPath)
                    .toUri().toURL().toString();
            generatePdfFromString(htmlContent, os, baseUrl);
        } catch (FileNotFoundException e) {
            throw new Except4SupportDocumented("ErrPdfGenerator1", "Failed create ouput stream " + outputPath + fileName + DirManager.FILE_EXTENSION_PDF);
        } catch (MalformedURLException e) {
            throw new Except4SupportDocumented("ErrPdfGenerator2", "Failed create base URL " + outputPath);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void generatePdfFromString(String htmlContent, OutputStream os, String baseUrl) {

        Document doc = Jsoup.parse(htmlContent);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).charset(StandardCharsets.UTF_8);
        String s = DirManager.getFontsPath() + DirManager.FONT_NAME_NORMAL;
        log.info(s);
        try {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext cntxt = renderer.getSharedContext();
            cntxt.setPrint(true);
            cntxt.setInteractive(false);
            renderer.setDocumentFromString(doc.html(), baseUrl);
            renderer.getSharedContext().setDotsPerPixel((int) (renderer.getSharedContext().getDotsPerPixel() * SCALE_FACTOR));   // Size
            renderer.getFontResolver().addFont(DirManager.getFontsPath() + DirManager.FONT_NAME_NORMAL, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.layout();
            renderer.createPDF(os);
        } catch (IOException e) {
            throw new Except4Support("ErrPdfGenerator3", "Failed to load font " + DirManager.getFontsPath() + DirManager.FONT_NAME_NORMAL + " to ITextRenderer", e.getCause());
        } catch (DocumentException e) {
            throw new Except4Support("ErrPdfGenerator4", "Failed to get byte array ", e.getCause());
        }
    }
}