package ro.mta.se.lab.weatherApp.model;

import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;
import ro.mta.se.lab.weatherApp.Logger.LogManager;
import ro.mta.se.lab.weatherApp.Logger.LoggerType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Aceasta clasa este utilizata pentru a parsa fisierul
 * ce contine detaliile despre orase pentru care aplicatia
 * va afisa informatii
 *
 * @author Rosca Stefan
 */

public class ParseFileObject implements IParseObject{

    /**
        numele fisierului ce contine informatiile despre orase
     */
    private String filename;

    public ParseFileObject(String filename) {
        this.filename = filename;
    }


    /**
     * Metoda ce va apela functia readFile care face parsarea propriu-zisa a fisierului
     * @return va returna un ArrayList de obiecte de tip City
     * @throws FileHandlerException in cazul in care fisierul nu are formatul corespunzator va fi aruncata o exceptie de acest tip
     */
    @Override
    public Object parse( ) throws FileHandlerException {
        ArrayList<City> cities=this.readFile();
        return  cities;
    }

    /**
     * Aceasta metoda parseaza fisierul in urmatorul mod:citeste cate o linie  ,dupa care linie este utilizata functia
     * split di JAVA Api ,avand ca parametru /t. Dupa care  din vectorul de tipul String sun extrase detaliile legate de un oras
     * dupa care se creeaza un obiect de tip City care este adaugat intr-un ArrayList
     * @return va returna un ArrayList de obiecte de tip City
     * @throws FileHandlerException in cazul in care fisierul nu are formatul corespunzator va fi aruncata o exceptie de acest tip
     */

    private  ArrayList<City> readFile() throws FileHandlerException {
        ArrayList<City> cities=new ArrayList<>();
        try {

            String data;
            //deschidere fisiser
            BufferedReader inFile = new BufferedReader(new FileReader(this.filename));
            Scanner in = new Scanner(inFile);
            //ignorare antet fisier
            inFile.readLine();
            while ((data = inFile.readLine()) != null) {
                //pe baza linii citite obtine un vector se tip String ce contine informatii despre un oras
               String[] strings=data.split( "\t");
               // extrage informatii despre oras
               Integer id=Integer.parseInt( strings[0] );
               String nameCity=strings[1];
               Double lat=Double.parseDouble( strings[2] );
               Double lon=Double.parseDouble( strings[3] );
               String codeCountry=strings[4];
               //creaza un obiect de tip oras si adauga in ArrayList
               City city=new City( nameCity,id,lat,lon,codeCountry);
               cities.add( city );
            }

        }catch (Exception exception){
            //in cazul unei exceptii evenimetul va fi logat si va fi aruncata o exceptie (poate aparea erorii daca tipul fisierului nu are formatul corespunzator)
            LogManager.getLogger(LoggerType.eventLogger).log(Level.ALL, "Error: " + exception.getMessage());
            throw new FileHandlerException("Error parse configure file");
        }
        return cities;
    }
}
