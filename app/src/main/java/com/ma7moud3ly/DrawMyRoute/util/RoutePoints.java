package com.ma7moud3ly.DrawMyRoute.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class RoutePoints {
    //the points for the driving route Polyline
    public List<LatLng> drivingRoute;
    //the points for the walking route source-> start of driving route
    public List<LatLng> sourceWalk;
    //the points for the walking route driving end of the driving route -> dest
    public List<LatLng> destWalk;
}