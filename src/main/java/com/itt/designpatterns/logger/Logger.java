package com.itt.designpatterns.logger;

import java.util.Objects;

public class Logger {

    private static Logger instance;

    private Logger() {}

    // Thread-safe Singleton instance retrieval
    public static Logger getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (Logger.class) {
                if (Objects.isNull(instance)) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("Log: " + message);
    }
}
