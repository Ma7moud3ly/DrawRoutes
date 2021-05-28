package com.ma7moud3ly.DrawMyRoute;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ma7moud3ly.DrawMyRoute.util.DirectionPointListener;
import com.ma7moud3ly.DrawMyRoute.util.GetPathFromLocation;

import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {

        //set the source and the dest path
        LatLng source = new LatLng(53.474353213819775, -2.194336680184662);
        LatLng destination = new LatLng(53.62132199731562, -2.153090419727381);
        //read the API_Key from google_maps_key
        String API_KEY = getResources().getString(R.string.google_maps_key);
        //return a polyLine for the route from source to the dest.
        new GetPathFromLocation(source, destination, API_KEY, new DirectionPointListener() {
            @Override
            public void onPath(PolylineOptions polyLine) {
                //when the path is retrieved, plot it on the map
                map.addPolyline(polyLine);
            }
        }).execute();

        //add markers to the src,dst locations.
        map.addMarker(new MarkerOptions().position(source).title("SOURCE"));
        map.addMarker(new MarkerOptions().position(destination).title("DEST"));
        //zoom to the source location with scale 12..
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 12));

    }
}