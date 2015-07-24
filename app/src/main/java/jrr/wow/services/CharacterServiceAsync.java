package jrr.wow.services;

import jrr.wow.aidl.CharacterData;
import jrr.wow.aidl.CharacterRequest;
import jrr.wow.aidl.CharacterResults;
import jrr.wow.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @class CharacterServiceAsync
 * 
 * @brief Reused from AcronymAsync
 */
public class CharacterServiceAsync extends LifecycleLoggingService {
    /**
     * Factory method that makes an Intent used to start the
     * CharacterServiceAsync when passed to bindService().
     * 
     * @param context
     *            The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                          CharacterServiceAsync.class);
    }

    /**
     * Called when a client (e.g., MainActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of CharacterRequest, which is implicitly cast as
     * an IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mCharacterRequestImpl;
    }

    /**
     * The concrete implementation of the AIDL Interface
     * CharacterRequest, which extends the Stub class that implements
     * CharacterRequest, thereby allowing Android to handle calls across
     * process boundaries.  This method runs in a separate Thread as
     * part of the Android Binder framework.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    CharacterRequest.Stub mCharacterRequestImpl = new CharacterRequest.Stub() {
            /**
             * Implement the AIDL CharacterRequest
             *      oneway void getCharacterLinkedIn(in String Name,
             in CharacterResults results);;
             */
    	
            @Override
            public void getCharacterLinkedIn(String mProfileData,
                                      CharacterResults results)
                throws RemoteException {

                // Call the Character Web service to get the character description for the LinkedIn Profile.
                //Temporalily dummy character is returned until the Character Web Service is only
               CharacterData character = Utils.getResults(mProfileData);

                //character = new CharacterData("Test Subject", "Fairy Queen", 80, 1000000,1);
                // Invoke a one-way callback to send the CharacterData
                // expansions back to the Main Activity.
                if (character != null) {
                    Log.d(TAG, "" 
                          + mProfileData + " character is  "
                          + character);
                    results.sendResults(character);
                } else
                    results.sendError("Didn't find linkedin character ");
            }
	};
}
