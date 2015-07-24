package jrr.wow.aidl;

import jrr.wow.aidl.CharacterResults;

/**
 * Interface defining the method implemented within
 * WoWServiceAsync that provides asynchronous access to the
 * Character Translator Service web service.
 */
interface CharacterRequest {
   /**
    * A one-way (non-blocking) call to the WeatherServiceAsync that
    * retrieves information about the current weather from the Weather
    * Service web service.  WeatherServiceAsync subsequently uses the
    * WeatherResults parameter to return a List of WeatherData
    * containing the results from the Weather Service web service back
    * to the WeatherActivity via the one-way sendResults() method.
    */
    oneway void getCharacterLinkedIn(in String Name,
                                  in CharacterResults results);
}
