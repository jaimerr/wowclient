package jrr.wow.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jrr.wow.json.CharacterJSONParser;
import jrr.wow.json.JsonCharacter;
import jrr.wow.aidl.CharacterData;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import java.net.URLEncoder;

/**
 * @class CharacterDownloadUtils
 *
 * @brief Handles the actual downloading of Weather information from
 *        the Weather web service.
 */
public class Utils {
    /**
     * Logging tag used by the debugger. 
     */
    private final static String TAG = "WoW "+Utils.class.getCanonicalName();

    /** 
     * URL to the Weather web service.
     */
    private final static String sCharacter_Web_Service_URL =
        "http://52.16.152.191:8080/character/?LIProfile=";

    /**
     * Obtain the Character information.
     * 
     * @return The information that responds to the Current LinkedIn Profile.
     */
    public static CharacterData getResults(final String profileData) {
        // Create an object that will return the WeatherData obtained
        // from the Weather Service web service.
        final CharacterData returnData;
            
        // The JsonWeather object.
        JsonCharacter jsonCharacter = null;

        Log.d(TAG,
              "GetResults entered");
        try {
            // Append the location to create the full URL.
            final URL url =
                new URL(sCharacter_Web_Service_URL
                        +URLEncoder.encode(url,"UTF-8"));
            Log.d(TAG,
                    "URL to be entered: "+URLEncoder.encode(url,"UTF-8"));
            // Opens a connection to the Acronym Service.
            HttpURLConnection urlConnection =
                (HttpURLConnection) url.openConnection();
            Log.d(TAG,
                    "Connection Opened");        
            // Sends the GET request and reads the Json results.
            try (InputStream in =
                 new BufferedInputStream(urlConnection.getInputStream())) {
                Log.d(TAG,
                        "Creating the Parser");   
                 // Create the parser.
                 final CharacterJSONParser parser =
                     new CharacterJSONParser();

                // Parse the Json results and create JsonCharacter data
                // objects.
                jsonCharacter = parser.parseJsonStreamCharacter(in);
                Log.d(TAG,
                        "Jsonparsed "+jsonCharacter);
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.d(TAG,
                    "Exception in communication with webservices "+e); 
            e.printStackTrace();
        }

        if (jsonCharacter != null ) {
            /* Convert the JsonAcronym data objects to our CharacterData
                 public CharacterData(String name,
                       String raceclass,
                       double level,
                       double bounty, double rank)

           object, which can be passed between processes.*/
            Log.d(TAG,
                    "Creating character data with info");
        returnData = new CharacterData(jsonCharacter.getName(),
                		jsonCharacter.getRaceClass(),
                		jsonCharacter.getLevel(),
                		jsonCharacter.getBounty(),
                		jsonCharacter.getRank()
                		);
             // Return the CharacterData register
             return returnData;
        }  else
            Log.d(TAG,
                    "Returning null");   
            return null;
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public static void hideKeyboard(Activity activity,
                                    IBinder windowToken) {
        InputMethodManager mgr =
           (InputMethodManager) activity.getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                                    0);
    }

    /**
     * Show a toast message.
     */
    public static void showToast(Context context,
                                 String message) {
        Toast.makeText(context,
                       message,
                       Toast.LENGTH_SHORT).show();
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private Utils() {
        throw new AssertionError();
    } 
}
