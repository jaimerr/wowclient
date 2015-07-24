package jrr.wow.aidl;

import jrr.wow.aidl.CharacterData;
import java.util.List;

/**
 * Interface defining the method that receives callbacks from the
 * WoWServiceAsync.  This method should be implemented by the
 * MainActivity.
 */
interface CharacterResults {
    /**
     * This one-way (non-blocking) method allows WeatherServiceAsync
     * to return the List of WeatherData results associated with a
     * one-way WeatherRequest.getCurrentWeather() call.
     */
    oneway void sendResults(in CharacterData results);
    oneway void sendError(in String error);
}
