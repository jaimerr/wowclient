package jrr.wow.operations;

import android.util.Log;

import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import java.lang.ref.WeakReference;

import jrr.wow.activities.MainActivity;

/**
 * Created by Gurrupatas on 15/07/2015.
 */
public class LinkedInListener implements ApiListener{
    protected final String TAG = "WoW "+getClass().getSimpleName();
    protected WeakReference<WoWOps> mWoW;
    public LinkedInListener(WoWOps mWoWOps){
        mWoW = new WeakReference<>(mWoWOps);
    }
    public void onApiSuccess(ApiResponse apiResponse){
        Log.d(TAG, "JSON Response received"+apiResponse.getResponseDataAsJson().toString());
        mWoW.get().setProfileData(apiResponse.getResponseDataAsJson().toString());
    }

    public void onApiError(LIApiError LIApiError){
        Log.d(TAG, "Error Response received"+LIApiError.toString());
    }
}
