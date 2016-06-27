package com.example.username.petly;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.username.myapplication.R;
import com.google.android.gms.maps.model.Circle;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Class: Manages the MainActivity newsfeed RecyclerView.
 */
public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendsRecyclerViewHolder> {

    private final String KEY_USER_USERNAME = "userUserName";
    private final int ADD_FRIEND = 3;

    private ArrayList<AccountInfoDocument> dataItems;
    private String currentUser;


    /**
     * Constructor: Used to instantiate the FriendsRecyclerViewAdapter
     * @param dataItems: Assumes it is a non-null ArrayList of non-null AccountInfoDocument.
     */
    public FriendsRecyclerViewAdapter(ArrayList<AccountInfoDocument> dataItems) {
        this.dataItems = dataItems;
    }

    /**
     * Method: Used to create the ViewHolder for each card part of the RecyclerView using this Class.
     * @param parent: Assumes it is a non-null ViewGroup object.
     * @param viewType: Assumes it is a non-null int data-type.
     * @return: New FriendsRecyclerViewHolder object.
     */
    @Override
    public FriendsRecyclerViewAdapter.FriendsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlist_cardview, parent, false);
        return new FriendsRecyclerViewHolder(v);
    }

    /**
     * Method: Used to set the views part of the Viewholder.
     * @param holder: Assumes that it is a non-null FriendsRecyclerViewHolder object.
     * @param position: Assumes it is a non-null int data-type.
     */
    @Override
    public void onBindViewHolder(FriendsRecyclerViewAdapter.FriendsRecyclerViewHolder holder, int position) {
        try {
            holder.mUsername.setText(dataItems.get(position).getUsername());
            byte[] userImageInBytes = Base64.decode(dataItems.get(position).getDisplayPic(), Base64.DEFAULT);
            holder.mUserDP.setImageBitmap(BitmapFactory.decodeByteArray(userImageInBytes, 0, userImageInBytes.length));
            byte[] petImageInBytes = Base64.decode(dataItems.get(position).getPetDisplayPic(), Base64.DEFAULT);
            holder.mUserPetDP.setImageBitmap(BitmapFactory.decodeByteArray(petImageInBytes, 0, petImageInBytes.length));
            holder.mPetName.setText(dataItems.get(position).getPetName());
        } catch (NullPointerException e) {

        }

    }

    /**
     * Method: Used to return the total amount of items (cards) currently suspended in the RecyclerView.
     * @return: Returns the size as an int.
     */
    @Override
    public int getItemCount() {
        try {
            return dataItems.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * Method: Calls the parent onAttachedToRecyclerView (Called by the RecyclerView using this custom Adapter).
     * @param recyclerView: Assumes it is a non-null RecyclerView object.
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Class: Used to define the viewholder in each of the cards of the Adapter.
     */
    public class FriendsRecyclerViewHolder extends RecyclerView.ViewHolder {
        protected CardView cv;
        protected TextView mUsername;
        protected TextView mPetName;
        protected ImageView mUserDP;
        protected ImageView mUserPetDP;

        /**
         * Constructor: Defines the views part of the viewholder and their values.
         * @param v: Assumes this is a non-null View object.
         */
        public FriendsRecyclerViewHolder(final View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.friendlist_card_view);
            mUsername = (TextView) v.findViewById(R.id.username_text);
            mPetName = (TextView) v.findViewById(R.id.userfound_pet_name);
            mUserDP = (ImageView) v.findViewById(R.id.userfound_picture);
            mUserPetDP = (ImageView) v.findViewById(R.id.find_friend_pet_picture);


            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
            currentUser = prefs.getString(KEY_USER_USERNAME, "");

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Vinit's Log", "FRIEND ADDED");
                    String friendID = mUsername.getText().toString();
                    new PostInternetData(ADD_FRIEND, friendID, currentUser).execute();
                }
            });
        }


    }


}
