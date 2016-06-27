package com.example.username.petly;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.username.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Class: Manages the MainActivity newsfeed RecyclerView.
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainRecyclerViewHolder> {

    private ArrayList<Post> dataItems;
    private MainRecyclerViewHolder mMainRecyclerViewHolder;

    /**
     * Constructor: Used to instantiate the MainRecyclerViewAdapter object.
     * @param dataItems: Assumes dataItems is a non-null ArrayList of non-null Post objects.
     */
    public MainRecyclerViewAdapter(ArrayList<Post> dataItems) {
        this.dataItems = dataItems;
    }

    /**
     * Method: Used to create the Viewholder for each card part of the RecyclerView using this Class.
     * @param parent: Assumes it is a non-null ViewGroup object.
     * @param viewType: Assumes it is a non-null int data-type.
     * @return: New MainRecyclerViewHolder object.
     */
    @Override
    public MainRecyclerViewAdapter.MainRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_normal_cardview, parent, false);
        mMainRecyclerViewHolder = new MainRecyclerViewHolder(v);

        return mMainRecyclerViewHolder;
    }

    /**
     * Method: Used to set the views part of the Viewholder.
     * @param holder: Assumes that it is a non-null MainRecyclerViewHolder object.
     * @param position: Assumes it is a non-null int data-type.
     */
    @Override
    public void onBindViewHolder(final MainRecyclerViewAdapter.MainRecyclerViewHolder holder, final int position) {
        holder.mTextString.setText(dataItems.get(position).getStatus());
        holder.mDateTimeString.setText(dataItems.get(position).getDateTime().toString());
        holder.mPosterUsername.setText("~"+dataItems.get(position).getPosterUsername());
        new BitmapModifierClass(dataItems.get(position).getPosterPicture(), holder.mPosterDP, new OnAsyncTaskCompleted() {
            @Override
            public void onPostComplete() {
                //Not used
            }

            @Override
            public void onPostComplete(Bitmap bitmapImage) {
                holder.mPosterDP.setImageBitmap(bitmapImage);
            }

            @Override
            public void onFetchComplete(AccountInfoDocument data) {
                //Not used
            }

            @Override
            public void onFetchCompleteGetList(ArrayList<Post> dataList) {
                //Not used
            }
        }).execute();

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
    public class MainRecyclerViewHolder extends RecyclerView.ViewHolder {
        protected CardView cv;
        protected TextView mTextString;
        protected TextView mDateTimeString;
        protected CircleImageView mPosterDP;
        protected TextView mPosterUsername;

        /**
         * Constructor: Defines the views part of the viewholder and their values.
         * @param v: Assumes this is a non-null View object.
         */
        public MainRecyclerViewHolder(final View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.main_normal_card_view);
            cv.setCardElevation(10);
            mTextString = (TextView) v.findViewById(R.id.text_inside_card);
            mDateTimeString = (TextView) v.findViewById(R.id.date_of_post_text);
            mPosterDP = (CircleImageView) v.findViewById(R.id.poster_picture);
            mPosterUsername = (TextView) v.findViewById(R.id.poster_username_text);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Vinit's TAG" ,"Card Press detected");


                }
            });
        }



    }




}
