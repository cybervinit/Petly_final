package com.example.username.petly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.username.myapplication.R;

import java.util.ArrayList;

/**
 * Class: Created from AppStartActivity.
 * Use: To login to the user's account.
 * Use: To create RegisterAccountActivity.
 */
public class LoginActivity extends AppCompatActivity {
    private final String KEY_USER_REGISTERED = "isRegistered";
    private final String KEY_USER_PASSWORD = "userPassword";
    private final String KEY_USER_USERNAME = "userUserName";
    private final int LOGIN_ACTION = 1;

    private SharedPreferences mPrefs;

    private EditText mUsernameSpace;
    private EditText mPasswordSpace;
    private Button mLoginButton;
    private Button mRegisterButton;


    private String usernameActual;
    private String passwordActual;
    private AccountInfoDocument mAccountsDoc;

    /**
     * Method: Initiates the following:
     * mLoginButton OnClickListener: used to fetch user data via FetchInternetData object
     * to attempt login to user's account.
     * mRegisterButton OnClickListener: used to create RegisterAccountActivity.
     *
     * @param savedInstanceState: Assumes it is a valid non-null Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assignment of components
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mUsernameSpace = (EditText) findViewById(R.id.username_space);
        mPasswordSpace = (EditText) findViewById(R.id.password_space);
        mUsernameSpace.setHint(R.string.username_string);
        mPasswordSpace.setHint(R.string.password_string);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        usernameActual = "";
        passwordActual = "";

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("This one runs", "");
                try {
                    if (!mUsernameSpace.getText().toString().isEmpty() || !mPasswordSpace.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Attempting to Login...", Toast.LENGTH_SHORT).show();
                        mLoginButton.setEnabled(false);
                        new FetchInternetData(LOGIN_ACTION, mUsernameSpace.getText().toString(), new OnAsyncTaskCompleted() {
                            @Override
                            public void onFetchComplete(AccountInfoDocument data) {
                                try {
                                    mAccountsDoc = data;
                                    usernameActual = mAccountsDoc.getUsername();
                                    passwordActual = mAccountsDoc.getPassword();
                                    if (mUsernameSpace.getText().toString().equals(usernameActual) && mPasswordSpace.getText().toString().equals(passwordActual)) {
                                        setLoggedIn(usernameActual, passwordActual);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getBaseContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (NullPointerException npe) {
                                    Toast.makeText(getBaseContext(), "Incorrect credentials", Toast.LENGTH_SHORT).show();
                                }
                                mLoginButton.setEnabled(true);
                            }

                            @Override
                            public void onFetchCompleteGetList(ArrayList<Post> dataList) {
                                //Not Used
                            }

                            @Override
                            public void onPostComplete() {
                                //Not Used
                            }

                            @Override
                            public void onPostComplete(Bitmap bitmapImage) {
                                //Not Used
                            }
                        }).execute();
                    } else {
                        Toast.makeText(LoginActivity.this, "Fill both prompts", Toast.LENGTH_SHORT).show();
                    }



                } catch (NullPointerException e) {
                    Log.d("Vinit's Log: ", e.toString());
                } catch (Exception f) {
                    Log.d("Vinit's Log", f.toString());
                }

            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterAccountActivity.class));
            }
        });
    }


    /**
     * Method: Updates the default sharedpreferences according to the follwing:
     * KEY_USER_USERNAME: sets this value to usernameActual value.
     * KEY_USER_PASSWORD: sets this value to passwordActual value.
     * KEY_USER_REGISTERED: sets this value to true.
     *
     * mPrefs: Assumes this is a non-null SharedPreferences object .
     * @param usernameActual: Assumes it is a non-null string value.
     * @param passwordActual: Assumes it is a non-null string value.
     */
    private void setLoggedIn(String usernameActual, String passwordActual) {
        SharedPreferences.Editor sfEditor = mPrefs.edit();
        sfEditor.putString(KEY_USER_USERNAME, usernameActual);
        sfEditor.putString(KEY_USER_PASSWORD, passwordActual);
        sfEditor.putBoolean(KEY_USER_REGISTERED, true);
        sfEditor.apply();
    }

}