package com.example.username.petly;

import android.os.AsyncTask;
import android.util.Log;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class PostInternetData extends AsyncTask<Void, Void, Void>{
    private String currentUserID;
    private OnAsyncTaskCompleted afterTaskCompleted;
    private int actionType;

    private ArrayList<String> mUserInfo;

    private final String FEED_DB = "feed_db";
    private final String dbURL = "https://mypetapp.cloudant.com/";
    private final String ACCOUNTS_DB = "accounts_db";


    private Post newPost;
    private String friendID;

    private String newDisplayPictureStringFormat;

    /**
     * Constructor: Instantiates the PostInternetData task (Usually when a new account needs to be made).
     * @param actionType: Assumes it is a non-null int data-type.
     * @param userInfo: Assumes it is a non-null ArrayList<String> with non-null String objects.
     */
    public PostInternetData(int actionType, ArrayList<String> userInfo) {
        this.mUserInfo = userInfo;
        this.actionType = actionType;
        Log.d("Vinit's SIZE IN CONS ", mUserInfo.size()+"");
    }

    /**
     * Constructor: Instantiates the PostInternetData task (Usually when a new post needs to be made).
     * @param actionType: Assumes it is a non-null int data-type.
     * @param currentUserID: Assumes it is a non-null String object.
     * @param newPost: Assumes it is a non-null Post object.
     * @param task: Assumes it is a non-null OnAsyncTaskCompleted object.
     */
    public PostInternetData(int actionType, String currentUserID, Post newPost, OnAsyncTaskCompleted task) {
        this.currentUserID = currentUserID;
        this.actionType = actionType;
        this.newPost = newPost;
        this.afterTaskCompleted = task;
    }

    /**
     * Constructor: Instantiates the PostInternetData task (Usually to add a new friend or update DP).
     * @param actionType: Assumes it is a non-null int data-type.
     * @param newData: Assumes it is a non-null String object.
     * @param currentUser: Assumes it is a non-null String object.
     */
    public PostInternetData(int actionType, String newData, String currentUser) {
        this.actionType = actionType;
        if (actionType == 3) {
            this.friendID = newData;
        } else if (actionType == 4 || actionType == 5) {
            this.newDisplayPictureStringFormat = newData;
        }
        this.currentUserID = currentUser;
    }


    /**
     * Method: AsyncTask of the FetchInternetData (Probably to be executed).
     * @param voids: Assumes it is a non-null String object.
     * @return null.
     */
    @Override
    protected Void doInBackground(Void... voids) {


        try {
            CloudantClient client = ClientBuilder.url(new URL(dbURL))
                    .username("mypetapp")
                    .password("ics4up01")
                    .build();
            Database accountsDB = client.database(ACCOUNTS_DB, true);
            Database feedDB = client.database(FEED_DB, true);



            if (actionType == 1) { // Making a new account (1)
                Log.d("Vinit's SIZE ", mUserInfo.size()+"");
                accountsDB.save(new AccountInfoDocument(mUserInfo));
                feedDB.save(new FeedPostDocument(mUserInfo.get(0)));
            }

            else if (actionType == 2) { // Adding a new document to newsfeed (2)
                Log.d("Vinit's LOG --> ", "BEFORE POSTING DOC ON CLOUDANT");
                FeedPostDocument fpd = feedDB.find(FeedPostDocument.class, currentUserID);
                fpd.addPost(newPost);
                feedDB.update(fpd);
                Log.d("Vinit's LOG --> ", "AFTER POSTING DOC ON CLOUDANT");
            }

            else if (actionType == 3) { // Adding a friend (3)
                AccountInfoDocument aid = accountsDB.find(AccountInfoDocument.class, currentUserID);
                boolean isFriendAlready = false;
                for (int i = 0; i < aid.getFriends().size(); i++) {
                    if (aid.getFriends().get(i).equals(friendID)) { isFriendAlready = true; }
                }
                if (!isFriendAlready) {
                    aid.addFriend(friendID);
                }
                accountsDB.update(aid);
            }
            else if(actionType == 4) { // Changing the Display Picture
                AccountInfoDocument aid = accountsDB.find(AccountInfoDocument.class, currentUserID);
                aid.changeDP(newDisplayPictureStringFormat);
                accountsDB.update(aid);
            } else if(actionType == 5) { // Changing the Display Picture of Pet
                Log.d("Vinit's LOG", "Setting PET DP ONLINE --> 2");
                AccountInfoDocument aid = accountsDB.find(AccountInfoDocument.class, currentUserID);
                aid.changePetDP(newDisplayPictureStringFormat);
                accountsDB.update(aid);
            }

            Log.d("Vinit's Log: ", "<<<<SAVED TO DB>>>>");

        } catch (IOException e) {
            Log.d("Vinit's Post Error: ", "I/O exception at fetching data");
        } catch (Exception e) {
            Log.d("Vinit's Post Error: ", e.toString());
        }
        return null;
    }

    /**
     * Method: Used to run the appropriate OnAsyncTaskCompleted object required after AsyncTask completed.
     * @param aVoid: Not used in method.
     *
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        if (actionType == 1){
            // No need to do anything.
        } else if (actionType == 2) {
            afterTaskCompleted.onPostComplete();
        } else if (actionType == 3) {
            //afterTaskCompleted.onPostComplete();
        }

    }
}
