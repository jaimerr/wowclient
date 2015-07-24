package jrr.wow.operations;

import jrr.wow.activities.MainActivity;

import android.content.Intent;
import android.view.View;

/**
 * This class defines all the acronym-related operations.
 */
public interface WoWOps {
    /**
     * Initiate the service binding protocol.
     */
    public void bindService();

    /**
     * Initiate the service unbinding protocol.
     */
    public void unbindService();
    public void connectLinkedIn(View v);
    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void requestCharacter(View v);
    public void setProfileData(String profile);
    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    public void shareCharacter(View v);
    public void onActivityResultWoW(int requestCode, int resultCode, Intent data);
    /**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    public void onConfigurationChange(MainActivity activity);
}
