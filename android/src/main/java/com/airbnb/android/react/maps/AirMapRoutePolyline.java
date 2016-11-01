package com.airbnb.android.react.maps;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;
import okhttp3.Response;

//import static java.security.AccessController.getContext;

/**
 * Created by aspid on 30.10.16.
 */





public class AirMapRoutePolyline extends AirMapFeature {

    private class Location{
        double lat;
        double lng;
    }

    private class Step{
        public Location start_location;
        public Location end_location;
    }

    private class Leg{
        ArrayList<Step> steps;
    }

    private class Route{
        ArrayList<Leg> legs;
    }

    private class DirectionAnswer{
        ArrayList<Route> routes;
    }

    private PolylineOptions polylineOptions;
    private Polyline polyline;

    private List<LatLng> coordinates;
    private List<LatLng> route;

    private int color;
    private float width;
    private boolean geodesic;
    private float zIndex;

    public AirMapRoutePolyline(Context context) {
        super(context);
    }
    AsyncHttpClient client = new AsyncHttpClient();
    JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onStart() {
            Toast.makeText(getContext(),"start",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            super.onSuccess(statusCode, headers, responseString);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            // called when response HTTP status is "200 OK"
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            DirectionAnswer directionAnswer = gson.fromJson(response.toString(),DirectionAnswer.class);
//            Toast.makeText(getContext(),response.toString(),Toast.LENGTH_SHORT).show();
            coordinates = new ArrayList<>();
//            coordinates.addAll(route);

            for( int i = 0 ; i < directionAnswer.routes.size() ; ++i){
                for( int j = 0 ; j < directionAnswer.routes.get(i).legs.size() ; ++j){
                    for( int k = 0 ; k < directionAnswer.routes.get(i).legs.get(j).steps.size() ; ++k){
                        coordinates.add(new LatLng(
                                directionAnswer.routes.get(i).legs.get(j).steps.get(k).start_location.lat,
                                directionAnswer.routes.get(i).legs.get(j).steps.get(k).start_location.lng
                        ));
                        coordinates.add(new LatLng(
                                directionAnswer.routes.get(i).legs.get(j).steps.get(k).end_location.lat,
                                directionAnswer.routes.get(i).legs.get(j).steps.get(k).end_location.lng
                        ));

                    }
                }
            }
            if (polyline != null) {
                polyline.setPoints(coordinates);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Toast.makeText(getContext(),throwable.toString(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFinish() {
            Toast.makeText(getContext(),"finish",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onRetry(int retryNo) {
            // called when request is retried
        }
    };
    private Runnable r = new Runnable() {
        @Override
        public void run() {

            String url = "https://maps.googleapis.com/maps/api/directions/json";
            RequestParams params = new RequestParams();
            if(route.size()>=2) {
                LatLng origin = route.get(0);
                LatLng destinition = route.get(route.size()-1);
                params.put("origin", origin.latitude+","+origin.longitude);
                params.put("destination", destinition.latitude+","+destinition.longitude);
//                params.put("language","ru");
//                params.put("key","AIzaSyCmyJg8DNf2K7kXNdKRxTFmY8JCOLQCo5A");
                String waypoints = "";

                for (int i = 1 ; i < route.size()-1;++i)
                    waypoints = waypoints + "via:"+route.get(i).latitude+","+route.get(i).longitude+"|";
                params.put("waypoints",waypoints);
                client.get(url,params, jsonHttpResponseHandler);
            }
//            Toast.makeText(getContext(),"fuck",Toast.LENGTH_SHORT).show();

        }
    };

    public void setCoordinates(ReadableArray coordinates) {
//        if(route == null)
            route = new ArrayList<>();
        this.coordinates = new ArrayList<>(coordinates.size());

        for (int i = 0; i < coordinates.size(); i++) {
            ReadableMap coordinate = coordinates.getMap(i);
//            this.coordinates.add(i,
//                    new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")));
            route.add(new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")));
        }
//        route.add(new LatLng(0,0));
//        if (polyline != null) {
//            polyline.setPoints(this.coordinates);
//        }
        r.run();


//        if (polyline != null) {
//            polyline.setPoints(this.coordinates);
//        }
    }


    public void setColor(int color) {
        this.color = color;
        if (polyline != null) {
            polyline.setColor(color);
        }
    }

    public void setWidth(float width) {
        this.width = width;
        if (polyline != null) {
            polyline.setWidth(width);
        }
    }

    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (polyline != null) {
            polyline.setZIndex(zIndex);
        }
    }

    public void setGeodesic(boolean geodesic) {
        this.geodesic = geodesic;
        if (polyline != null) {
            polyline.setGeodesic(geodesic);
        }
    }

    public PolylineOptions getPolylineOptions() {
        if (polylineOptions == null) {
            polylineOptions = createPolylineOptions();
        }
        return polylineOptions;
    }

    private PolylineOptions createPolylineOptions() {
        PolylineOptions options = new PolylineOptions();
        options.addAll(route);
        options.color(color);
        options.width(width);
        options.geodesic(geodesic);
        options.zIndex(zIndex);
        return options;
    }

    @Override
    public Object getFeature() {
        return polyline;
    }

    @Override
    public void addToMap(GoogleMap map) {
        polyline = map.addPolyline(getPolylineOptions());

    }

    @Override
    public void removeFromMap(GoogleMap map) {
        polyline.remove();
    }
}
