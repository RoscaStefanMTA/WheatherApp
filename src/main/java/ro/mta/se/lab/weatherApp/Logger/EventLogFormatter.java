package ro.mta.se.lab.weatherApp.Logger;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class for logging event app
 *
 * @author Rosca Stefan
 */
public class EventLogFormatter extends Formatter  {
    /**
     * Method that change default format for Logger.
     * @param record entry in the file
     * @return
     */
    @Override
    public String format(LogRecord record) {
        return record.getThreadID()+"::"+record.getSourceClassName()+"::"
                +record.getSourceMethodName()+"::"
                +new Date(record.getMillis())+"::"
                +record.getMessage()+"\n";
    }
}
