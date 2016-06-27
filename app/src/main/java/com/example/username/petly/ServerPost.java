package com.example.username.petly;

import android.os.AsyncTask;
import android.util.Log;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

/**
 * Created by 322073339 on 24/05/2016.
 */
public class ServerPost extends AsyncTask<Void, Void, Void>{
    private String meetupName;
    private double latitude;
    private double longitude;

    public ServerPost(String meetupName, double latitude, double longitude) {
        meetupName.replace(" ", "");
        meetupName.replace("'", "");
        this.meetupName = meetupName;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    protected Void doInBackground(Void... params) {
        CloudantClient client = ClientBuilder.account("mypetapp")
                .username("mypetapp")
                .password("ics4up01")
                .build();

        Database meetupDB = client.database("meetups_db", true);

        try {
            MeetUpDocument md = new MeetUpDocument(meetupName, latitude, longitude);
            meetupDB.save(md);
        } catch (Exception e) {
            Log.d("Sunny's Log", e.getMessage());
        }




        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
