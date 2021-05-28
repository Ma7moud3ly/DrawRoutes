# DrawRoutes

# How To..
- Create new Google Maps Activity
### Manifest
1- add internet permission (essential) and fine location (optional)
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> 
```
2- add meta tags
```
<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="@string/google_maps_key" />
<meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" /> 
```
### app gradle
1- Add secrets_gradle_plugin in the plugins to be..
```
plugins {
    id 'com.android.application'
    id 'com.google.secrets_gradle_plugin' version '0.5'
}
```

2-Don't forget to add play-services-maps dependency.

```
implementation 'com.google.android.gms:play-services-maps:17.0.0'
```

### In Values/google_maps_api.xml (add your api key)

```
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">API_KEY</string>
```

### Map Activity..

1-Copy util to your package [util](https://github.com/Ma7moud3ly/DrawRoutes/tree/master/app/src/main/java/com/ma7moud3ly/DrawMyRoute)

2-Add this code in onMapReady() callback..
```
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
```      

  
