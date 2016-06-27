package com.example.username.petly;

/**
 * Created by 322073339 on 24/05/2016.
 */
public class MeetUpDocument {
    private String meetupName;
    private double latitude;
    private double longitude;

    public MeetUpDocument(String meetupName, double latitude, double longitude) {
        this.meetupName = meetupName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
