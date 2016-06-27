package com.example.username.petly;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.username.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Class: Created from LoginActivity
 * Use: For the user to interact with other users through posts.
 * Use: To open the navigation drawer.
 */
public class MainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_PET_IMAGE = 2;
    private static final int REFRESH_ACTION = 3;
    private static final int SET_NEW_DP_ACTION = 4;
    private static final int SET_NEW_PET_DP_ACTION = 5;
    private boolean firstTimeDPFetch = true;
    private static final String KEY_USER_USERNAME = "userUserName";
    private static final String KEY_USER_PASSWORD = "userPassword";
    private static final String KEY_USER_REGISTERED = "isRegistered";
    private static final String KEY_USER_DP = "userDP";

    private final int CLICK_DELAY = 450;

    private Toolbar mToolbar;
    private NavigationView mNaviView;
    private RelativeLayout mRelativeLayout;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MainRecyclerViewAdapter mAdapter;
    private ArrayList<Post> items;

    private String userID;
    private String mDisplayPictureStringFormat;
    private String mPetDisplayPictureStringFormat;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor msfEditor;


    private EditText mPostText;

    private CircleImageView mDisplayPicture;
    private CircleImageView mPetDisplayPicture;
    private TextView mUsernameTextView;
    private TextView mPetnameTextView;

    private AlertDialog.Builder mPostAlertDialogBuilder;
    private AlertDialog mPostAlertDialog;


    /**
     * Method: Initiates the following:
     * mNaviView OnItemSelectedListener
     * mDisplayPicture OnClickListener
     * mPetDisplayPicture OnClickListener
     * @param savedInstanceState: Assumes it is a valid non-null Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        final NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        mNaviView = (NavigationView) findViewById(R.id.nav_view);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        msfEditor = mPrefs.edit();
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        assert mRecyclerView != null;
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_container);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MainRecyclerViewAdapter(items);
        ItemOffsetDecoration iod = new ItemOffsetDecoration(this, this.getResources().getDimensionPixelSize(R.dimen.item_offset));
        mPostText = new EditText(this);
        View headerView = mNaviView.getHeaderView(0);
        mDisplayPicture = (CircleImageView) headerView.findViewById(R.id.user_picture);
        mPetDisplayPicture = (CircleImageView) headerView.findViewById(R.id.user_pet_picture);
        mUsernameTextView = (TextView) headerView.findViewById(R.id.user_name_display);
        mPetnameTextView = (TextView) headerView.findViewById(R.id.user_petname_display);
        mPostAlertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Notification Message")
                .setMessage(" ");
        mPostText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        mPostText.setHint(" Express Yourself ");
        mPostAlertDialogBuilder.setView(mPostText);


        setSupportActionBar(mToolbar);

        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(iod);
        mRecyclerView.setAdapter(mAdapter);

        userID = mPrefs.getString(KEY_USER_USERNAME, "");

        mRefreshLayout.setNestedScrollingEnabled(true);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
            }
        });

        /**
         * Alert Dialog Postive Button listener: Allows user to post to the cloudant server.
         */
        mPostAlertDialogBuilder.setPositiveButton("POST", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                if (mPostText.getText().length() <= 0) {
                    Toast.makeText(MainActivity.this, "Please Enter a Message. Do not leave blank.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Posting...", Toast.LENGTH_SHORT).show();
                    Post newPost = new Post(mPostText.getText().toString(), userID, mDisplayPictureStringFormat);
                    new PostInternetData(2, userID, newPost, new OnAsyncTaskCompleted() {
                        @Override
                        public void onPostComplete() {
                            mPostText.setText("");
                            mPostText.setSelected(false);
                            Toast.makeText(MainActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            refreshFeed();
                        }

                        @Override
                        public void onPostComplete(Bitmap bitmapImage) {

                        }

                        @Override
                        public void onFetchComplete(AccountInfoDocument data) {
                        }

                        @Override
                        public void onFetchCompleteGetList(ArrayList<Post> dataList) {
                        }
                    }).execute();

                }

            }
        }).setTitle("Post").setMessage("Enter your message here:");


        /**
         * Method that sets the AlertDialog negative button. Allows user to close alert dialog.
         */
        mPostAlertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPostText.getText().length() <= 0) {
                    Toast.makeText(MainActivity.this, "Dismissed Dialog.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();


            }
        });

        mPostAlertDialog = mPostAlertDialogBuilder.create();


        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);

        mNaviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.pet_management_activity_item) {
                    mDrawerLayout.closeDrawers();
                    mNaviView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    }, CLICK_DELAY);
                    return true;
                }
                if (id == R.id.map_activity_item) {
                    mDrawerLayout.closeDrawers();
                    mNaviView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class).putExtra("KEY", userID).putExtra("KEY_USERDP", mDisplayPictureStringFormat);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    }, CLICK_DELAY);
                    return true;
                }
                if (id == R.id.friends_activity_item) {
                    mDrawerLayout.closeDrawers();
                    mNaviView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, FriendsActivity.class).putExtra("currentUsername", userID);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    }, CLICK_DELAY);
                    return true;
                }
                if (id == R.id.liscense_activity_item) {
                    mDrawerLayout.closeDrawers();
                    mNaviView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LiscenseActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    }, CLICK_DELAY);
                    return true;
                }
                if (id == R.id.logout_item) {
                    mDrawerLayout.closeDrawers();
                    mNaviView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString(KEY_USER_USERNAME, "");
                            editor.putString(KEY_USER_PASSWORD, "");
                            editor.putString(KEY_USER_REGISTERED, "");
                            editor.apply();
                            finish();
                        }
                    }, CLICK_DELAY);
                    return true;
                }
                if (id == R.id.email_us_activity_item) {
                    mDrawerLayout.closeDrawers();
                    mNaviView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, EmailUsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    }, CLICK_DELAY);
                    return true;
                }
                return false;
            }
        });


        fetchUserInfo();
        mUsernameTextView.setText(userID);

        mDisplayPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE);
            }
        });

        mPetDisplayPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_PET_IMAGE);
            }
        });
    }

    /**
     * Method: Updates the newsfeed on the main screen by getting the user's posts and the user's friends posts from Cloudant
     * through a FetchInternetData Task.
     */
    private void refreshFeed() {
        new FetchInternetData(REFRESH_ACTION, userID, new OnAsyncTaskCompleted() {
            @Override
            public void onPostComplete() {
            }

            @Override
            public void onPostComplete(Bitmap bitmapImage) {

            }

            @Override
            public void onFetchComplete(AccountInfoDocument data) {
            }

            @Override
            public void onFetchCompleteGetList(ArrayList<Post> dataList) throws NullPointerException {
                int initialSize = items.size();
                for (int i = 0; i < initialSize; i++) {
                    items.remove(items.size() - 1);
                }
                ArrayList<Post> items2 = new ArrayList<>();
                for (int k = 0; k < dataList.size(); k++) {
                    items2.add(k, dataList.get(k));
                }
                for (int i = 0; i < items2.size(); i++) {
                    items.add(i, items2.get(items2.size() - i - 1));
                }
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
            }
        }).execute();
    }

    /**
     * Method: Resumes the Activity when returned to the MainActivity.
     * refreshes the feed and sets user info on the Nav-Drawer header.
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshFeed();
        mPostText.setSelected(false);
        fetchUserInfo();


        //setDP(mPrefs.getString(KEY_USER_DP, ""));
    }


    /**
     * Method: Used to access photos on the user's phone.
     * @param requestCode: Assumes this is a valid, non-null int value.
     * @param resultCode: Assumes this is a valid, non-null int value.
     * @param data: Assumes this is a valid, non-null Intent object.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If they wanna change their DP
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Uri image = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(image, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bmImage = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bmImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                byte[] picInBytes = outputStream.toByteArray();
                setDP(Base64.encodeToString(picInBytes, Base64.DEFAULT));
                setDPOnline(Base64.encodeToString(picInBytes, Base64.DEFAULT));
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Unable to set picture", Toast.LENGTH_SHORT).show();
            }

        }
        // If they wanna change their DP
        if (requestCode == RESULT_LOAD_PET_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Uri image = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(image, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bmImage = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bmImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                byte[] picInBytes = outputStream.toByteArray();
                Log.d("Vinit's PET PIC", Base64.encodeToString(picInBytes, Base64.DEFAULT));
                setPetDP(Base64.encodeToString(picInBytes, Base64.DEFAULT));
                setPetDPOnline(Base64.encodeToString(picInBytes, Base64.DEFAULT));
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Unable to set picture", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * Method: To set the user's DP to a new value
     * @param stringFormat: Assumes it is a valid non-null String object (Base64 encoded).
     */
    private void setDP(String stringFormat) {
        byte[] image = Base64.decode(stringFormat, Base64.DEFAULT);
        mDisplayPictureStringFormat = stringFormat;
        mDisplayPicture.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
    }

    /**
     * Method: To set the user's DP online to a new value in String format (Base64 encoded).
     * @param stringFormat: Assumes it is a valid non-null String object (Base64 encoded).
     */
    private void setDPOnline(String stringFormat) {
        new PostInternetData(SET_NEW_DP_ACTION, stringFormat, userID).execute();
    }

    /**
     * Method: To set the user's pet's DP to a new value
     * @param stringFormat: Assumes it is a valid non-null String object (Base64 encoded).
     */
    private void setPetDP(String stringFormat) {
        byte[] image = Base64.decode(stringFormat, Base64.DEFAULT);
        mPetDisplayPictureStringFormat = stringFormat;
        mPetDisplayPicture.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
    }

    /**
     * Method: To set the user's pet's DP online to a new value in String format (Base64 encoded).
     * @param stringFormat: Assumes it is a valid non-null String object (Base64 encoded).
     */
    private void setPetDPOnline(String stringFormat) {
        Log.d("Vinit's LOG", "Setting PET DP ONLINE");
        new PostInternetData(SET_NEW_PET_DP_ACTION, stringFormat, userID).execute();
    }

    /**
     * Method: Fetches the user's data from online, if first time and later sets the user's info in the Nav-Drawer appropriately.
     */
    private void fetchUserInfo() {
        if (firstTimeDPFetch) {
            new FetchInternetData(1, userID, new OnAsyncTaskCompleted() {
                @Override
                public void onPostComplete() {

                }

                @Override
                public void onPostComplete(Bitmap bitmapImage) {

                }

                @Override
                public void onFetchComplete(AccountInfoDocument data) {
                    try {
                        msfEditor.putString(KEY_USER_DP, data.getDisplayPic());
                        msfEditor.apply();
                        mDisplayPictureStringFormat = data.getDisplayPic();
                        setDP(data.getDisplayPic());
                        mPetnameTextView.setText(data.getPetName());
                        setPetDP(data.getPetDisplayPic());
                        firstTimeDPFetch = false;
                    } catch (NullPointerException e) {
                        Log.d("Vinit's LOG", "NULL WHILE GETTING ACCOUNT INFO");
                    }

                }

                @Override
                public void onFetchCompleteGetList(ArrayList<Post> dataList) {

                }
            }).execute();
        } else {
            setDP(mDisplayPictureStringFormat);
        }

    }

    /**
     * Method: Inflates the appropriate layout of the toolbar in the MainActivity
     * @param menu: Assumes it is a non-null Menu Object.
     * @return Boolean, true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    /**
     * Method: Used to create action for the options menu in the toolbar.
     * @param item: Assumes it is a non-null MenuItem Object.
     * @return True if id of MenuItem matches. Else false.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_post_button) {
            mPostAlertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
