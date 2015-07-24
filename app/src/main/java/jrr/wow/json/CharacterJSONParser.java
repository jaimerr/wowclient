package jrr.wow.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

/**
 * Parses the Json acronym data returned from the Acronym Services API
 * and returns a List of JsonAcronym objects that contain this data.
 */
public class CharacterJSONParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG = "WoW"+
        this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonAcronym
     * objects.
     */
    public JsonCharacter parseJsonStreamCharacter(InputStream inputStream){

        // Create a JsonReader for the inputStream.
        try (JsonReader reader =
             new JsonReader(new InputStreamReader(inputStream,
                                                  "UTF-8"))) {
            Log.d(TAG, "Parsing the results");
            Log.d(TAG, "JsonReader info "+reader);
            // Handle the array returned from the Acronym Service.
            return parseCharacterServiceResults(reader);
        } catch (Exception e){
        	Log.d(TAG, "Parsing exception "+e);
        	return null;
        }
    }

    /**
     * Parse a Json stream and convert it into a List of JsonAcronym
     * objects.
     */
    public JsonCharacter parseCharacterServiceResults(JsonReader reader)
        throws IOException {
        Log.d(TAG, "Entering parseCharacterServiceResults");


      
            // If the query didn't return anything return null;
            if (reader.peek() == JsonToken.END_DOCUMENT) {
                Log.d(TAG, "Returning null, no information");
                return null; }
            else {
            // Create a JsonAcronym object for each element in the
            // Json array.
                reader.beginObject();
                JsonCharacter character = new JsonCharacter();
                while (reader.hasNext() ){
                    String name = reader.nextName();
                    Log.d(TAG, "Read :'"+name+"'");
                    switch (name){
                        case JsonCharacter.name_JSON:
                            character.setName(reader.nextString());
                            Log.d(TAG, "reading name " + character.getName());
                            break;
                        case JsonCharacter.class_JSON:
                            character.setRaceClass (reader.nextString());
                            Log.d(TAG, "reading name " + character.getRaceClass());
                            break;
                        case JsonCharacter.level_JSON:
                            character.setLevel(reader.nextDouble());
                            Log.d(TAG, "reading level " + character.getLevel());
                            break;
                        case JsonCharacter.bounty_JSON:
                            character.setBounty(reader.nextDouble());
                            Log.d(TAG, "reading bounty " + character.getBounty());
                            break;
                        case JsonCharacter.rank_JSON:
                            character.setRank(reader.nextDouble());
                            Log.d(TAG, "reading rank " + character.getRank());
                            break;
                        default:
                            reader.skipValue();
                            Log.d(TAG, "ignoring " + name);
                            break;
                    }
                    Log.d(TAG, "End of character");
                }
            return character;
            }

        
    }





}
