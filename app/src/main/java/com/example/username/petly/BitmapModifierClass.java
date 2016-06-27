package com.example.username.petly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

/**
 * Class: Used to rescale images to a usable format (for memory efficiency).
 */
public class BitmapModifierClass extends AsyncTask<Void, Void, Bitmap> {
    private String bmStringFormat;

    private int imageViewWidth;
    private int imageViewHeight;

    private ImageView imageView;

    private OnAsyncTaskCompleted postCompleteTask;

    /**
     * Constructor: Instantiates the BitmapModifierClass AsyncTask object.
     * @param bmStringFormat: Assumes it is a non-null String object (Base64 encoded).
     * @param imageView: Assumes it is a non-null ImageView object.
     * @param postTask: Assumes it is a non-null OnAsyncTaskCompleted object.
     */
    public BitmapModifierClass(String bmStringFormat, ImageView imageView, OnAsyncTaskCompleted postTask) {
        this.imageView = imageView;
        this.bmStringFormat = bmStringFormat;
        this.imageViewWidth = imageView.getWidth();
        this.imageViewHeight = imageView.getHeight();
        this.postCompleteTask = postTask;

    }

    /**
     * Method: AsyncTask of the BitmapModifierClass (Probably to be executed).
     * @param Void: Assumes it is a non-null Void object.
     * @return
     */
    @Override
    protected Bitmap doInBackground(Void... Void) {
        return stringToBitmap();
    }

    /**
     * Method: Used to run the appropriate OnAsyncTaskCompleted object required after AsyncTask completed.
     * @param bitmap: Assumes it is a non-null Bitmap object.
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {

        postCompleteTask.onPostComplete(bitmap);
        try {

        } catch (Exception e) {

        }
    }

    /**
     * Method: Used to get the highest factor by which the image resolution can be reduced.
     * @param bmfOptions: Assumes it is a non-null BitmapFactory.Options object.
     * @return: An int value.
     */
    private int getInSampleSize(BitmapFactory.Options bmfOptions) {
        final int bitmapWidth = bmfOptions.outWidth;
        final int bitmapHeight = bmfOptions.outHeight;
        int scaleRatio = 1;

        if (bitmapWidth > imageViewWidth || bitmapHeight > imageViewHeight) {
            int halfBitmapWidth = bitmapWidth / 2;
            int halfBitmapHeight = bitmapHeight / 2;
            while (halfBitmapWidth/scaleRatio > imageViewWidth || halfBitmapHeight/scaleRatio > imageViewHeight) {
                scaleRatio *= 2;
            }
        }
        Log.d("Vinit's LOG", "SCALE RATIO: "+scaleRatio);
        return scaleRatio;
    }

    /**
     * Method: Used to reconfigure the size of the image (Uses getInSampleSize(BitmapFactory.Options)).
     * @return: Bitmap object.
     */
    private Bitmap stringToBitmap() {
        BitmapFactory.Options bmfOptions = new BitmapFactory.Options();
        bmfOptions.inJustDecodeBounds = true;
        byte[] stringToByteFormat = Base64.decode(bmStringFormat, Base64.DEFAULT);
        BitmapFactory.decodeByteArray(stringToByteFormat, 0, stringToByteFormat.length, bmfOptions);
        Log.d("Vinit's LOG", "FIRST SIZE: "+bmfOptions.outHeight);
        bmfOptions.inSampleSize = getInSampleSize(bmfOptions);
        bmfOptions.inJustDecodeBounds = false;
        bmfOptions.outHeight = bmfOptions.outHeight/bmfOptions.inSampleSize;
        bmfOptions.outWidth = bmfOptions.outWidth/bmfOptions.inSampleSize;
        Log.d("Vinit's LOG", "SECOND SIZE: "+bmfOptions.outHeight);
        return BitmapFactory.decodeByteArray(stringToByteFormat, 0, stringToByteFormat.length, bmfOptions);
    }
}
