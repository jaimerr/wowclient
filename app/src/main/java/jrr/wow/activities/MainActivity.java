package jrr.wow.activities;

import jrr.wow.operations.WoWOps;
import jrr.wow.operations.WoWOpsImpl;
import jrr.wow.utils.RetainedFragmentManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

/**
 * The main Activity reuses the Acronym main activity for the Weather  Extends
 * LifecycleLoggingActivity so its lifecycle hook methods are logged
 * automatically.  This implementation uses the
 * RetainedFragmentManager class to handle runtime reconfigurations
 * robustly.  As a result, MainActivity plays the role of the
 * "Caretaker" in the Memento pattern.
 */
public class MainActivity extends LifecycleLoggingActivity {
    /**
     * Used to retain the WeatherOps state between runtime configuration
     * changes.
     */
    protected final RetainedFragmentManager mRetainedFragmentManager =
        new RetainedFragmentManager(this.getFragmentManager(),
                                    TAG);

    /**
     * Provides acronym-related operations.
     */
    private WoWOps mWoWOps;

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., runtime
     * configuration changes.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);

        // Handle any configuration change.
        handleConfigurationChanges();
        Log.d(TAG, "On create");
        try {
            PackageInfo info = getPackageManager().getPackageInfo("jrr.wow", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG+"KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.d(TAG + "KeyHash error:", e.toString());
        }
    }

    /**
     * Hook method called after onCreate() or after onRestart() (when
     * the activity is being restarted from stopped state).  
     */	
    @Override
    protected void onStart(){
        // Always call super class for necessary
        // initialization/implementation.
        super.onStart();

        // Initiate the service binding protocol.
        mWoWOps.bindService();
    }

    /**
     * Hook method called by Android when this Activity becomes
     * invisible.
     */
    @Override
    protected void onStop() {
        // Unbind from the Service.
        mWoWOps.unbindService();

        // Always call super class for necessary operations when
        // stopping.
        super.onStop();
    }

    /**
     * Handle hardware reconfigurations, such as rotating the display.
     */
    protected void handleConfigurationChanges() {
        // If this method returns true then this is the first time the
        // Activity has been created.
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG,
                  "First time onCreate() call");

            // Create the AcronymOps object one time.
            mWoWOps = new WoWOpsImpl(this);

            // Store the AcronymOps into the RetainedFragmentManager.
            mRetainedFragmentManager.put("WOW_OPS_STATE",
                                         mWoWOps);
            
        } else {
            // The RetainedFragmentManager was previously initialized,
            // which means that a runtime configuration change
            // occured.

            Log.d(TAG,
                  "Second or subsequent onCreate() call");

            // Obtain the AcronymOps object from the
            // RetainedFragmentManager.
            mWoWOps =
                mRetainedFragmentManager.get("WOW_OPS_STATE");

            // This check shouldn't be necessary under normal
            // circumtances, but it's better to lose state than to
            // crash!
            if (mWoWOps == null) {
                // Create the AcronymOps object one time.
                mWoWOps = new WoWOpsImpl(this);

                // Store the AcronymOps into the
                // RetainedFragmentManager.
                mRetainedFragmentManager.put("WOW_OPS_STATE",
                        mWoWOps);
            } else 
                // Inform it that the runtime configuration change has
                // completed.
                mWoWOps.onConfigurationChange(this);
        }
    }

    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void shareCharacter(View v) {
    	Log.d(TAG, "Share your Linked In Character");
        mWoWOps.shareCharacter(v);
    }

    public void requestCharacter(View v) {
        Log.d(TAG, "Request your Linked In Character - Main Activity");
        mWoWOps.requestCharacter(v);
    }

    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    public void connectLinkedIn(View v) {
    	Log.d(TAG, "Connect to Linked In");
        mWoWOps.connectLinkedIn(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "On ActivityResult called");
        // Add this line to your existing onActivityResult() method
        mWoWOps.onActivityResultWoW(requestCode, resultCode, data);
    }
}
