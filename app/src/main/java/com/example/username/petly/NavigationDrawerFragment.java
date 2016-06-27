package com.example.username.petly;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.username.myapplication.R;

import java.security.acl.Group;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;

    private LinearLayout mDrawerHeaderLayout;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    /**
     * Method: Used to Create the navigation drawer in the MainActivity.
     * @param savedInstanceState:
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer = Boolean.getBoolean(readFromPreference(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }

    }


    /**
     * Method: Used to create the view in the navigation drawer.
     * @param inflater: Assumes it is a non-null LayoutInflater object.
     * @param container: Assumes it is a non-null ViewGroup object.
     * @param savedInstanceState: Assumes it is a non-null Bundle object.
     * @return Inflated View object.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    /**
     * Method: Used to setup the Navigation Drawer. (Used from the MainActivity.
     * @param drawerLayout: Assumes it is a non-null DrawerLayout object.
     * @param toolbar: Assumes it is a non-null Toolbar object.
     */
    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(R.id.navigation_drawer_fragment);
        mDrawerLayout = drawerLayout;
        Group group1 = (Group) getActivity().findViewById(R.id.nav_group);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreference(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    /**
     * Method: Used to save the Navigation Drawer preferences to SharedPreferences.
     * @param context: Assumes it is a non-null Context object.
     * @param preferenceName: Assumes it is a non-null String object.
     * @param preferenceValue: Assumes it is a non-null String object.
     */
    public static void saveToPreference(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    /**
     * Method: Used to read Navigation Drawer preferences from SharedPreferences.
     * @param context: Assumes it is a non-null Context object.
     * @param preferenceName: Assumes it is a non-null String object.
     * @param preferenceValue: Assumes it is a non-null String object.
     * @return preferenceValue in String format.
     */
    public static String readFromPreference(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        return sharedPreferences.getString(preferenceName, preferenceValue);
    }

    /**
     * Method: Used to configure the Options in the Navigation Drawer.
     * @param item: Assumes it is a non-null MenuItem object.
     * @return returns boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }




}

