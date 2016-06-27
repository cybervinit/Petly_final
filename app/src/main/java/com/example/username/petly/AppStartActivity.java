package com.example.username.petly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Class: Created at the start of the app.
 * Use: To replace the SplashScreen.
 */
public class AppStartActivity extends AppCompatActivity {
    private boolean mLoggedIn;
        private SharedPreferences mPrefs;
    private final String KEY_USER_REGISTERED = "isRegistered";
    private final int SC_WAIT_TIME = 1000;

    /**
     * Method: Initiates the following:
     * Handler: Waits for SC_WAIT_TIME milliseconds before initiating appropriate activity.
     *
     * @param savedInstanceState: Assumes it is a valid Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        SharedPreferences.Editor editor = mPrefs.edit();

        editor.putBoolean(KEY_USER_REGISTERED, false);
        editor.apply();
        Log.d("Vinit's Log: ", "READCHED HERE");
        mLoggedIn = mPrefs.getBoolean(KEY_USER_REGISTERED, false);
        if (mLoggedIn) {
            startActivity(new Intent(AppStartActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(AppStartActivity.this, LoginActivity.class));
        }
        finish();

    }


}
