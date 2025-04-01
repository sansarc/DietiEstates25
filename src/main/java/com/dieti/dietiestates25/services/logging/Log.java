package com.dieti.dietiestates25.services.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    private Log() {}

    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    public static void info(String logMessage) {
        logger.info(logMessage);
    }

    public static void warn(String logMessage) {
        logger.warn(logMessage);
    }

    public static void error(String logMessage) {
        logger.error(logMessage);
    }

    public static void debug(String logMessage) { logger.debug(logMessage); }
}
