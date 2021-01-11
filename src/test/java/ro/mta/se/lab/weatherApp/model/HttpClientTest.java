package ro.mta.se.lab.weatherApp.model;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;
import ro.mta.se.lab.weatherApp.Exceptions.NetworkException;

import java.util.ArrayList;

public class HttpClientTest {


      String baseUri;
      String apiKey;
      String endPoint;
      int timeout;
      String typeReq;
      HttpClient httpClient;


    /**
     * functie ce initializeaza obiectele folosite pentru testare
     */
    @Before
    public void  setUp(){
        this.baseUri="https://api.openweathermap.org";
        this.apiKey="b7f04de2595bb2d357442a8a9bac4686";
        this.endPoint="data/2.5/weather";
        this.timeout=2000;
        this.typeReq="cc";
    }


    /**
     * testare daca API-ul raspunde cu succes la request-ul aplicatiei
     */
    @Test(timeout = 2000)
    public void testSuccessCall(){
        try {
            this.httpClient=new HttpClient(baseUri,endPoint,apiKey,timeout,typeReq);
            String queryParam="q=London,GB";
            IParseObject parseObject=this.httpClient.execute(queryParam);
            ArrayList<String> param=(ArrayList<String>) parseObject.parse();
            //verificare resultat
            Assert.assertEquals(200, Integer.parseInt(param.get(9)));
        } catch (NetworkException | FileHandlerException e) {
            //daca ajunge in acest punct testul va fi esuat
            Assert.fail();
        }
    }


    /**
     * testare daca in cazul unor parametrii invalizi metoda execute() va arunca o eroare de tip NetworkException
     * @throws NetworkException
     */
    @Test(expected = NetworkException.class,timeout = 2000)
    public void testInvalidQueryParam() throws NetworkException {
        //setare parametrii invalizi
        String queryParam="q=LondonNotExist";
        this.httpClient=new HttpClient(baseUri,endPoint,apiKey,timeout,typeReq);
        httpClient.execute(queryParam);

    }

}
