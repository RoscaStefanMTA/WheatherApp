/*
 * LogManager
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package ro.mta.se.lab.weatherApp.Logger;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class for logging history app
 *
 * @author Rosca Stefan
 */
public class HistoryLogFormatter extends Formatter {

    /**
     * Method that change default format for Logger.
     * @param record entry in the file
     * @return
     */
    @Override
    public String format(LogRecord record) {
        return  "[HISTORY]"+"::"
                +new Date(record.getMillis())+"::"
                +record.getMessage()+"\n";
    }
}
