package com.ma7moud3ly.DrawMyRoute;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ma7moud3ly.DrawMyRoute.util.DirectionPointListener;
import com.ma7moud3ly.DrawMyRoute.util.GetPathFromLocation;
import com.ma7moud3ly.DrawMyRoute.util.RoutePoints;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

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
        LatLng source = new LatLng(18.204751888957418, -63.07592214047706);
        LatLng destination = new LatLng(18.187612392585468, -63.09476338640147);
        //read the API_Key from google_maps_key
        String API_KEY = getResources().getString(R.string.google_maps_key);
        //draw dotted walk line
        boolean walkLine = true;
        //draw alternative routes if possible
        boolean alternatives = true;
        //return a polyLine for the route from source to the dest.

        new GetPathFromLocation(source, destination, alternatives, walkLine, API_KEY, new DirectionPointListener() {
            @Override
            public void onPath(List<RoutePoints> routes) {

                //a dotted pattern for the walk line
                final List<PatternItem> pattern = Arrays.asList(new Dot(), new Gap(20));
                // color for different routes
                final int routeColors[] = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};

                RoutePoints route = null;
                int color = routeColors[0];
                //iterate over all routes
                for (int i = 0; i < routes.size(); i++) {
                    route = routes.get(i);
                    color = routeColors[i];
                    //draw the driving route
                    PolylineOptions options = new PolylineOptions()
                            .addAll(route.drivingRoute)
                            .width(10)
                            .color(color)
                            .clickable(true);
                    //add the route to the map
                    Polyline drivingRoute = map.addPolyline(options);
                    //add tag to the route to be accessible
                    drivingRoute.setTag("route_" + i);
                }
                //here we draw the dotted walk line once
                if (route != null) {
                    //the dotted line between source->near driving route
                    PolylineOptions destWalk = new PolylineOptions()
                            .addAll(route.destWalk)
                            .width(10)
                            .color(color)
                            .pattern(pattern);
                    //the dotted line between dest->last driving route
                    PolylineOptions srcWalk = new PolylineOptions()
                            .addAll(route.sourceWalk)
                            .width(10)
                            .color(color)
                            .pattern(pattern);
                    //add both routes to the map
                    map.addPolyline(destWalk);
                    map.addPolyline(srcWalk);
                }

            }
        }).execute();

        //set the routes listen to the click events
        map.setOnPolylineClickListener(this);
        //add markers to the src,dst locations.
        map.addMarker(new MarkerOptions().position(source).title("SOURCE"));
        map.addMarker(new MarkerOptions().position(destination).title("DEST"));
        //zoom to the source location with scale 12..
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 24));

    }

    @Override
    public void onPolylineClick(Polyline route) {
        //set the clicked route at the top
        route.setZIndex(route.getZIndex() + 1);
        //do something with the selected route..
        Toast.makeText(this, "" + route.getTag(), Toast.LENGTH_SHORT).show();
    }
}