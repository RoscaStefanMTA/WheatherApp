package ro.mta.se.lab.weatherApp.model;

import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;
import ro.mta.se.lab.weatherApp.Exceptions.NetworkException;

import java.util.Locale;

/**
 * Aceasta clasa va modela un oras.Aceasta clasa va contine informatii ce vor
 * fi utilizate pentru a obtine de la API informatii despre vreme
 *
 * @author Rosca Stefan
 */

public class City {
    /**
     * numele orasului
     */
    private String cityName;
    /**
     * id-ul orasului dupa care poate fi identificat de catre API
     */
    private Integer idCity;
    /**
     * longitudinea
     */
    private Double lon;
    /**
     * latitudinea
     */
    private Double lat;
    /**
     * codul ISO al tarii din care face parte orasul
     */
    private String codeCountry;
    /**
     * numele tarii
     */
    private String countryName;

    public City(String cityName, Integer idCity, Double lon, Double lat, String codeCountry) {
        this.cityName = cityName;
        this.idCity = idCity;
        this.lon = lon;
        this.lat = lat;
        this.codeCountry = codeCountry;
        this.countryName = this.getCountryFromCountryCodes( this.codeCountry );
    }


    /**
     * va identifica numele tarii pe baza codului ISO al tarii
     * @param code codul ISO al tarii din care face parte orasul
     * @return numele tarii
     */
    public String getCountryFromCountryCodes(String code){
        Locale locale=new Locale( "",code );
        return locale.getDisplayCountry();
    }

    public String getCityName() {
        return cityName;
    }

    public String getCodeCountry() {
        return codeCountry;
    }

    public String getCountryName() {
        return countryName;
    }


    /**
     * Aceasta functie va returna un strin ce reprezinta un parametru al metodei GET dupa care API-ul va identifica orasul
     * @param type  reprezinta modalitatea dupa care orasul va fi identificat de catre API
     * @return valoarea parametrului in functie de tipul dorit
     */
    private String getQueryParam(String type){
        switch (type) {
            case "id":
                return "id="+this.idCity;
            case "ll":
                return "lon="+this.lon+"&lat="+this.lat;
            case "cc":
                return "q="+this.cityName+","+this.getCodeCountry();

        }
        return "q="+this.cityName+","+this.getCodeCountry();
    }

    /**
     * Aceasta metoda va obtine informatii meteo de la API
     * @param client
     * @return un obiect de tip CityWeather ce contine informatii meteo despre oras
     * @throws NetworkException daca apare o problema de conexiune
     * @throws FileHandlerException daca apara o problema la parsarea fisierului JSON
     */

    public CityWeather getWeatherDetails(IHttpClient client) throws NetworkException, FileHandlerException {
        String queryParam=this.getQueryParam(client.getTypeReq());
        IParseObject parseObject = client.execute(queryParam);
        if(parseObject==null)
            return null;
        return new CityWeather( parseObject );
    }


}
