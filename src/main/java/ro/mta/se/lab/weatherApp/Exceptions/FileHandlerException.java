package ro.mta.se.lab.weatherApp.Exceptions;


/**
 * Exceptie care poate aparea la lucrul cu fisierle
 * @author Rosca Stefan
 */
public class FileHandlerException extends  Exception{
    String message;

    public FileHandlerException(String message) {
        super(message);
    }


    public void print(){
        System.out.println("Exception message(file error): " + message);
    }
}
