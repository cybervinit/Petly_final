package com.example.username.petly;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Class: Associated with AccountInfoDocument objects used to transfer Account information to
 * and from Cloudant server to application.
 */
public class AccountInfoDocument {
    String _id;
    String _rev;

    private static final int USERNAME_LOCATION = 0;
    private static final int PASSWORD_LOCATION = 1;
    private static final int PETNAME_LOCATION = 2;
    private static final int DISPLAY_PICTURE_LOCATION = 3;
    private static final int PET_DISPLAY_PICTURE_LOCATION = 4;
    private ArrayList<String> userInfo;
    private ArrayList<String> friends;


    /**
     * Constructor: Instantiates the AccountInfoDocument object.
     * @param mUserInfo: Assumes it is a non-null ArrayList of String objects containing information
     *                 as follows:
     *                 mUserInfo[0]: User's username
     *                 mUserInfo[1]: User's password
     *                 mUserInfo[2]: User's pet's name
     *                 mUserInfo[3]: User's Display Picture in string format (Base64 encoded).
     */
    public AccountInfoDocument(ArrayList<String> mUserInfo) {
        this.userInfo = mUserInfo;
        this._id = mUserInfo.get(0);
        friends = new ArrayList<>(0);
    }

    /**
     * Method: Used to add a friend name to the ArrayList of friends.
     * @param friendsID: Assumes it is a non-null String value.
     */
    public void addFriend(String friendsID) {
        try {
            friends.add(friends.size(), friendsID);
        } catch (NullPointerException f) {
            friends = new ArrayList<>(0);
            friends.add(friends.size(), friendsID);
        }
    }

    /**
     * Method: Used to set the user's DP to another string value.
     * @param newDPStringFormat: Assumes it is a picture formatted in non-null String object
     *                         (encoded in Base64).
     */
    public void changeDP(String newDPStringFormat) {
        userInfo.set(DISPLAY_PICTURE_LOCATION, newDPStringFormat);
    }

    /**
     * Method: Used to set the user's DP to another string value.
     * @param newDPStringFormat: Assumes it is a picture formatted in non-null String object
     *                         (encoded in Base64).
     */
    public void changePetDP(String newDPStringFormat) {
        Log.d("Vinit's LOG", "AT AID");
        try {
            userInfo.get(PET_DISPLAY_PICTURE_LOCATION);
            userInfo.set(PET_DISPLAY_PICTURE_LOCATION, newDPStringFormat);
        } catch (NullPointerException e) {
            userInfo.add(PET_DISPLAY_PICTURE_LOCATION, newDPStringFormat);
        }

    }

    /**
     * Method: Used to fetch the user's username.
     * @return: User's username as a String.
     */
    public String getUsername() {
        return userInfo.get(USERNAME_LOCATION);
    }

    /**
     * Method: Used to fetch the user's password.
     * @return: User's password as a String.
     */
    public String getPassword() {
        return userInfo.get(PASSWORD_LOCATION);
    }


    /**
     * Method: Used to get the user's friends in ArrayList format.
     * @return: ArrayList of String Objects.
     */
    public ArrayList<String> getFriends() {
        return friends;
    }

    public String getPetName() {
        return userInfo.get(PETNAME_LOCATION);
    }

    /**
     * Method: Used to get the user's DP in string format (Encoded in Base64).
     * @return: User's DP in String format (Encoded in Base64).
     */
    public String getDisplayPic() {
        return userInfo.get(3);
    }

    /**
     * Method: Used to get the user's pet's DP in string format (Encoded in Base64).
     * @return: User's pet's DP in String format (Encoded in Base64).
     */
    public String getPetDisplayPic() {
        return userInfo.get(4);
    }


}
