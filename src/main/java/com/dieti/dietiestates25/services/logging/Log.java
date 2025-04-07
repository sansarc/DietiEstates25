package com.dieti.dietiestates25.services.logging;

import org.slf4j.LoggerFactory;

public class Log {
    private Log() {}

    public static void info(Class<?> source, String logMessage) {
        LoggerFactory.getLogger(source).info(logMessage);
    }

    public static void warn(Class<?> source, String logMessage) {
        LoggerFactory.getLogger(source).warn(logMessage);
    }

    public static void error(Class<?> source, String logMessage) {
        LoggerFactory.getLogger(source).error(logMessage);
    }

    public static void debug(Class<?> source, String logMessage) {
        LoggerFactory.getLogger(source).debug(logMessage);
    }
}