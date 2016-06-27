package com.example.username.petly;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;

import com.example.username.myapplication.R;

/**
 * This class allows the user to create (redirect to another class), and lists all their notifications that have been set.
 */
public class NotificationActivity extends AppCompatActivity {


    private ArrayAdapter<String> adapter;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;

    @Override
    /**
     * This method initializes the title, toolbar, list view which contains the list of notifications, and the floating action button which
     * allows the user to create a notification by starting a new activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notifications");
        setContentView(R.layout.activity_notification);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NotificationHolder.notificationDetails);
        assert listView != null;
        listView.setAdapter(adapter);

        mFab = (FloatingActionButton) findViewById(R.id.add_alarm_fab);
        mFab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#228B22")));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), PetManagementActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


}
