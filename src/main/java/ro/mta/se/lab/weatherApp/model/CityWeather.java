package ro.mta.se.lab.weatherApp.model;

import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 * Aceasta clasa va modela informatiile meteo despre un oras
 *
 * @author Rosca Stefan
 */

public class CityWeather {

    /**
     * numele orasului
     */
    private String city;
    /**
     * numele tarii
     */
    private String country;

    /**
     * reprezinta timpul la care sunt aceste informatii despre oras valabile
     */
    private String time;
    /**
     * temperatura
     */
    private Double temperature;
    /**
     * reprezinta un identificator ce va fi folosit pentru a selecta o imagine ce va fi afista in interfata grafica in functie de detaliile meteo
     */
    private String icon;
    /**
     * descrierea informatiilor  meteorologice
     */
    private String description;
    /**
     * viteza vantului
     */
    private String windSpeed;
    /**
     * cat de innorat este
     */
    private String cloudiness;
    /**
     * presiunea
     */
    private String pressure;
    /**
     * umiditatea
     */
    private String humidity;

    /**
     * obiect ve va fi utilizat pentru a parsa obiectul JSON primit de la API
     */
    private IParseObject parseObject;


    public CityWeather(IParseObject parseObject) throws FileHandlerException {
        if(parseObject==null)
            return;
        this.parseObject=parseObject;
        ArrayList<String> stringArrayList = (ArrayList<String>) this.parseObject.parse();
        this.city = stringArrayList.get( 0 );
        this.temperature = Double.parseDouble( stringArrayList.get( 1 ) );
        this.icon = stringArrayList.get( 2 );
        this.description = stringArrayList.get( 3 );
        this.windSpeed = stringArrayList.get( 4 );
        this.cloudiness = stringArrayList.get( 5 );
        this.pressure = stringArrayList.get( 6 );
        this.humidity = stringArrayList.get( 7 );

        long timestamp= Long.parseLong( stringArrayList.get( 8 ) );
        this.setTime( this.time ,timestamp);
    }

    /**
     * Nu am folosit timestamp-ul furnizat de API ,deoarece nu am reusit sa obtin precizie si la afisarea minutelor
     * @param day valoare care va fi setata valoarea timpului afisat
     * @param timeStamp valoarea timestamp-ului obtinut de la API
     */

    private void setTime(String day,long timeStamp){

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
        Date time=new Date( timeStamp *1000);
        this.time= df.format( new Date() );
    }

    public String getCity() {
        return city;
    }

    public String getTime() {
        return time;
    }

    public Double getTemperature() { return this.temperature; }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }


    @Override
    public boolean equals(Object object) {
        CityWeather cityWeather=(CityWeather) object;
        if(object==null)
            return false;
        if(!this.city.equals( cityWeather.city ))
        {
            return false;
        }
        if(!this.temperature.equals( cityWeather.temperature ))
        {
            return false;
        }
        if(!this.icon.equals( cityWeather.icon ))
        {
            return false;
        }
        if(!this.description.equals( cityWeather.description ))
        {
            return false;
        }
        if(!this.windSpeed.equals( cityWeather.windSpeed ))
        {
            return false;
        }
        if(!this.cloudiness.equals( cityWeather.cloudiness ))
        {
            return false;
        }
        if(!this.pressure.equals( cityWeather.pressure ))
        {
            return false;
        }
        if(!this.humidity.equals( cityWeather.humidity ))
        {
            return false;
        }
        return true;
    }

}
