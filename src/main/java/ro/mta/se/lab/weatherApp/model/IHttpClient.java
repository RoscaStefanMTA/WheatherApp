package ro.mta.se.lab.weatherApp.model;


import ro.mta.se.lab.weatherApp.Exceptions.NetworkException;

/**
 * @author Rosca Stefan
 */

public interface IHttpClient {
    IParseObject execute(String queryParam) throws NetworkException;

    String getTypeReq();
}
