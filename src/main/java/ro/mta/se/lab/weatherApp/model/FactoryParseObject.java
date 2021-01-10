package ro.mta.se.lab.weatherApp.model;


/**
 *
 * Clasa are o metoda ce va crea obiecte concrete de tip ParseJsonObject si ParseFileObject
 * @author Rosca Stefan
 */

public class FactoryParseObject {

    /**
     *  creeaza obiecte de tip ParseJsonObject si ParseFileObject in functie de parametrul type
     * @param type specifica ce tip de obiect va fi creat
     * @param param este primit de catre constructorul obiectului
     * @return un obiect de tip ParseObject in functie de tip
     */
    public static IParseObject getParseObject(String type,String param){
        if(type == null)
            return null;
        if(type.equalsIgnoreCase( "JSON" )){
            return new ParseJsonObject( param );
        }
        if(type.equalsIgnoreCase( "FILE" )){
            return new ParseFileObject( param );
        }
        return null;
    }
}
