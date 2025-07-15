package com.itt.designpatterns.logger;

public class LoggingApplication implements Runnable {

    public static void main(String[] args) {
        LoggingApplication firstLogger = new LoggingApplication();
        LoggingApplication secondLogger = new LoggingApplication();

        Thread thread1 = new Thread(firstLogger);
        Thread thread2 = new Thread(secondLogger);

        thread1.start();
        thread2.start();
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        System.out.println(logger.hashCode());
    }

}
