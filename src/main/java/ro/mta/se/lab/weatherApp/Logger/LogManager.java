/*
 * LogManager
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package ro.mta.se.lab.weatherApp.Logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class LogManager used for logging weatherApp actions
 *
 * @author Rosca Stefan
 */

public class LogManager {

    private static LogManager singleInstance;
    private static Logger historyLogger;
    private static Logger eventLogger;
    private LogManager() {
    }

    /**
     * Method for returning the single instance of Singleton class LogManager
     * providing double-checked locking implementation for thread-safety
     * but avoiding extra overhead everytime for performance reasons
     *
     * @return the single instance of LogManager
     */
    public static LogManager getInstance() {
        if (singleInstance == null) {
            synchronized (LogManager.class) {
                if (singleInstance == null) {
                    singleInstance = new LogManager();
                }
            }
        }
        return singleInstance;
    }

    /**
     * Method for getting the right logger (console or file)
     * @param type must be one of LoggerType values
     * @return
     * @throws IOException
     */
    public static Logger getLogger(LoggerType type)   {
        if (type == LoggerType.eventLogger && eventLogger == null) {
            eventLogger = Logger.getLogger("LogEvent");
            eventLogger.setUseParentHandlers(false);
            Handler fileHandler = null;
            try {
                fileHandler = new FileHandler("src/main/resources/logEvent.txt", 2000, 1,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            EventLogFormatter eventLogFormatter = new EventLogFormatter();
            fileHandler.setFormatter(eventLogFormatter);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            eventLogger.addHandler(fileHandler);
            return eventLogger;
        }
        if ( type == LoggerType.historyLogger && historyLogger == null)
        {
            historyLogger = Logger.getLogger("LogHistory");
            historyLogger.setUseParentHandlers(false);
            Handler fileHandler = null;
            try {
                fileHandler = new FileHandler("src/main/resources/logHistory.txt", 2000, 1,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HistoryLogFormatter historyLogFormatter = new HistoryLogFormatter();
            fileHandler.setFormatter(historyLogFormatter);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            historyLogger.addHandler(fileHandler);
            return historyLogger;
        }
        if(type == LoggerType.eventLogger && eventLogger != null)
        {
            return eventLogger;
        }
        if(type == LoggerType.historyLogger && historyLogger != null)
        {
            return historyLogger;
        }
        return null;
    }
}