package ro.mta.se.lab.weatherApp.Exceptions;


/**
 * Exceptie care poate aparea din diverse motive ale conexiunii de retea
 * @author Rosca Stefan
 */

public class NetworkException extends Exception{

    String message;

    public NetworkException(String message) {
        super(message);
    }


    public void print(){
        System.out.println("Exception message(network error): " + message);
    }
}
