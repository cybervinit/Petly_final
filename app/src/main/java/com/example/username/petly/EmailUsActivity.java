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
import android.widget.EditText;

import com.example.username.myapplication.R;

/**
 * This class handles the Email option within the app. It is responsible for creating a email with user input of
 * subject and message. It also handles the "call us" feature within the app.
 */
public class EmailUsActivity extends AppCompatActivity {


    // Button,Toolbar, and TextField deceleration
    private Button mSendEmail;
    private Toolbar mToolbar;
    private EditText subject;
    private EditText message;

    /**
     * Method: Initiates the following:
     * mCallButton onClickListener: To call Petly's Development Team (Us).
     * @param savedInstanceState: Assumes it is a non-null Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_us);

        //IDing Button and Toolbar
        Button mCallButton = (Button) findViewById(R.id.call_now_button);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSendEmail = (Button) findViewById(R.id.send_email);

        assert mCallButton != null;
        subject=(EditText) findViewById(R.id.SubjectText);
        subject.setHint(R.string.enter_subject_string);
        message=(EditText) findViewById(R.id.MessageText);
        message.setHint(R.string.post_message_hint);


        //set Toolbar options
        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mToolbar.setTitle(R.string.activity_name_email_us);

        mCallButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:6475172457"));


                if (ActivityCompat.checkSelfPermission(EmailUsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

        //OnClick initiates email send
        mSendEmail.setOnClickListener(new View.OnClickListener()


        {

            @Override
            public void onClick(View v) {

                // converts textfield user input into string
                String subjectString=subject.getText().toString();
                String messagestring=message.getText().toString();

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{getString(R.string.contact_email)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,subjectString);
                emailIntent.putExtra(Intent.EXTRA_TEXT,messagestring);
                emailIntent.setType("message/rfc822"); // sets the MIME type of the application chooser (Multipurpose Internet Mail Extensions)
                startActivity(Intent.createChooser(emailIntent,"Email")); //chooses apps within phone which are e-mail associated
            }

        });



    }
}
