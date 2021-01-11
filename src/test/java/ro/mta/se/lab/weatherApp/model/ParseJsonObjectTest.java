package ro.mta.se.lab.weatherApp.model;

import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.mta.se.lab.weatherApp.Exceptions.FileHandlerException;

import java.io.*;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class ParseJsonObjectTest {

    String content;
    ArrayList<String> expectedResult=new ArrayList<>();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * citire fisier ce contine un obiect JSON ce va fi utilizat pentru test
     * @return continut fisier
     */
    private String readTestContent(){
        StringBuilder content=new StringBuilder();
        BufferedReader in;
        try {

            InputStream inputStream= new FileInputStream("src/main/resources/test.json"    );
            in = new BufferedReader(
                    new InputStreamReader(inputStream));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * functie ce initializeaza obiectele folosite pentru testare
     */
    @Before
    public void setUp(){
        this.content=this.readTestContent();

        this.expectedResult.add( "Bucharest" );
        this.expectedResult.add( "5.75" );
        this.expectedResult.add( "01n" );
        this.expectedResult.add( "clear sky" );
        this.expectedResult.add( "3.09" );
        this.expectedResult.add( "0" );
        this.expectedResult.add( "1016" );
        this.expectedResult.add( "81" );
        this.expectedResult.add( "1610045976" );
        this.expectedResult.add( "200" );

    }

    /**
     * "dezalocare" obiecte
     */
    @After
    public void tearDown(){
        this.content=null;
        this.expectedResult=null ;
        this.exception=null ;
    }

    /**
     * testare daca parsarea cu un input corect  este ok
     */
    @Test
    public void testParseJsonObject(){
        IParseObject parseJsonObject=new ParseJsonObject( this.content );
        try {
            Assert.assertEquals( expectedResult, parseJsonObject.parse() );
        } catch (FileHandlerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidExpectedResultParseJsonObject(){
        IParseObject parseJsonObject=new ParseJsonObject( this.content );
        try {
            expectedResult.add( "123456789" );
            assertThat(expectedResult,is(not(parseJsonObject.parse())));
        } catch (FileHandlerException e) {
            e.printStackTrace();
        }
    }

    /**
     * testare daca parsarea cu un input incorect  va arunca o exceptie
     */
    @Test
    public void testInvalidContentParseJsonObject() throws FileHandlerException {
        exception.expect( FileHandlerException.class );
        exception.expectMessage( "Error parse Json Object." );

        this.content="";
        IParseObject parseJsonObject=new ParseJsonObject( this.content );
        parseJsonObject.parse();

    }


}
