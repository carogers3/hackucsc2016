package com.example.user.dropapintest;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static android.widget.Toast.*;

public class MapsActivityTest extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity_test);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public class MainActivity extends AbstractMapActivityTest implements {
        ActionBar.OnNavigationListener,GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMyLocationChangeListener


        private static final String STATE_NAV = "nav";

        private static final int[] MAP_TYPE_NAMES = {R.string.normal,
                R.string.hybrid, R.string.satellite, R.string.terrain};
        private static final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_NORMAL,
                GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_SATELLITE,
                GoogleMap.MAP_TYPE_TERRAIN};
        private GoogleMap map = null;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                if (readyToGo()) {
                    setContentView(R.layout.activity_main);

                    SupportMapFragment mapFrag =
                            (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                    initListNav();

                    map = mapFrag.getMap();

                    if (savedInstanceState == null) {
                        CameraUpdate center =
                                CameraUpdateFactory.newLatLng(new LatLng(40.76793169992044,
                                        -73.98180484771729));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                        map.moveCamera(center);
                        map.animateCamera(zoom);
                    }

                    /*addMarker(map, 40.748963847316034, -73.96807193756104,
                            R.string.un, R.string.united_nations);
                    addMarker(map, 40.76866299974387, -73.98268461227417,
                            R.string.lincoln_center,
                            R.string.lincoln_center_snippet);
                    addMarker(map, 40.765136435316755, -73.97989511489868,
                            R.string.carnegie_hall, R.string.practice_x3);
                    addMarker(map, 40.70686417491799, -74.01572942733765,
                            R.string.downtown_club, R.string.heisman_trophy);*/

                    map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
                    map.setOnInfoWindowClickListener(this);

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
                    map.setMyLocationEnabled(true);
                    map.setOnMyLocationChangeListener(this);
                }
            }

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                map.setMapType(MAP_TYPES[itemPosition]);

                return(true);
            }

            @Override
            public void onSaveInstanceState(Bundle savedInstanceState) {
                super.onSaveInstanceState(savedInstanceState);

                savedInstanceState.putInt(STATE_NAV,
                        getSupportActionBar().getSelectedNavigationIndex());
            }

            @Override
            public void onRestoreInstanceState(Bundle savedInstanceState) {
                super.onRestoreInstanceState(savedInstanceState);

                getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_NAV));
            }

            @Override
            public void onInfoWindowClick(Marker marker) {
                makeText(this, marker.getTitle(), LENGTH_LONG).show();
            }

            @Override
            public void onMyLocationChange(Location lastKnownLocation) {
                Log.d(getClass().getSimpleName(),
                        String.format("%f:%f", lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude()));
            }

            private void initListNav() {
                ArrayList<String> items=new ArrayList<String>();
                ArrayAdapter<String> nav=null;
                ActionBar bar=getSupportActionBar();

                for (int type : MAP_TYPE_NAMES) {
                    items.add(getString(type));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    nav=
                            new ArrayAdapter<String>(
                                    bar.getThemedContext(),
                                    android.R.layout.simple_spinner_item,
                                    items);
                }
                else {
                    nav=
                            new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
                }

                nav.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                bar.setListNavigationCallbacks(nav, this);
            }

            private void addMarker(GoogleMap map, double lat, double lon,
                                   int title, int snippet) {
                map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                        .title(getString(title))
                        .snippet(getString(snippet)));
            }
        }
    }

