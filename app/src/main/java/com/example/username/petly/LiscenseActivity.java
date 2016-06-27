package com.example.username.petly;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.username.myapplication.R;


/**
 * This class provides information about the licensing agreement, and allows the user to call Paws to licence their pet.
 */
public class LiscenseActivity extends AppCompatActivity {

    //Toolbar and callButton deceleration
    private Toolbar mToolbar;
    private Button mCallButton;


    /**
     * Method: Initiates the following:
     * mCallButton onClickListener
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liscense);

        //IDing call button and Toolbar
        mCallButton = (Button) findViewById(R.id.button3);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        //licensing agreement toolbar initiation
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        mToolbar.setTitle(R.string.activity_name_liscense);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mCallButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:6475172457"));


                if (ActivityCompat.checkSelfPermission(LiscenseActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);



            }
        });
    }


}
