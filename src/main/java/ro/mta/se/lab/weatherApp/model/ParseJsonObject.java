package ro.mta.se.lab.weatherApp.model;

import org.json.JSONException;
import org.json.JSONObject;
import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;
import ro.mta.se.lab.weatherApp.Logger.LogManager;
import ro.mta.se.lab.weatherApp.Logger.LoggerType;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Aceasta clasa este utilizata pentru a parsa un obiect JSON ce
 * contine informatii meteorologice despre un oras
 *
 * @author Rosca Stefan
 */

public class ParseJsonObject implements IParseObject{

    /**
     * valoarea continutului obiectului JSON ce va fi parsat
     */
    private String content;

    public ParseJsonObject(String content) {
        this.content = content;
    }

    /**
     * Aceasta metoda va parsa un obiect de tip JSON care contine informatii meteorologice despre un anumit oras
     * @return un ArrayList de tip String ce va contine informatii meteorologice despre un anumit oras care vor fi afisate in interfata grafica
     * @throws FileHandlerException
     */
    @Override
    public Object parse() throws FileHandlerException {
        ArrayList<String> stringArrayList=new ArrayList<>();
        try {
            //instantiaza un obiect de tip JSON cu continutul aferent
            JSONObject jsonObject=new JSONObject(content);

            //extrage informatii si le adauga in ArrayList
            stringArrayList.add( jsonObject.getString( "name" ) );
            stringArrayList.add( jsonObject.getJSONObject( "main" ).get( "temp" ).toString() );
            stringArrayList.add( jsonObject.getJSONArray( "weather" ).getJSONObject( 0 ).getString( "icon" ) );
            stringArrayList.add( jsonObject.getJSONArray( "weather" ).getJSONObject( 0 ).getString( "description" ) );
            stringArrayList.add( jsonObject.getJSONObject( "wind" ).get( "speed" ).toString() );
            stringArrayList.add( jsonObject.getJSONObject( "clouds" ).get( "all" ).toString() );

            stringArrayList.add( jsonObject.getJSONObject( "main" ).get( "pressure" ).toString() );
            stringArrayList.add( jsonObject.getJSONObject( "main" ).get( "humidity" ).toString() );

            stringArrayList.add( jsonObject.get( "dt" ).toString() );

            stringArrayList.add( jsonObject.get("cod").toString() );
        }catch (JSONException e)
        {
            //in cazul aparatitiei unei exceptii ca va fi logat aparitia evenimentului si va fi aruncata o exceptie
            LogManager.getLogger(LoggerType.eventLogger).log(Level.ALL, "Error: " + e.getMessage());
            throw new FileHandlerException("Error parse Json Object.");
        }


        return stringArrayList;
    }


}
