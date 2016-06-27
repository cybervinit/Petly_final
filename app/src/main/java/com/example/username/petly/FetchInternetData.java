package com.example.username.petly;

import android.os.AsyncTask;
import android.util.Log;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.NoDocumentException;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class: Used to fetch data from the databases of the Cloudant server.
 */
public class FetchInternetData extends AsyncTask<String, Void, AccountInfoDocument> {
    private final String ACCOUNTS_DB = "accounts_db";
    private final String FEED_DB = "feed_db";

    private AccountInfoDocument accountDocument;
    private String docID;
    private OnAsyncTaskCompleted afterTaskCompleted;
    private int actionType;

    private final int GET_ACCOUNT_INFO_ACTION = 1;
    private final int FIND_FRIENDS_ACTION = 2;
    private final int REFRESH_ACTION = 3;
    private ArrayList<Post> newPosts; // Replace DataType with "Post" datatype that you will make in future.
    private String requiredUsername;


    /**
     * Constructor: Instantiates the FetchInternetData AsyncTask object.
     * @param actionType: Assumes it is a non-null int data-type.
     * @param docID: Assumes it is a non-null String object.
     * @param task: Assumes it is a non-null OnAsyncTaskCompleted object.
     */
    public FetchInternetData(int actionType, String docID, OnAsyncTaskCompleted task) {
        this.docID = docID;
        this.requiredUsername = docID;
        this.actionType = actionType;
        afterTaskCompleted = task;

    }

    /**
     * Method: AsyncTask of the FetchInternetData (Probably to be executed).
     * @param strings: Assumes it is a non-null String object.
     * @return: Probably an AccountInfoDocument (or null depending on if object found on Cloudant server.)
     */
    @Override
    protected AccountInfoDocument doInBackground(String... strings) {
        String url = "https://mypetapp.cloudant.com/";
        try {
            CloudantClient client = ClientBuilder.url(new URL(url))
                    .username("mypetapp")
                    .password("ics4up01")
                    .build();
            Database accountsDB = client.database(ACCOUNTS_DB, true);
            Database feedDB = client.database(FEED_DB, true);


            if (actionType == GET_ACCOUNT_INFO_ACTION) { // Get credentials to log in (1)
                accountDocument = accountsDB.find(AccountInfoDocument.class, docID);
                Log.d("VINIT'S LOG", accountDocument.getUsername());
                return accountDocument;
            } else if (actionType == FIND_FRIENDS_ACTION) { // Get friends with name (2)
                try {
                    accountDocument = accountsDB.find(AccountInfoDocument.class, requiredUsername);
                    Log.d("VINIT'S LOG", "Friend fetch successful");

                    return accountDocument;
                } catch (NoDocumentException l){
                    accountDocument = null;
                    return null;
                }
            } else if (actionType == REFRESH_ACTION) { // Refresh server (3)
                newPosts = new ArrayList<>(0);
                accountDocument = accountsDB.find(AccountInfoDocument.class, docID);
                ArrayList<String> friends;
                try {
                    friends = accountDocument.getFriends();
                    for (int i = 0; i < friends.size(); i++) {
                        FeedPostDocument fpd = feedDB.find(FeedPostDocument.class, friends.get(i));
                        ArrayList<Post> eachFriendsposts = fpd.getPosts();
                        for (int k = 0; k < eachFriendsposts.size(); k++) {
                            newPosts.add(newPosts.size(), eachFriendsposts.get(k));
                        }
                    }
                } catch (NullPointerException e) {
                    Log.d("Vinit's Log", "User has no friends yet.");
                }
                Log.d("Vinit's LOG --> ", "BEFORE FINDING DOC ON CLOUDANT");
                FeedPostDocument userPostsDocument = feedDB.find(FeedPostDocument.class, accountDocument.getUsername());
                Log.d("Vinit's LOG --> ", "AFTER FINDING DOC ON CLOUDANT");
                for (int i = 0; i < userPostsDocument.getPosts().size(); i++) {
                    newPosts.add(newPosts.size(), userPostsDocument.getPosts().get(i));
                }
                Log.d("VINIT'S LOG", "Refreshed list. fetch Successful");
            }


            return accountDocument;

        } catch (IOException e) {
            Log.d("Vinit's Error Message: ", "I/O exception at fetching data");
            return null;
        } catch (Exception e) {
            Log.d("Vinit's Fetch Error: ", e.toString());
            return null;
        }


    }

    /**
     * Method: Used to run the appropriate OnAsyncTaskCompleted object required after AsyncTask completed.
     * @param aDocument: Assumes it is a non-null AccountInfoDocument object.
     */
    @Override
    protected void onPostExecute(AccountInfoDocument aDocument) {
        if (actionType == 1 || actionType == 2) {
            try {
                accountDocument.getUsername(); // Testing if null
                afterTaskCompleted.onFetchComplete(accountDocument);
                //Log.d("Vinit's Log", aDocument.getUsername());
            } catch (NullPointerException e) {
                Log.d("vinit's TAG: ", "User Doesn't exist");
                afterTaskCompleted.onFetchComplete(accountDocument);
            }
        } else if (actionType == 3) {
            Collections.sort(newPosts);
            afterTaskCompleted.onFetchCompleteGetList(newPosts);
        }
    }
}
