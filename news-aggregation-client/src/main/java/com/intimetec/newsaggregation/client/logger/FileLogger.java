package com.intimetec.newsaggregation.client.logger;

import com.intimetec.newsaggregation.client.constant.LogLevel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private static final String LOG_FILE_PATH = "src/main/resources/application.log";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static FileLogger instance;
    private final File logFile;

    private FileLogger() {
        this.logFile = new File(LOG_FILE_PATH);
        createLogFileIfNotExists();
    }

    public static FileLogger getInstance() {
        if (instance == null) {
            instance = new FileLogger();
        }
        return instance;
    }

    private void createLogFileIfNotExists() {
        try {
            if (!logFile.exists()) {
                File directory = logFile.getParentFile();
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                logFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error creating log file: " + e.getMessage());
        }
    }

    private void writeLog(String level, String message) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            String logEntry = String.format("[%s] %s: %s%n", timestamp, level, message);
            writer.write(logEntry);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public void info(String message) {
        writeLog(LogLevel.INFO.name(), message);
    }

    public void debug(String message) {
        writeLog(LogLevel.DEBUG.name(), message);
    }

    public void error(String message) {
        writeLog(LogLevel.ERROR.name(), message);
    }

    public void warn(String message) {
        writeLog(LogLevel.WARN.name(), message);
    }

}