package com.example.username.petly;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Interface: Used to implement methods according to required task to be run after an AsyncTask.
 */
public interface OnAsyncTaskCompleted {

    /**
     * Method: Used to run a task after AsyncTask.
     */
    void onPostComplete();

    /**
     * Method: Used to run a task with a use of Bitmap from AyncTask after AsyncTask.
     * @param bitmapImage: Assumes is it a non-null Bitmap object.
     */
    void onPostComplete(Bitmap bitmapImage);

    /**
     * Method: Used to run a task with a use of AccountInfoDocument from AyncTask after AsyncTask.
     * @param data: Assumes is it a non-null AccountInfoDocument object.
     */
    void onFetchComplete(AccountInfoDocument data);

    /**
     * Method: Used to run a task with a use of ArrayList<Post> from AyncTask after AsyncTask.
     * @param dataList: Assumes is it a non-null ArrayList<Post> with non-null Post objects.
     */
    void onFetchCompleteGetList(ArrayList<Post> dataList);
}
