package com.example.username.petly;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

/**
 * Class: Used to Decorate the UI of the Recylcer View items of this app's Recycler Views.
 */
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    /**
     * Constructor: Used to instantiate the ItemOffsetDecoration object.
     * @param itemOffset: Assumes it is a non-null int data-type.
     */
    public ItemOffsetDecoration (int itemOffset) {
        this.mItemOffset = itemOffset;
    }

    /**
     * Constructor: Used to instantiate the ItemOffsetDecoration object.
     * @param context: Assumes it is a non-null Context object.
     * @param itemOffsetBy: Assumes it is a non-null int data-type.
     */
    public ItemOffsetDecoration(Context context, int itemOffsetBy) {
        this(itemOffsetBy);
    }

    /**
     * Method: Used to set the cards' offset from the sides.
     * @param outRect: Assumes it is a non-null Rect object.
     * @param view: Assumes it is a non-null View object.
     * @param recViewParent: Assumes it is a non-null RecyclerView object.
     * @param recViewState: Assumes it is a non-null RecyclerView.State object.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView recViewParent, RecyclerView.State recViewState) {
        super.getItemOffsets(outRect, view, recViewParent, recViewState);
        outRect.set(mItemOffset - (mItemOffset/2), mItemOffset, mItemOffset - (mItemOffset/2), mItemOffset);
    }
}
