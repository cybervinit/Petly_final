package com.example.username.petly;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sunny Patel on 5/25/2016.
 * Last Modified by Sunny Patel on 6/10/2016
 * Log:
 * 6/10 - Javadocing and documentation completed
 * 5/11 - Added extra instance variables: distanceTo, id, and type
 * 5/01 - Created
 */
public class AddressClass {
    private LatLng latitude;
    private String name;
    private String address;
    private float distanceTo;
    private int id;
    private String type;

    /**
     * @param latitude   : LatLng
     *                   The latitude and longitude of the point of interest
     * @param name       : String
     *                   The name of the point of interest
     * @param Address    : String
     *                   The address (in postal code or street name) of the point of interest
     * @param distanceTo : float
     *                   The distance to the point of interest from the current location
     * @param id         : int
     *                   The numeric id of the point of interest, used for troubleshooting purposes and combining beacons
     * @param type       : int
     *                   The type of place, used for filtering
     */
    public AddressClass(LatLng latitude, String name, String Address, float distanceTo, int id, String type) {
        this.latitude = latitude;
        this.name = name;
        this.address = Address;
        this.distanceTo = distanceTo;
        this.id = id;
        this.type = type;

    }

    /**
     * @return : float
     * the distance to a given object
     */
    public float getDistance() {
        return distanceTo;

    }

    /**
     * Returns the Name of the park/vet clinic
     *
     * @return : String
     * the name
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the LatLng object
     *
     * @return : LatLng
     * The latitude/longitude object associated with the location
     */
    public LatLng getLat() {
        return latitude;
    }

    /**
     * Returns the type of location
     *
     * @return : String
     * The type of location this is
     */
    public String getType() {
        return type;
    }

}
