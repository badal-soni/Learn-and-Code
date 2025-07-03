package com.intimetec.newsaggregation.client.logger;

public class ConsoleLogger {

    public void info(Object object) {
        System.out.println(object);
    }

    public void error(Object object) {
        System.err.println(object);
    }

}
