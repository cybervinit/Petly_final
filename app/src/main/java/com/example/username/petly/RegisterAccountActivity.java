package com.example.username.petly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.username.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Class: Created from LoginActivity
 * Use: To create a new user account
 */
public class RegisterAccountActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_PET_IMAGE = 2;
    private SharedPreferences mPrefs;
    private final String KEY_USER_REGISTERED = "isRegistered";

    private static final int NEW_ACCOUNT_ACTION = 1;
    private static final int FETCH_ACCOUNT_ACTION = 1;

    private Toolbar mToolbar;
    //EditText fields for making new account.
    private EditText new_username;
    private EditText new_password;
    private EditText new_petname;
    private Button mRegisterButton;
    private CircleImageView mNewDP;
    private CircleImageView mNewPetDP;

    private String mNewDPstringFormat;
    private String mNewPetDPStringFormat;

    private ArrayList<String> userInfo;
    private final int USERNAME_KEY = 0;
    private final int PASSWORD_KEY = 1;

    /**
     * Method: Initiates the following:
     * mNewDPButton OnClickListener: used to set a new user display picture while
     * creating a new account.
     * mRegisterButton OnClickListener: used to verify username is unique and later, makeAccount().
     *
     * @param savedInstanceState: Assumes it is a valid Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Assignment of components
        mRegisterButton = (Button) findViewById(R.id.register_button);
        new_username = (EditText) findViewById(R.id.new_username);
        new_password = (EditText) findViewById(R.id.new_password);
        new_petname = (EditText) findViewById(R.id.new_pet_name);
        new_username.setHint(R.string.new_username_string);
        new_password.setHint(R.string.new_password_string);
        new_petname.setHint(R.string.new_petname_string);
        userInfo = new ArrayList<>();
        mNewDP = (CircleImageView) findViewById(R.id.new_DP_imageView);
        mNewPetDP = (CircleImageView) findViewById(R.id.new_pet_DP_imageView);
        mNewDPstringFormat = "";

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNewDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE);
            }
        });

        mNewPetDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_PET_IMAGE);
            }
        });



        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsFilled()) {
                    final String usernameToGo = String.valueOf(new_username.getText().toString());
                    final String passwordToGo = String.valueOf(new_password.getText().toString());
                    final String petnameToGo = String.valueOf(new_petname.getText().toString());


                    mRegisterButton.setEnabled(false);
                    new FetchInternetData(FETCH_ACCOUNT_ACTION, usernameToGo, new OnAsyncTaskCompleted() {
                        @Override
                        public void onPostComplete() {
                            //Not Used
                        }

                        @Override
                        public void onPostComplete(Bitmap bitmapImage) {
                            //Not Used
                        }

                        @Override
                        public void onFetchComplete(AccountInfoDocument data) {
                            try {
                                mRegisterButton.setEnabled(true);
                                data.getUsername();
                                Toast.makeText(getBaseContext(), "Username exists, choose another", Toast.LENGTH_SHORT).show();

                            } catch (NullPointerException e) {
                                if (credentialsSecure(usernameToGo, passwordToGo)) {
                                    userInfo.add(usernameToGo);
                                    userInfo.add(passwordToGo);
                                    userInfo.add(petnameToGo);
                                    userInfo.add(mNewDPstringFormat);
                                    userInfo.add(mNewPetDPStringFormat);
                                    makeAccount();
                                } else {
                                    mRegisterButton.setEnabled(true);
                                }

                            }

                        }

                        @Override
                        public void onFetchCompleteGetList(ArrayList<Post> dataList) {
                            //Not Used
                        }
                    }).execute();


                } else {
                    Toast.makeText(getBaseContext(), "Required fields empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean credentialsSecure(String usernameToGo, String passwordToGo) {
        final Animation shakeEdittextForward = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake_edittext_forward);

        if (usernameToGo.length() < 5) {
            Toast.makeText(getBaseContext(), "Should be atleast 5 characters", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 5; i++) {
                new_username.startAnimation(shakeEdittextForward);
            }
            return false;
        }
        if (passwordToGo.length() < 8) {
            Toast.makeText(getBaseContext(), "Password should be atleast 8 characters", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 5; i++) {
                new_password.startAnimation(shakeEdittextForward);
            }
            return false;
        }
        return true;
    }

    /**
     * Method: Used to make a new user account on Cloudant server via PostInternetData object.
     *
     * userInfo: Assumes this array is appropriately populated with non-null objects as follows:
     * userInfo[0]: Assumes this is the user's username in String format.
     * userInfo[1]: Assumes this is the user's password in String format.
     * userInfo[2]: Assumes this is the user's pet's name in String format.
     * userInfo[3]: Assumes this is the user's display picture in String format (encoded in Base64).
     */
    private void makeAccount() {
        if (infoIsValid(userInfo.get(USERNAME_KEY)) && infoIsValid(userInfo.get(PASSWORD_KEY))) {
            Log.d("<<MAKE ACCOUNT>> ", userInfo.size() + "");
            new PostInternetData(NEW_ACCOUNT_ACTION, userInfo).execute();

            SharedPreferences.Editor sfEditor = mPrefs.edit();
            sfEditor.putBoolean(KEY_USER_REGISTERED, true);
            sfEditor.apply();
            finish();
            onBackPressed();
        } else {
            //Make alert dialog
            Toast.makeText(this, R.string.invalid_text_entry_text, Toast.LENGTH_SHORT).show();
        }


    }


    /**
     * Method: Used to check text validity according to alphanumericChecker().
     * @param text: Assumes this a non-null String object
     * @return boolean according to if "text" is valid or not.
     */
    private boolean infoIsValid(String text) {
        String letter = "";
        for (int i = 0; i < text.length(); i++) {
            letter = text.substring(i, i + 1);
            if (!alphanumericChecker(letter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method: Used to check if given letter is an alphabet or a number.
     * @param x: Assumes it is a String object of length 1.
     * @return true if letter is alphabet or number. false if other character.
     */
    private boolean alphanumericChecker(String x) {
        String valid = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm123456789";
        for (int k = 0; k < valid.length(); k++) {
            if (x.equals(valid.substring(k, k + 1))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method: Used to access photos on the user's phone.
     * @param requestCode: Assumes this is a valid, non-null int value.
     * @param resultCode: Assumes this is a valid, non-null int value.
     * @param intent: Assumes this is a valid, non-null Intent object.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != intent) {
            try {
                Uri image = intent.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(image, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bmImage = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bmImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                byte[] picInBytes = outputStream.toByteArray();
                mNewDP.setImageBitmap(BitmapFactory.decodeByteArray(picInBytes, 0, picInBytes.length));
                mNewDPstringFormat = Base64.encodeToString(picInBytes, Base64.DEFAULT);
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Unable to set picture", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == RESULT_LOAD_PET_IMAGE && resultCode == RESULT_OK && null != intent) {
            try {
                Uri image = intent.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(image, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bmImage = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bmImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                byte[] picInBytes = outputStream.toByteArray();
                mNewPetDP.setImageBitmap(BitmapFactory.decodeByteArray(picInBytes, 0, picInBytes.length));
                mNewPetDPStringFormat = Base64.encodeToString(picInBytes, Base64.DEFAULT);
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Unable to set picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Method: Used to ensure that all fields are not empty at the end of registering.
     * @return true if all fields are filled up.
     */
    private boolean fieldsFilled() {
        return (!new_username.getText().toString().equals("")) && (!new_password.getText().toString().equals("")) && (!new_petname.getText().toString().equals(""));
    }

}
