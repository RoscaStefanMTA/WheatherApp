package ro.mta.se.lab.weatherApp.model;

import javafx.scene.control.Alert;
import ro.mta.se.lab.weatherApp.Exceptions.NetworkException;
import ro.mta.se.lab.weatherApp.Logger.LogManager;
import ro.mta.se.lab.weatherApp.Logger.LoggerType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

/**
 * Clasa ce va fi utilizata pentru a ne conecta cu API-ul si pentru
 * a obtine informatii de la acesta
 *
 * @author Rosca Stefan
 */

public class HttpClient implements IHttpClient{
    /**
     * url-ul de baza al APi-ului
     */
    private final String baseUri;
    /**
     * cheia de identificare
     */
    private final String apiKey;
    /**
     * calea catre resursa care dorim sa o accesam
     */
    private final String endPoint;
    /**
     * timpul maxim care va fi asteptat pentru primirea unui raspuns de la API
     */
    private final int timeout;
    /**
     * tipul parametrului metodei GET pentru a identifica un oras
     */
    private final String typeReq;



    public HttpClient(String baseUri,String endPoint, String apiKey,int timeout,String typeReq) {
        this.baseUri = baseUri;
        this.endPoint=endPoint;
        this.apiKey = apiKey;
        this.timeout=timeout;
        this.typeReq=typeReq;

    }

    public String getTypeReq() {
        return typeReq;
    }

    /**
     * functie ce va obtine informatii de la API
     * @param queryParam un parametru al metodei GET ce va identifica orasul
     * @return va returna un obiect ParseJsonObject ce contine informatii despre un anumit oras
     * @throws NetworkException daca sunt proble de conexiune
     */
    @Override
    public IParseObject execute( String queryParam) throws NetworkException {

        StringBuilder content = new StringBuilder();
        String requestString=this.buildRequestString(queryParam);
        try {
            //instantiere obiecte ce vor realiza conexiunea cu API-ul
            URL url = new URL(requestString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout( this.timeout );
            int status = con.getResponseCode();
            String responseMessage=con.getResponseMessage();

            //verificare daca cererea a avut loc cu succes
            if(status <200 || status >=300){
                LogManager.getLogger(LoggerType.eventLogger).log(Level.WARNING,"Code: " + status +", Message: " +responseMessage);
                throw new Exception("NO Internet connection! ");
            }
            //citire raspunsului primit de la API
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            //inchidere conexiune si logarea acestei actiuni ca a avut loc cu succes
            in.close();
            LogManager.getLogger(LoggerType.eventLogger).log(Level.INFO,"Code: " + status +", Message: " + responseMessage);

            con.disconnect();

        } catch (Exception e) {
             LogManager.getLogger(LoggerType.eventLogger).log(Level.INFO, "Error: " + e.getMessage());
             throw new NetworkException("NO Internet connection! ");

        }

        return new ParseJsonObject( content.toString() );

    }

    /**
     * va fi folosita pentru a costrui URL-ul complet care va fi folosit pentru a obtine informatii meteo despre un oras
     * @param queryParam un parametru al metodei GET ce va identifica orasul
     * @return va retetuna URL-ul complet care va fi folosit pentru a obtine informatii meteo despre un oras
     */
    private String buildRequestString(String queryParam){

        return baseUri+"/"+endPoint+"?"+queryParam+"&appid="+apiKey+"&units=metric";
    }
}
