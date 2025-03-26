package com.ss.excelAnalyzer.service;

import com.ss.Except4SupportDocumented;
import com.ss.excelAnalyzer.conf.js.ConfJsExcelAnalyzer;
import vl.utils.FormatsDate;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class DirManager {

    public static final String DIR_PATH_FILES = ConfJsExcelAnalyzer.getInstance().getApp().getFileDirectory() + File.separator;
    public static final String DIR_PATH_DOWNLOAD_REL = "d" + File.separator;
    public static final String DIR_PATH_DOWNLOAD = DIR_PATH_FILES + DIR_PATH_DOWNLOAD_REL;
    public static final String DIR_PATH_TEMP = "temp" + File.separator;
    public static final String DIR_PATH_RESOURCES = "r" + File.separator;
    public static final String DIR_PATH_FONTS = ConfJsExcelAnalyzer.getInstance().getApp().getFontsDirectory() + File.separator;
    public static final String DIR_PATH_USER_GROUP = "ug"; // todo: add file.separator and check getPath
    public static final String DIR_RESOURCES = ConfJsExcelAnalyzer.getInstance().getApp().getResourcesDirectory();
    public static final String DIR_NAME_FOR_EXAMPLE = "examples";
    public static final String DIR_RESOURCES_EXAMPLE = DIR_RESOURCES + File.separator + DIR_NAME_FOR_EXAMPLE;

    public static final String DIR_NAME_FOR_USER_GROUP = "ug_";
    public static final String DIR_NAME_FOR_USER = "u_";
    public static final String DIR_NAME_FOR_COMPANY = "com_";
    public static final String DIR_NAME_FOR_DOWNLOAD = "d_";
    public static final String DIR_NAME_FOR_PERSON = "person_";

    public static final String FILE_NAME_FOR_SIGN = "sign_";
    public static final String FILE_NAME_FOR_STAMP = "stamp_";
    public static final String FILE_NAME_FOR_TRIPLIST = "tl_%d_";
    public static final String FILE_NAME_FOR_ARCHIVE = "tl_%d_%02d";

    public static final String FILE_POSTFIX_TL_CLEAR = "c_";
    public static final String FILE_POSTFIX_TL_SCAN = "s_";
    public static final String FILE_POSTFIX_TL_1C = "1c";

    public static final String FILE_EXTENSION_PDF = ".pdf";
    public static final String FILE_EXTENSION_ZIP = ".zip";
    public static final String FILE_EXTENSION_XML = ".xml";
    public static final String FONT_NAME_NORMAL = "timesnewromanpsmt.ttf";
    public static final String FONT_NAME_BOLD = "TimesNewRomanBold.ttf";

    private static final SimpleDateFormat DOWNLOAD_DATE_FORMAT = FormatsDate.SDF_UNDERSCORE_YEAR_MONTH_REVERSED;
    private static final DateTimeFormatter DOWNLOAD_LOCALDATE_FORMAT = FormatsDate.DTF_UNDERSCORE_YEAR_MONTH_REVERSED;

    public static String getLocalPath(String from, String to) {
        Path pathFrom = Paths.get(from);
        Path pathTo = Paths.get(to);
        return pathFrom.relativize(pathTo).toString();
    }

    public static String getOrCreateDir(long userId, long companyId) {

        StringBuilder currentPath = new StringBuilder(DIR_PATH_FILES);
        String[] directories = getPathStr(userId, companyId);
        for (String directory : directories) {
            currentPath.append(directory).append(File.separator);
            createDirByPath(currentPath.toString());
        }
        return currentPath.toString();
    }

    public static String getOrCreateDir(long userId, long companyId, long personId) {
        return createDirByPath(getOrCreateDir(userId, companyId) + DIR_NAME_FOR_PERSON + personId + File.separator);
    }

    public static String getOrCreateDir(long userId, long companyId, LocalDate date) { // for download
        return createDirByPath(getOrCreateDir(userId, companyId) + File.separator + DIR_NAME_FOR_DOWNLOAD + date.format(DOWNLOAD_LOCALDATE_FORMAT) + File.separator);
    }

    public static String getOrCreateDir(long userId, long companyId, int year) { // for tl
        return createDirByPath(getOrCreateDir(userId, companyId) + year + File.separator);
    }

    public static String getOrCreateDirDownload(Date date) {
        return createDirByPath(DIR_PATH_DOWNLOAD + "d_" + DOWNLOAD_DATE_FORMAT.format(date));
    }
    public static String getOrCreateDirDownload(LocalDateTime date) {
        return createDirByPath(DIR_PATH_DOWNLOAD + "d_" + DOWNLOAD_LOCALDATE_FORMAT.format(date));
    }

    public static String createDirByPath(String path) {
        File dir = new File(path);
        dir.setReadable(true, false);
        dir.setWritable(true, false);

        if (dir.exists()) {
            return path;
        }
        if (!dir.mkdir()) {
            throw new Except4SupportDocumented("DirManagerCreate1", "Failed to create dir: " + path);
        }
        return path;
    }

    private static String[] getPathStr(long userId, long companyId) {
        return new String[]{DIR_PATH_USER_GROUP, DIR_NAME_FOR_USER_GROUP + (userId % 1000),
                DIR_NAME_FOR_USER + userId, DIR_NAME_FOR_COMPANY + companyId};
    }

    private static String getPath(long userId, long companyId) {
        return String.join(File.pathSeparator, getPathStr(userId, companyId));
    }

    private static String getPathToPerson(long userId, long companyId, long personId) {
        return getPath(userId, companyId) + File.separator + (DIR_NAME_FOR_PERSON + personId);
    }

    public static void deleteDirIfExist(long userId, long companyId) {
        deleteByPath(getPath(userId, companyId));
    }

    public static void deleteDirIfExist(long userId, long companyId, long personId) {
        deleteByPath(getPathToPerson(userId, companyId, personId));
    }

    public static void deleteByPath(List<String> pathes) {
        for (String path : pathes) {
            deleteByPath(path);
        }
    }

    public static void deleteByPath(String path) {
        try {
            Files.walkFileTree(Path.of(path), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (NoSuchFileException e) { // Directory is not exist
        } catch (IOException e) {
            throw new Except4SupportDocumented("DirManagerDel1", "Failed to delete : " + path, e);
        }
    }
    public static String getTempPath(){
        return DIR_PATH_FILES + DIR_PATH_TEMP;
    }
    // todo: delete this and change all usages to constant
    public static String getFontsPath() {
        return DIR_PATH_FONTS;
    }
}