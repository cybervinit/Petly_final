package com.example.username.petly;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.username.myapplication.R;

import java.util.ArrayList;

/**
 * Class: Created from MainActivity.
 * Use: For the user to search up friends on Cloudant server and add friends.
 *
 */
public class FriendsActivity extends AppCompatActivity {

    private final int FIND_FRIEND_ACTION = 2;
    private Toolbar mToolbar;
    private EditText mSearchText;
    private Button mSearchButton;
    private RecyclerView mRecyclerViewFriends;
    private LinearLayoutManager mLinearLayoutManagerFriends;
    private FriendsRecyclerViewAdapter mRecyclerViewAdapterFriends;
    ArrayList<AccountInfoDocument> items;
    private String userID;

    /**
     * Method: Used to initiate the following:
     * mSearchText onClickListener: used to make search edittext focusable.
     * mSearchButton onClickListener: used to search for friends on Cloudant server
     * via FetchInternetData.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        userID = getIntent().getStringExtra("currentUsername");
        items = new ArrayList<>(0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        assert getSupportActionBar() != null;
        mSearchText = (EditText) findViewById(R.id.search_edittext);
        assert mSearchText != null;
        mSearchButton = (Button) findViewById(R.id.search_button);
        mRecyclerViewFriends = (RecyclerView) findViewById(R.id.recycler_view_friends);
        mRecyclerViewAdapterFriends = new FriendsRecyclerViewAdapter(items);
        assert mRecyclerViewFriends != null;
        mLinearLayoutManagerFriends = new LinearLayoutManager(this);
        ItemOffsetDecoration iod2 = new ItemOffsetDecoration(this, this.getResources().getDimensionPixelOffset(R.dimen.item_offset));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSearchText.setHint(R.string.SEARCH_FRIEND_STRING);
        mSearchText.clearFocus();

        mRecyclerViewFriends.setAdapter(mRecyclerViewAdapterFriends);
        mRecyclerViewFriends.setHasFixedSize(true);
        mLinearLayoutManagerFriends.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewFriends.setLayoutManager(mLinearLayoutManagerFriends);
        mRecyclerViewFriends.addItemDecoration(iod2);

        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchText.setFocusable(true);
            }
        });


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mSearchText.getText().toString();
                new FetchInternetData(FIND_FRIEND_ACTION, username, new OnAsyncTaskCompleted() {
                    @Override
                    public void onFetchComplete(AccountInfoDocument data) {
                        try {
                            if (!data.equals(null)) {
                                if (!data.getUsername().equals(userID)) {
                                    items.add(items.size(), data);
                                    mRecyclerViewAdapterFriends.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getBaseContext(), "You searched yourself!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (NullPointerException npe) {
                            mRecyclerViewAdapterFriends.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), "No User Found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFetchCompleteGetList(ArrayList<Post> dataList) {
                        //Not used
                    }

                    @Override
                    public void onPostComplete() {
                        //Not used
                    }

                    @Override
                    public void onPostComplete(Bitmap bitmapImage) {
                        //Not used
                    }

                }).execute();

            }
        });


    }


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onBackPressed();
            return true;
        }
        if (id == R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }*/

    /**
     * Method: implements the parent onResume() class. Also, sets mSearchText selected to false.
     */
    @Override
    public void onResume() {
        super.onResume();
        mSearchText.setSelected(false);
    }

}
