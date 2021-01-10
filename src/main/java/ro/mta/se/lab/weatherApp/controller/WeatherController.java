package ro.mta.se.lab.weatherApp.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONException;
import org.json.JSONObject;
import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;
import ro.mta.se.lab.weatherApp.Logger.LogManager;
import ro.mta.se.lab.weatherApp.Logger.LoggerType;
import ro.mta.se.lab.weatherApp.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Aceasta clasa controleaza informatiile din interfata grafica
 *
 * @author Rosca Stefan
 */

public class WeatherController {

    @FXML
    /**
     * reprezinta label-ul unde se va fi afisa viteza vantului
     */
    private Label windSpeed;

    @FXML
    /**
     * reprezinta label-ul unde se va fi afisa cat de innorat este
     */
    private Label cloudiness;

    @FXML
    /**
     * reprezinta label-ul unde se va fi afisa presiunea
     */
    private Label pressure;

    @FXML
    /**
     * reprezinta label-ul unde se va fi afisa umiditatea
     */
    private Label humidity;

    @FXML
    /**
     * reprezinta label-ul unde se va fi afisa numele orasului sau daca nu este selectat un oras "Select City"
     */
    private Label cityNameValue;

    @FXML
    /**
     * reprezinta label-ul unde se va fi afisa decrierea vremii
     */
    private Label desc;

    @FXML
    /**
     * reprezinta label-ul unde va fi afisa temperatura
     */
    private Label temperature;

    @FXML
    /**
     * reprezinta label-ul unde va fi afisa data
     */
    private Label day;

    @FXML
    /**
     * reprezinta label-ul unde va fi afisa o imagine in functie de vreme
     */
    private ImageView img;

    @FXML
    /**
     * reprezinta un combo box ce va permite utilizatorului selectia unui oras
     */
    private ComboBox<String> cities;

    @FXML
    /**
     * reprezinta un combo box ce va permite utilizatorului selectia unei tari
     */
    private ComboBox<String> countries;

    /**
     * lista cu numele tarilor disponibile
     */
    private Set<String> countriesName=new HashSet<>();

    /**
     * clientul HTTP ce va face cereri la API pentru a obtine informatii meteo
     */
    private IHttpClient httpClient;
    /**
     * numele fisierului de initializare
     */
    private String initFileName;


    private WeatherManager weatherManager;



    /**
     * eveniment ce apare cand se doreste selectarea unei tarii
     */
    private EventHandler<ActionEvent> eventSelectCountry= new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            String countryName = countries.getValue();
            //popularea cu numele oraselor in combobox cities
            ArrayList<String> citiesName = weatherManager.getCitiesFromCountry(countryName);

            cities.setItems(FXCollections.observableArrayList(citiesName));
            //logarea evenimet
            LogManager.getLogger(LoggerType.eventLogger).log(Level.INFO, "Select country: " + countryName);

            resetParam();
        }
    };

    /**
     * evenimet ce apare la selectarea unui oras si afiseaza informatii meteo depre acesta
     */
    private EventHandler<ActionEvent> eventSelectCity= new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            ///obtine valoare selectata in combobox
            String cityName = cities.getValue();

            //obtine informatii de la API
            CityWeather cityWeather = weatherManager.getWeatherDetails(cityName);
            if (cityWeather != null) {
                //afisare detalii in interfata grafica
                showWeather( cityWeather );
                //logare istoric aplicatie
                LogManager.getLogger( LoggerType.historyLogger ).log( Level.INFO, "Display information about: " +
                        cityName + "(Temperature: " + temperature.getText() + " Humidity: " + humidity.getText()
                        + " Pressure: " + pressure.getText() + ")" );
                LogManager.getLogger( LoggerType.eventLogger ).log( Level.INFO, "Display information about: " +
                        cityName + "(Temperature: " + temperature.getText() + " Humidity: " + humidity.getText()
                        + " Pressure: " + pressure.getText() + ")" );
            }
        }
    };


    /**
     * initializeaza elemetele din interfata grafica la deschiderea aplicatiei
     */
    @FXML
    private void initialize( )  {

        try {
            //initializare parametrii de configurare
            this.initConfParam();
            this.weatherManager=WeatherManager.getInstance( initFileName,httpClient );

        } catch (FileHandlerException e) {
            LogManager.getLogger(LoggerType.eventLogger).log(Level.ALL, "Error: " + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "This is an error!\n"+e.getMessage()).showAndWait();
        }
        //initializare lista tari
        countriesName=weatherManager.getCountriesName();
        //setarea lista de tarii dispnibile
        countries.setItems( FXCollections.observableArrayList(countriesName));
        //setare unui hadler cand apare evenimete ce implica combobox-urile  din aplicatie
        countries.setOnAction( eventSelectCountry );
        cities.setOnAction( eventSelectCity );
    }


    /**
     * va afisa in interfata grafica detaliile meteo despre orasul selectat
     * @param city obiect ce contine  detaliile meteo
     */
    private void showWeather(CityWeather city){

        cityNameValue.setText(city.getCity());
        temperature.setText(city.getTemperature().toString()+"°C");
        day.setText( city.getTime() );
        desc.setText(city.getDescription());
        img.setImage(new Image( ImageHandler.getImage( city.getIcon() ) ));
        windSpeed.setText(city.getWindSpeed() + " m/s");
        cloudiness.setText(city.getCloudiness() + " %");
        pressure.setText( city.getPressure() + " hpa");
        humidity.setText( city.getHumidity() + " %");
    }

    /**
     * "stergere " informatii din interfata grafica cand se selecteaza o noua tara
     */
    private void resetParam(){
        cityNameValue.setText("Select city");
        temperature.setText("0 °C");
        day.setText( "" );
        desc.setText("");
        img.setImage(null);
        windSpeed.setText( " m/s");
        cloudiness.setText( "0 %");
        pressure.setText( "0 hpa");
        humidity.setText( "0 %");
    }





    /**
     * are scopul de a initializa parametrii aplicatiei pe baza fisierului de configurare
     * @throws FileHandlerException daca apare o eroarea fisierului de configurare sau la parsare unui obiect de tip JSON
     */
    private void initConfParam() throws FileHandlerException {
        StringBuilder content=new StringBuilder();
        BufferedReader in;
        try {
            //citire fisier
            InputStream inputStream= new FileInputStream("src/main/resources/conf.json"    );
            in = new BufferedReader(
                    new InputStreamReader(inputStream));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (IOException e ) {
            LogManager.getLogger(LoggerType.eventLogger).log(Level.ALL, "Error: " + e.getMessage());
            throw new FileHandlerException("Error read configuration file");
        }

        try {
            //parsare obiect JSON
            JSONObject jsonObject=new JSONObject(content.toString());


            String apikey=jsonObject.getString( "apikey" );
            String baseUri=jsonObject.getString( "base_uri" );
            String endPoint=jsonObject.getString( "end_point" );
            int timeout=jsonObject.getInt( "timeout" );
            String typeReq=jsonObject.getString( "type_request" );
            this.initFileName= jsonObject.getString("init_file");
            httpClient=new HttpClient( baseUri,endPoint,apikey,timeout,typeReq );
        }catch (JSONException e){
            LogManager.getLogger(LoggerType.eventLogger).log(Level.ALL, "Error: " + e.getMessage());
            throw new FileHandlerException("Error parse JSON object(configuration file:conf.json).");
        }

    }

}
