package com.ma7moud3ly.DrawMyRoute.util;


import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GetPathFromLocation extends AsyncTask<String, Void, List<RoutePoints>> {

    private String TAG = "GetPathFromLocation";
    private String API_KEY;
    private LatLng source, destination;
    private DirectionPointListener resultCallback;
    private boolean walkLine;
    private boolean alternatives;

    public GetPathFromLocation(LatLng source, LatLng destination, boolean alternatives, boolean walkLine,
                               String API_KEY, DirectionPointListener resultCallback) {
        this.API_KEY = API_KEY;
        this.source = source;
        this.destination = destination;
        this.walkLine = walkLine;
        this.alternatives = alternatives;
        this.resultCallback = resultCallback;
    }

    public String getUrl() {
        String str_origin = "origin=" + source.latitude + "," + source.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String alternatives = "alternatives=" + this.alternatives;
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + alternatives;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + API_KEY;
        Log.i("HINT", url);
        return url;
    }

    @Override
    protected List<RoutePoints> doInBackground(String... url) {

        try {

            JSONObject jsonObject;

            String json = routeResponse(getUrl());
            if (json.equals("")) return null;

            try {
                jsonObject = new JSONObject(json);
                // Starts parsing data
                List<List<HashMap<String, String>>> routes= new DirectionHelper().parse(jsonObject);

                List<RoutePoints> allRoutes = new ArrayList<>();

                // Traversing through all the routes
                for (int i = 0; i < routes.size(); i++) {
                    ArrayList<LatLng> points = new ArrayList<>();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = routes.get(i);
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    RoutePoints routePoints = new RoutePoints();
                    routePoints.drivingRoute = points;
                    routePoints.sourceWalk = curvedPolyline(source, points.get(0), 0.8);
                    routePoints.destWalk=curvedPolyline(points.get(points.size() - 1), destination, 0.8);
                    allRoutes.add(routePoints);
                }

                // Drawing polyline in the Google Map for the i-th route
                if (allRoutes != null && allRoutes.size()>0) {
                    return allRoutes;
                } else {
                    return null;
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception in Executing Routes : " + e.toString());
                return null;
            }

        } catch (Exception e) {
            Log.e(TAG, "Background Task Exception : " + e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<RoutePoints> routes) {
        super.onPostExecute(routes);
        if (resultCallback != null && routes != null)
            resultCallback.onPath(routes);
    }

    private String routeResponse(String url) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        String data = "";
        try {
            URL directionUrl = new URL(url);
            connection = (HttpURLConnection) directionUrl.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();
            bufferedReader.close();

        } catch (Exception e) {
            Log.e(TAG, "Exception : " + e.toString());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
        return data;
    }

    //draw a curved line between point p1, to p2
    //k defines curvature of the polyline
    private List<LatLng> curvedPolyline(LatLng p1, LatLng p2, double k) {
        //Calculate distance and heading between two points
        double d = SphericalUtil.computeDistanceBetween(p1, p2);
        double h = SphericalUtil.computeHeading(p1, p2);
        //Midpoint position
        LatLng p = SphericalUtil.computeOffset(p1, d * 0.5, h);
        //Apply some mathematics to calculate position of the circle center
        double x = (1 - k * k) * d * 0.5 / (2 * k);
        double r = (1 + k * k) * d * 0.5 / (2 * k);

        LatLng c = SphericalUtil.computeOffset(p, x, h + 90.0);

        //Calculate heading between circle center and two points
        double h1 = SphericalUtil.computeHeading(c, p1);
        double h2 = SphericalUtil.computeHeading(c, p2);

        //Calculate positions of points on circle border and add them to polyline options
        int numpoints = 100;
        double step = (h2 - h1) / numpoints;

         List<LatLng> points=new ArrayList<>();

        for (int i = 0; i < numpoints; i++) {
            LatLng pi = SphericalUtil.computeOffset(c, r, h1 + i * step);
            points.add(pi);
        }

        //Draw polyline
        return points;
    }

}