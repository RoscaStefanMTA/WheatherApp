package ro.mta.se.lab.weatherApp.model;

import javafx.scene.control.Alert;
import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;
import ro.mta.se.lab.weatherApp.Exceptions.NetworkException;
import ro.mta.se.lab.weatherApp.Logger.LogManager;
import ro.mta.se.lab.weatherApp.Logger.LoggerType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Aceasta clasa este un manager ce gestioneaza toate entitatile te tip City si expune metode ce sunt folosite
 * de catre clasa WeatherController pentru a afisa detalii despre un oras. Aceasta clasa implementeaz pattern-ul
 * Singleton
 * @author Rosca Stefan
 */

public class WeatherManager {



    /**
     * lista cu numele tarilor disponibile
     */
    private Set<String> countriesName=new HashSet<>();
    /**
     * toate entitatile de tip oras create la citirea fisierului de initializare
     */
    private ArrayList<City> cities=new ArrayList<>();
    /**
     * clientul HTTP ce va face cereri la API pentru a obtine informatii meteo
     */
    private IHttpClient httpClient;
    /**
     * numele fisierului de initializare
     */
    private String initFileName;
    /**
     * reprezinta o instanta a clasei WeatherManager ce va fi
     * partajata de toate clasele ce vor instantia aceasta clasa
     */
    private static WeatherManager instance;




    private WeatherManager(String initFileName, IHttpClient httpClient) {
        this.initFileName = initFileName;
        this.httpClient = httpClient;
        this.initCitiesArray();
    }

    /**
     * returneaza o instanta a clasei WeatherManager ,verifica mai intai daca este creata ,
     * iar daca nu este deja creata creeaza o instanta altfel o returneaza pe cea existenta
     * De asememenea permite utilizarea clasei si intr-un sistem multithreading,chiar daca aplicatia dezvoltata
     * nu este multithreading
     * @param initFileName numele fisierului de initializare
     * @param httpClient clientul HTTP ce va face cereri la API pentru a obtine informatii meteo
     * @return unica instanta a clasei WeatherManager
     */

    public static WeatherManager getInstance(String initFileName,IHttpClient httpClient) {
        if(instance==null){
            synchronized (WeatherManager.class) {
                if(instance==null) {
                    instance = new WeatherManager( initFileName, httpClient );
                }
            }
        }
        return instance;
    }

    /**
     * initializeaza ArrayList-ul cities  cu obiecte de tip City pe baza informatiilor aflate in fisierul cu numele iniFileName
     */

    private void initCitiesArray(){
        try {
            //initializare parametrii de configurare
            IParseObject parseObject=FactoryParseObject.getParseObject("FILE", this.initFileName );
            //inistializare lista de orase
            this.cities= (ArrayList<City>) parseObject.parse();
        } catch (FileHandlerException e) {
            LogManager.getLogger( LoggerType.eventLogger).log( Level.ALL, "Error: " + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "This is an error!\n"+e.getMessage()).showAndWait();
        }
    }

    /**
     * Metoda ce va returna o lista cu numele oraselor dintr-o anumita tara
     * @param countryName numele tarii pentru care vor fi returntata lista cu numele oraselor
     * @return o lista cu numele oraselor dintr-o anumita tara
     */
    public ArrayList<String> getCitiesFromCountry(String countryName){
        ArrayList<String> citiesName=new ArrayList<>();
        for(City city:cities) {
            if(city.getCountryName().equals( countryName ))
                citiesName.add( city.getCityName() );
        }
        return  citiesName;
    }

    /**
     * cauta in lista de obiecte de tip City citiesEntities un oras dupa nume
     * @param cityName numele orasului
     * @return daca gaseste un oras va returna orasul,altfel null
     */
    private City getCityEntity(String cityName){
        for(City city:cities) {
            if(city.getCityName().equals( cityName ))
                return city;
        }
        return null;
    }

    /**
     * pe baza oraselor din fisierul de configurarea si codul ISO al tarii aceasta metoda va returna o lista cu tarile unde se gasesc acele orase
     * @return o lista cu numele tarilor
     */
    public Set<String> getCountriesName(){
        Set<String> countriesValue=new HashSet<>();
        for(City city:cities){
            String countryName=city.getCountryFromCountryCodes( city.getCodeCountry() );
            countriesValue.add( countryName );
        }
        return countriesValue;
    }

    /**
     * Aceasta metoda va returna un obiect de tip CityWeather daca exista un oras cu numele primit ca parametru
     * @param cityName numele orasului pentru care vor fi afisate datele meteo
     * @return un obiect CityWeather ce contine informatiile meteo despre un oras
     */

    public CityWeather getWeatherDetails(String cityName){
        City city=this.getCityEntity( cityName );
        if(city!=null) {
            try {
                return city.getWeatherDetails( this.httpClient );
            } catch (NetworkException e) {
                LogManager.getLogger( LoggerType.eventLogger ).log( Level.ALL, "Error: " + e.getMessage() );
                new Alert( Alert.AlertType.ERROR, "No Internet connection!" ).showAndWait();
            } catch (FileHandlerException e) {
                LogManager.getLogger( LoggerType.eventLogger ).log( Level.ALL, "Error: " + e.getMessage() );
                new Alert( Alert.AlertType.ERROR, "This is an error!\n" + e.getMessage() ).showAndWait();
            }
        }
        return null;
    }




}
