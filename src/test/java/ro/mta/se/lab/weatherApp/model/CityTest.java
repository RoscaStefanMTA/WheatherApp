package ro.mta.se.lab.weatherApp.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;
import ro.mta.se.lab.weatherApp.Exceptions.NetworkException;

import java.io.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CityTest {

    String testResponse=null;

    IParseObject testResponseReqWithIDJSON=null;
    IParseObject testResponseReqWithLonLatJSON=null;
    IParseObject testResponseReqWithCityNameCountryCodeJSON=null;

    /**
     * citire fisier ce contine un obiect JSON ce va fi utilizat pentru test
     * @return continut fisier
     */
    private String readTestContent(){
        StringBuilder content=new StringBuilder();
        BufferedReader in ;
        try {
            InputStream inputStream= new FileInputStream("src/main/resources/test.json"    );
            in = new BufferedReader(
                    new InputStreamReader(inputStream));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * functie ce initializeaza obiectele folosite pentru testare
     */
    @Before
    public void setUp(){
        this.testResponse=this.readTestContent();
        this.testResponseReqWithIDJSON=new ParseJsonObject(testResponse) ;
        this.testResponseReqWithLonLatJSON=new ParseJsonObject(testResponse) ;
        this.testResponseReqWithCityNameCountryCodeJSON=new ParseJsonObject(testResponse) ;
    }

    /**
     * dezalocare resurse
     */
    @After
    public void tearDown(){
        this.testResponse=null;
        this.testResponseReqWithIDJSON=null ;
        this.testResponseReqWithLonLatJSON=null ;
        this.testResponseReqWithCityNameCountryCodeJSON=null ;
    }

    /**
     * test pentru a vedea daca datele obinute de la API utilizand ca identificare a orasului numele sau si codul ISO al tarii
     */
    @Test(timeout = 1000)
    public void testReqWithCityNameCountryCode() {

        try {
            String queryParam="q=Bucharest,RO";
            //creare mock si setare parametrii de return
            IHttpClient httpClient=mock(HttpClient.class);

            when( httpClient.execute( queryParam ) ).thenReturn( this.testResponseReqWithCityNameCountryCodeJSON );
            when( httpClient.getTypeReq() ).thenReturn( "cc" );

            City city=new City( "Bucharest",683506,26.10626,44.432251,"RO");

            CityWeather target=city.getWeatherDetails( httpClient );

            CityWeather cityWeather=new CityWeather( testResponseReqWithIDJSON );
            //verrificare rezultat
            Assert.assertEquals( true, target.equals( cityWeather ) );
        } catch (NetworkException | FileHandlerException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


    /**
     * test pentru a vedea daca datele obinute de la API utilizand ca identificare a orasului id-ul asociat
     */
    @Test(timeout = 1000)
    public void testReqWithID()   {

        try {
            String queryParam="id=683506";
            //creare mock si setare parametrii de return
            IHttpClient httpClient=mock(HttpClient.class);
            when( httpClient.execute( queryParam ) ).thenReturn( this.testResponseReqWithIDJSON );
            when( httpClient.getTypeReq() ).thenReturn( "id" );

            City city=new City( "Bucharest",683506,26.10626,44.432251,"RO");

            CityWeather target=city.getWeatherDetails( httpClient );

            CityWeather cityWeather=new CityWeather( testResponseReqWithIDJSON );
            //testare rezultat
            Assert.assertEquals( true, target.equals( cityWeather ) );
        } catch (NetworkException | FileHandlerException e) {
            e.printStackTrace();
            //daca ajunge in acest punct testul va fi esuat
            Assert.fail();
        }

    }


    /**
     * test pentru a vedea daca datele obinute de la API utilizand ca identificare a orasului longitudinea si latitudinea
     */
    @Test(timeout = 1000)
    public void testReqWithLonLat() {

        try {
            String queryParam="lon=26.10626&lat=44.432251";
            //creare mock si setare parametrii de return
            IHttpClient httpClient=mock(HttpClient.class);
            when( httpClient.execute( queryParam ) ).thenReturn( this.testResponseReqWithLonLatJSON );
            when( httpClient.getTypeReq() ).thenReturn( "ll" );

            City city=new City( "Bucharest",683506,26.10626,44.432251,"RO");

            CityWeather target=city.getWeatherDetails( httpClient );

            CityWeather cityWeather=new CityWeather( testResponseReqWithIDJSON );
            //testare rezultat
            Assert.assertEquals( true, target.equals( cityWeather ) );
        } catch (NetworkException | FileHandlerException e) {
            e.printStackTrace();
            //daca ajunge in acest punct testul va fi esuat
            Assert.fail();
        }
    }

    /**
     * test pentru a vedea daca in cazul aparitiei unei erori  aceasta va fi prinsa
     */
    @Test(timeout = 1000)
    public void testInvalidReqWithLonLat() {

        try {

            String queryParam="lon=26.10626=44.432251";
            //creare mock si setarea eroare ce va fi aruncata
            IHttpClient httpClient=mock(HttpClient.class);
            when( httpClient.execute( queryParam ) ).thenThrow(Exception.class);
            when( httpClient.getTypeReq() ).thenReturn( "ll" );

            City city=new City( "Bucharest",683506,26.10626,44.432251,"RO");

            city.getWeatherDetails( httpClient );

            //daca ajunge in acest punct testul va fi esuat
            Assert.fail();

        } catch (Exception e) {
            //testul este ok
            Assert.assertTrue( true );
        }
    }
}