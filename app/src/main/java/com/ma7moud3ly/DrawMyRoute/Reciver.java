package com.ma7moud3ly.DrawMyRoute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

class Reciver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
/*
        GoogleMap mMap=new GoogleMap(this);
        Marker drivermaker = mMap.addMarker(new MarkerOptions().position(new LatLng(1,1)).title("SOURCE"));
        double mydoublelat = 0, mydoublelon = 0;

        drivermaker.remove();
        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(mydoublelat, mydoublelon));
        mp.title("my position");
        drivermaker = mMap.addMarker(mp);

        //read the location bearing form the intent or wherever..
        float bearing = intent.getFloatExtra("mybearing", 0);

        CameraPosition position = CameraPosition.builder()
                .bearing(bearing)
                .target(new LatLng(mydoublelat, mydoublelon))
                .zoom(mMap.getCameraPosition().zoom)
                .tilt(mMap.getCameraPosition().tilt)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
*/
    }
}