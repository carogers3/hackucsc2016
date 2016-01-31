package com.example.caseychien.myapplication;

import android.content.Context;
import android.location.Location;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment map;
        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapx);
        map.getMapAsync(this);
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

        // Enable MyLocation Layer of Google Map
        try {
            mMap.setMyLocationEnabled(true);


            // Get LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Get Current Location
            Location myLocation = locationManager.getLastKnownLocation(provider);
            // Add a marker in Sydney and move the camera
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            LatLng currentLocation = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        } catch(SecurityException e) {
            Log.e("Tag", "User failed to give me location.");
        }
    }
}