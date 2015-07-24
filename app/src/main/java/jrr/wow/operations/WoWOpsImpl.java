package jrr.wow.operations;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.errors.LIAuthError;

import jrr.wow.activities.MainActivity;
import jrr.wow.aidl.CharacterData;
import jrr.wow.aidl.CharacterRequest;
import jrr.wow.aidl.CharacterResults;
import jrr.wow.services.CharacterServiceAsync;
import jrr.wow.utils.GenericServiceConnection;
import jrr.wow.utils.Utils;
import jrr.wow.R;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.utils.Scope;

/**
 * This class implements all the acronym-related operations defined in
 * the AcronymOps interface.
 */
public class WoWOpsImpl implements WoWOps {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG = "WoW "+getClass().getSimpleName();

    /**
     * Used to enable garbage collection.
     */
    protected WeakReference<MainActivity> mActivity;
    /**
     * Used to enable garbage collection.
     */
    protected LinkedInListener mListener;


    /**
     * User Name
     */
    protected WeakReference<EditText> mEditText;
    /**
     * Class
     */
    protected WeakReference<EditText> mEditTextClass;
    /**
     * Level
     */
    protected WeakReference<EditText> mEditTextLevel;
    /**
     * Bounty
     */
    protected WeakReference<EditText> mEditTextBounty;
    /**
     * Rank
     */
    protected WeakReference<EditText> mEditTextRank;

    /**
     * The LinkedIn session Manager controls the connection to LinkedIn
     *
     */
    private LISessionManager myLISM;

    private String myProfileData;

    /**
    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the WeatherServiceAsync Service using bindService().
     */
    private GenericServiceConnection<CharacterRequest> mServiceConnectionAsync;

    /**
     * Constructor initializes the fields.
     */
    public WoWOpsImpl(MainActivity activity) {
        // Initialize the WeakReference.
        mActivity = new WeakReference<>(activity);
           Log.d(TAG, "Creating WeatherOpsImpl");
        // Finish the initialization steps.
        initializeViewFields();
        initializeNonViewFields();
        mListener = new LinkedInListener(this);
        Log.d(TAG, "View and Nonview fields initialized");
        myProfileData = null;
    }

    /**
     * Initialize the View fields, which are all stored as
     * WeakReferences to enable garbage collection.
     */
    private void initializeViewFields() {
        // Get references to the UI components.
        mActivity.get().setContentView(R.layout.main_activity);

        // Store the EditText that holds the urls entered by the user
        // (if any).
        mEditText = new WeakReference<>
            ((EditText) mActivity.get().findViewById(R.id.Name));
        mEditTextClass = new WeakReference<>
        ((EditText) mActivity.get().findViewById(R.id.Class));
        mEditTextLevel = new WeakReference<>
        ((EditText) mActivity.get().findViewById(R.id.Level));
        mEditTextBounty = new WeakReference<>
        ((EditText) mActivity.get().findViewById(R.id.Bounty));
        mEditTextRank = new WeakReference<>
                ((EditText) mActivity.get().findViewById(R.id.Rank));




    }

    /**
     * (Re)initialize the non-view fields (e.g.,
     * GenericServiceConnection objects).
     */
    private void initializeNonViewFields() {
        mServiceConnectionAsync =
            new GenericServiceConnection<CharacterRequest>(CharacterRequest.class);
    }
        
    /**
     * Initiate the service binding protocol.
     */
    @Override
    public void bindService() {
        Log.d(TAG, "calling bindService()");

        // Launch the Character Bound Service if they aren't already
        // running via a call to bindService(), which binds this
        // activity to the CharacterService* if they aren't already
        // bound.

        if (mServiceConnectionAsync.getInterface() == null) {
        	Log.d(TAG, "Binding asynchronous. Async service connection was null");
            mActivity.get().bindService(CharacterServiceAsync.makeIntent(mActivity.get()),
                                        mServiceConnectionAsync,
                                        Context.BIND_AUTO_CREATE);
        }
    	Log.d(TAG, "Exiting bindService");
    }

    /**
     * Initiate the service unbinding protocol.
     */
    @Override
    public void unbindService() {
    	Log.d(TAG, "Calling unbind");
        // Unbind the Async Service if it is connected.
        if (mServiceConnectionAsync.getInterface() != null)
            mActivity.get().unbindService(mServiceConnectionAsync);

    }

    /**
     * Called by the AcronymOps constructor and after a runtime
     * configuration change occurs to finish the initialization steps.
     */
    public void onConfigurationChange(MainActivity activity) {
    	Log.d(TAG, "Configuration change");
        // Reset the mActivity WeakReference.
        mActivity = new WeakReference<>(activity);

        // (Re)initialize all the View and NonView fields.
        initializeViewFields();
        initializeNonViewFields();
    }

    /*
     * Initiate the asynchronous connection to LinkedIn and download of the character information
     */
    public void connectLinkedIn(View v) {
        boolean connected = false;
    	Log.d(TAG, "Connect to LinkedIn");
        //TODO OAuth connection to LinkedIn
        Scope myScope = Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
        Log.d(TAG, "Scope built");
        if (myLISM == null) {
            myLISM = LISessionManager.getInstance(mActivity.get());
            Log.d(TAG, "LISM created "+myLISM.toString());
        }
        if (myLISM.getSession()!= null){
            Log.d(TAG, "Session exists ");
            if (myLISM.getSession().getAccessToken()!=null){
                connected = true;
                Log.d(TAG, "Already connected. Token available ");
                requestProfile();
            }
        }
        if (!connected) {
            Log.d(TAG, "Not connected. Initializing connection");
            myLISM.init(mActivity.get(), myScope, new AuthListener() {
                @Override
                public void onAuthSuccess() {
                    // Authentication was successful.  You can now do
                    // other calls with the SDK.
                    Log.d("WoW Linkedin", "Connect to LinkedIn Successful");
                    Utils.showToast(mActivity.get(),
                            "LinkedIn Authentication Successful");
                    requestProfile();
                }

                @Override
                public void onAuthError(LIAuthError error) {
                    // Handle authentication errors
                    Log.d("WoW Linkedin", "Connect to LinkedIn Unsuccessful. Error " + error.toString());
                    Utils.showToast(mActivity.get(),
                            "LinkedIn Authentication Unsuccessful");
                    Utils.showToast(mActivity.get(),
                            error.toString());

                }
            }, true);
        }
        Log.d(TAG, "Exited Linkedin Authentication");
    }
    public void requestProfile(){
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,location,industry,positions,specialties,picture-url)";
        APIHelper apiHelper = APIHelper.getInstance(mActivity.get().getApplicationContext());
        apiHelper.getRequest(mActivity.get().getApplicationContext(), url, mListener);
    }
    public void setProfileData(String profile){
        myProfileData = profile;
    }


    // Build the list of member permissions our LinkedIn session requires
   public void requestCharacter(View v){
        // Character Request based on LinkedIn Data
        CharacterRequest characterRequest =
            mServiceConnectionAsync.getInterface();
       Log.d(TAG, "Request character in WoWOps");
        if (characterRequest != null) {
            Log.d(TAG, "Character request was not null");
            Utils.hideKeyboard(mActivity.get(),
                    mEditText.get().getWindowToken());

            try {
                // Invoke a one-way AIDL call, which does not block
                // the client.  The results are returned via the
                // sendResults() method of the mWeatherResults
                // callback object, which runs in a Thread from the
                // Thread pool managed by the Binder framework.
                characterRequest.getCharacterLinkedIn(myProfileData,
                                             mCharacterResults);
                Log.d(TAG, "Interface invoked");
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException:" + e.getMessage());
            }
        } else {
            Log.d(TAG, "characterRequest was null.");
        }
    }

    /*
     * Shares with Whatsapp
     */
    public void shareCharacter(View v) {
    	Log.d(TAG, "Sharing character to whatsapp ");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + mEditText.get().getText().toString() + " Class: " + mEditTextClass.get().getText().toString() + " Level: " + mEditTextLevel.get().getText().toString() + " Bounty: " + mEditTextBounty.get().getText().toString());
        shareIntent.setType("text/plan");
        mActivity.get().startActivity(shareIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "On ActivityResult called in WoWOps");
        // Add this line to your existing onActivityResult() method
        myLISM.onActivityResult(mActivity.get(), requestCode, resultCode, data);
    }

    public void onActivityResultWoW(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "On ActivityResultWoW called in WoWOps");
        // Add this line to your existing onActivityResult() method
        myLISM.onActivityResult(mActivity.get(), requestCode, resultCode, data);
    }


    /**
     * The implementation of the AcronymResults AIDL Interface, which
     * will be passed to the Acronym Web service using the
     * AcronymRequest.expandAcronym() method.
     * 
     * This implementation of AcronymResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private CharacterResults.Stub mCharacterResults = new CharacterResults.Stub() {
            /**
             * This method is invoked by the AcronymServiceAsync to
             * return the results back to the AcronymActivity.
             */
            @Override
            public void sendResults(final CharacterData character)
                throws RemoteException {
                // Since the Android Binder framework dispatches this
                // method in a background Thread we need to explicitly
                // post a runnable containing the results to the UI
                // Thread, where it's displayed.
                mActivity.get().runOnUiThread(new Runnable() {
                        public void run() {
                            displayResults(character);
                        }
                    });
            }

            /**
             * This method is invoked by the AcronymServiceAsync to
             * return error results back to the AcronymActivity.
             */
            @Override
            public void sendError(final String reason)
                throws RemoteException {
                // Since the Android Binder framework dispatches this
                // method in a background Thread we need to explicitly
                // post a runnable containing the results to the UI
                // Thread, where it's displayed.
                mActivity.get().runOnUiThread(new Runnable() {
                        public void run() {
                            Utils.showToast(mActivity.get(),
                                            reason);
                        }
                    });
            }
	};

    /**
     * Display the results to the screen.
     * 
     * @param results
     *            List of Resultes to be displayed.
     */
    private void displayResults(CharacterData results) {
        // Create custom ListView Adapter and fill it with our data.
        Log.d(TAG, "displayResults in WoWOps");
        if (results == null) {
            // Create a local instance of our custom Adapter for our
            // ListView.
        	mEditText.get().setText("No data found");
        	mEditTextClass.get().setText("");
        	mEditTextLevel.get().setText("");
        	mEditTextBounty.get().setText("");
        	mEditTextRank.get().setText("");
        } else {
            // If adapter already existed, then change data set.
        	mEditText.get().setText(results.getName());
        	mEditTextClass.get().setText("Class: "+results.getRaceClass());
        	mEditTextLevel.get().setText("Level: "+results.getLevel());
        	mEditTextBounty.get().setText("Bounty: "+results.getBounty());
        	mEditTextRank.get().setText("Rank: "+results.getRank());
        }
        

    }


}
