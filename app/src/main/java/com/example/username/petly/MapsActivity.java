package com.example.username.petly;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;


import com.example.username.myapplication.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar mToolbar;
    private GoogleMap mMap;
    private ArrayList<AddressClass> addressList = new ArrayList<>();
    private ArrayList<AddressClass> filteredList = new ArrayList<>();
    private LatLng selectedLatLng;
    private Location myLocation = new Location("");
    private LocationManager mMyLocationManager;
    private android.location.LocationListener mMyLocationListener;
    private AddressClass park1;
    private AddressClass park2;
    private AddressClass park3;
    private AddressClass park4;
    private AddressClass selectedAddress;
    private LinearLayout filterPane;
    private String filter1 = "DogPark";


    private Button mGoButton1;
    private Button mGoButton2;
    private Button mGoButton3;
    private Button mGoButton4;

    private boolean filterPaneVisible;

    private String userID;
    private String userDP;


    /**
     * Class that starts upon loading (i.e. when the intent is called
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mMyLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mGoButton1 = (Button) findViewById(R.id.go_button_1);
        mGoButton2 = (Button) findViewById(R.id.go_button_2);
        mGoButton3 = (Button) findViewById(R.id.go_button_3);
        mGoButton4 = (Button) findViewById(R.id.go_button_4);

        filterPaneVisible = true;

        userID = getIntent().getStringExtra("KEY");
        userDP = getIntent().getStringExtra("KEY_USERDP");



        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindAddress(park1);
            }
        });
        mGoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindAddress(park2);
            }
        });
        mGoButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindAddress(park3);
            }
        });
        mGoButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindAddress(park4);
            }
        });

        mGoButton1.setEnabled(false);
        mGoButton2.setEnabled(false);
        mGoButton3.setEnabled(false);
        mGoButton4.setEnabled(false);

        filterPane = (LinearLayout) findViewById(R.id.filterPane);
        filterPane.setVisibility(View.INVISIBLE);
        filterPaneVisible = false;

        mMyLocationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation.setLongitude(location.getLongitude());
                myLocation.setLatitude(location.getLatitude());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMyLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, (float) 0.0, mMyLocationListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }




        new LoadAddressesAsyncTask(new OnAsyncTaskCompleted() {
            @Override
            public void onPostComplete() {
                sortByDistance();
                mGoButton1.setEnabled(true);
                mGoButton2.setEnabled(true);
                mGoButton3.setEnabled(true);
                mGoButton4.setEnabled(true);
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



        /**File newFile = new File("parklocations.csv");
         Scanner inFile;
         String input = "";
         Geocoder geocoder = new Geocoder(this);
         AddressClass temp1;



         try {
         Toast.makeText(MapsActivity.this, "skjads ", Toast.LENGTH_SHORT).show();
         inFile = new Scanner(newFile);
         while (inFile.hasNextLine()) {
         input = inFile.nextLine();
         Toast.makeText(MapsActivity.this, "input", Toast.LENGTH_SHORT).show();
         String[] temp = input.split(",");
         List<Address> addresses = null;
         addresses = geocoder.getFromLocationName(temp[0], 1);
         LatLng lat1  = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
         temp1 = new AddressClass(lat1, temp[0], temp[1]);
         addressList.add(temp1);
         }
         }
         catch(Exception e){
         Log.e("MYAPP", "exception", e);
         }*/
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
    }

    /*public void FindAddress(View V){
        EditText editText = (EditText)findViewById(R.id.park1);
        String addressString = editText.getText().toString();

        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocationName(addressString, 1);
            LatLng lat1  = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(lat1).title(getString(R.string.typeDogPark)));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(lat1));

        } catch (Exception e) {
            Toast.makeText(MapsActivity.this, "Invalid Location", Toast.LENGTH_SHORT).show();

        }



    }*/

    /**
     * Loads the addresses from a CSV File
     * @param: none
     * @return None
     */
    public void loadAddresses(){
        InputStream inputStream = getResources().openRawResource(R.raw.parklocations);

        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        AddressClass temp1 = null;
        Geocoder geocoder = new Geocoder(this);
        Location tempLocate= new Location("");
        double tempLat = 0;
        double tempLong = 0;
        /*
        myLocation.setLatitude(43.7781147);
        myLocation.setLongitude(-79.2302099);
         */


        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] temp = csvLine.split(",");
                List<Address> addresses = null;
                addresses = geocoder.getFromLocationName(temp[1] + ", toronto, Ontario", 1);
                if (addresses.size() > 0){
                    LatLng lat1  = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    tempLat = addresses.get(0).getLatitude();
                    tempLong = addresses.get(0).getLongitude();
                    tempLocate.setLatitude(tempLat);
                    tempLocate.setLongitude(tempLong);
                    temp1 = new AddressClass(lat1, temp[0], temp[1], myLocation.distanceTo(tempLocate), Integer.parseInt(temp[2]), temp[3]);
                    addressList.add(temp1);
                }

            }
        }
        catch (Exception e) {
            Log.e("MYAPP", "exception", e);
        }

    }

    /**
     * Sorts all of the objects in the original arraylist by distance
     * @param: none
     * @return None
     * @precondition addressList should exist, filled with at least 4 AddressClass objects: cannot have a null distance
     */
    public void sortByDistance(){
        int min;

        try {
            for (int i = 0; i < addressList.size(); i++) {
                // Assume first element is min
                min = i;
                for (int j = i + 1; j < addressList.size(); j++) {
                    if (addressList.get(j).getDistance() < addressList.get(min).getDistance()) {
                        min = j;

                    }
                }
                if (min != i) {
                    AddressClass temp = addressList.get(i);
                    addressList.set(i, addressList.get(min));
                    addressList.set(min, temp);
                }
            }

            park1 = addressList.get(0);
            TextView tv1 = (TextView)findViewById(R.id.goText0);
            assert tv1 != null;
            tv1.setText(addressList.get(0).getName());
            ImageView iv1 = (ImageView)findViewById(R.id.imageView);
            if (park1.getType().equals("DogPark")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.parkBlue)));
            }
            if (park1.getType().equals("Vet")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.vetRed)));
            }



            park2 = addressList.get(1);
            tv1 = (TextView)findViewById(R.id.goText1);
            assert tv1 != null;
            tv1.setText(addressList.get(1).getName());
            iv1 = (ImageView)findViewById(R.id.imageView2);
            if (park2.getType().equals("DogPark")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.parkBlue)));
            }
            if (park2.getType().equals("Vet")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.vetRed)));
            }

            park3 = addressList.get(2);
            tv1 = (TextView)findViewById(R.id.goText2);
            assert tv1 != null;
            tv1.setText(addressList.get(2).getName());
            iv1 = (ImageView)findViewById(R.id.imageView3);
            if (park3.getType().equals("DogPark")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.parkBlue)));
            }
            if (park3.getType().equals("Vet")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.vetRed)));
            }

            park4 = addressList.get(3);
            tv1 = (TextView)findViewById(R.id.goText3);
            assert tv1 != null;
            tv1.setText(addressList.get(3).getName());
            iv1 = (ImageView)findViewById(R.id.imageView4);
            if (park4.getType().equals("DogPark")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.parkBlue)));
            }
            if (park4.getType().equals("Vet")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.vetRed)));
            }
        }
        catch(Exception e){
            Toast.makeText(MapsActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Sets the naviagtable park (or the selected Address and selected LatLng to the
     * @param targetPark : AddressClass
     *                   The target park (park 1, 2, 3, or 4)
     * @precondition : the possible target parks MUST be populated and be non-Null
     */
    public void FindAddress(AddressClass targetPark){
        Geocoder geocoder = new Geocoder(this);


        LatLng lat1  = targetPark.getLat();
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(lat1).title(getString(R.string.typeDogPark)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat1, mMap.getMaxZoomLevel()));
        selectedLatLng
                = lat1;
        selectedAddress = targetPark;
    }

    /**
     * Navigates to the currently selected location
     * @param V : View object
     *
     * @precondition Google maps api must be installed on the device
     */
    public void Navv(View V) {

        if(selectedLatLng
                != null){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+selectedLatLng
                    .latitude+","+selectedLatLng
                    .longitude));
            startActivity(i);
        }

    }

    /**
     * Sets a beacon to the user feed at the selected location
     * @param V : View object
     *
     * @precondition selectedAddress selectedLatLng
     * must not be null, ServerPost, nwePost, and PostInternetData classes must be in the package
     */
    public void BeaconCreate(View V){
        if(selectedLatLng
                != null){
            new ServerPost(selectedAddress.getName(),selectedAddress.getLat().latitude, selectedAddress.getLat().longitude).execute();
            Post newPost = new Post(userID+" is planning to go to: " + selectedAddress.getName(), userID, userDP);
            new PostInternetData(2, userID, newPost, new OnAsyncTaskCompleted() {
                @Override
                public void onPostComplete() {
                    Toast.makeText(MapsActivity.this, "Posted!", Toast.LENGTH_SHORT).show();
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

    /**
     * Shows/hides the filter pane
     * @param V : View object
     */
    public void Filter(View V){

        if(!filterPaneVisible){
            filterPane.setVisibility(View.VISIBLE);
            filterPaneVisible = true;
        }
        else{
            filterPane.setVisibility(View.INVISIBLE);
            filterPaneVisible = false;
        }




    }


    /**
     * Adds all of the matching results to the filteredAddresses list
     */
    public void reFilter(){
        /*for(int c = 0;c < addressList.size(); c++){
            for(int i = 0; i< addressList.size(); i++){
                if(addressList.get(i).getType() != filter){
                    addressList.remove(i);
                }

            }
        }*/

        /*ArrayList<Integer> indices = new ArrayList<Integer>() ;*/

        filteredList.clear();
        for(int i = addressList.size()-1; i>=0; i--){
            if ((addressList.get(i).getType().equals(filter1))){
                filteredList.add(addressList.get(i));
            }
        }
        //Toast.makeText(MapsActivity.this, "Locations found: " + filteredList.size(), Toast.LENGTH_SHORT).show();
        /*Collections.sort(indices, Collections.reverseOrder());

        for (int temp : indices){
            addressList.remove(temp);
        }*/

        sortByDistanceFiltered();


    }

    /**
     * Sorts the filtered array by the closest locations
     */
    public void sortByDistanceFiltered(){
        int min;

        try {
            for (int i = 0; i < filteredList.size(); i++) {
                // Assume first element is min
                min = i;
                for (int j = i + 1; j < filteredList.size(); j++) {
                    if (filteredList.get(j).getDistance() < filteredList.get(min).getDistance()) {
                        min = j;

                    }
                }
                if (min != i) {
                    AddressClass temp = filteredList.get(i);
                    filteredList.set(i, filteredList.get(min));
                    filteredList.set(min, temp);
                }
            }

            park1 = filteredList.get(0);
            TextView tv1 = (TextView)findViewById(R.id.goText0);
            tv1.setText(filteredList.get(0).getName());
            ImageView iv1 = (ImageView)findViewById(R.id.imageView);
            if (park1.getType().equals("DogPark")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.parkBlue)));
            }
            if (park1.getType().equals("Vet")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.vetRed)));
            }



            park2 = filteredList.get(1);
            tv1 = (TextView)findViewById(R.id.goText1);
            tv1.setText(filteredList.get(1).getName());
            iv1 = (ImageView)findViewById(R.id.imageView2);
            if (park2.getType().equals("DogPark")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.parkBlue)));
            }
            if (park2.getType().equals("Vet")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.vetRed)));
            }

            park3 = filteredList.get(2);
            tv1 = (TextView)findViewById(R.id.goText2);
            tv1.setText(filteredList.get(2).getName());
            iv1 = (ImageView)findViewById(R.id.imageView3);
            if (park3.getType().equals("DogPark")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.parkBlue)));
            }
            if (park3.getType().equals("Vet")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.vetRed)));
            }

            park4 = filteredList.get(3);
            tv1 = (TextView)findViewById(R.id.goText3);
            tv1.setText(filteredList.get(3).getName());
            iv1 = (ImageView)findViewById(R.id.imageView4);
            if (park4.getType().equals("DogPark")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.parkBlue)));
            }
            if (park4.getType().equals("Vet")){
                iv1.setBackgroundColor(Color.parseColor(getString(R.string.vetRed)));
            }
        }
        catch(Exception e){
            Toast.makeText(MapsActivity.this, "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Function called that will respond to any of the filtering options being chosen
     * @param V
     */
    public void Filter1(View V){


        Button b1 = (Button)findViewById(R.id.dog);
        Button b2 = (Button)findViewById(R.id.vet);

        if (V.getId() == b1.getId()){
            Toast.makeText(MapsActivity.this, R.string.filterDog, Toast.LENGTH_SHORT).show();
            filter1 = "DogPark";
        }


        if (V.getId() == b2.getId()){
            Toast.makeText(MapsActivity.this, R.string.filterVet, Toast.LENGTH_SHORT).show();
            filter1 = "Vet";
        }
        reFilter();

        filterPane.setVisibility(View.INVISIBLE);
    }


    /**
     * Loads the addresses in the background while the rest of th epage is displayed.
     */
    public class LoadAddressesAsyncTask extends AsyncTask<Void, Void, Void> {
        private OnAsyncTaskCompleted postTask;

        public LoadAddressesAsyncTask(OnAsyncTaskCompleted postTask) {
            this.postTask = postTask;
        }

        @Override
        public Void doInBackground(Void... voids) {

            loadAddresses();
            return null;
        }

        @Override
        public void onPostExecute(Void avoid) {
            postTask.onPostComplete();
        }
    }
}
