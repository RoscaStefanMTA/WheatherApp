package ro.mta.se.lab.weatherApp.model;

import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;

/**
 * @author Rosca Stefan
 */

public interface IParseObject {

     Object parse() throws FileHandlerException;
}
