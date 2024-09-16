package com.example.todoNew;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
    public void setLogDestination(LogDestination logDestination) {
    }

    public enum LogDestination {
        LOGGER_ONLY,
        SYSTEM_OUT_ONLY,
        ALL
    }

    @Getter
    @Setter
    private LogDestination logDestination = LogDestination.LOGGER_ONLY;

    private static final Logger logger = LoggerFactory.getLogger(LogManager.class);

      private static LogManager instance;

    private LogManager() {
    }

    // Метод для получения единственного экземпляра класса
    public static synchronized LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    public void logInfo(String message) {
        switch (logDestination) {
            case LOGGER_ONLY:
                logger.info(message);
                break;
            case SYSTEM_OUT_ONLY:
                System.out.println(message);
                break;
            case ALL:
                logger.info(message);
                System.out.println(message);
                break;
        }
    }

    public void logError(String message, Throwable throwable) {
        switch (logDestination) {
            case LOGGER_ONLY:
                logger.error(message, throwable);
                break;
            case SYSTEM_OUT_ONLY:
                System.err.println(message);
                throwable.printStackTrace(System.err);
                break;
            case ALL:
                logger.error(message, throwable);
                System.err.println(message);
                throwable.printStackTrace(System.err);
                break;
        }
    }
}
