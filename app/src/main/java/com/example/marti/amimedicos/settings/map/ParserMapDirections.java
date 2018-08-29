package com.example.marti.amimedicos.settings.map;


import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.marti.amimedicos.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marti on 21/08/18.
 */

public class ParserMapDirections extends AsyncTask< String,Integer,List<List<HashMap<String,String>>> > {

    GoogleMap mMap;

    ParserMapDirections(GoogleMap googleMap){
        mMap = googleMap;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jsonObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jsonObject = new JSONObject(jsonData[0]);
            Log.d("ParserTask",jsonData[0].toString());
            DataParser dataParser = new DataParser();

            routes = dataParser.parse(jsonObject);
            Log.d("ParserTask",routes.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        super.onPostExecute(result);
        ArrayList<LatLng> points;
        PolylineOptions polylineOptions = null;
        LatLng position=null;
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            polylineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = result.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String,String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                position = new LatLng(lat, lng);
                points.add(position);
                //
                polylineOptions.add(position);
                polylineOptions.width(10);
                polylineOptions.color(Color.GREEN);
                mMap.addPolyline(polylineOptions);
            }
            // polylineOptions.addAll(points);
            // polylineOptions.width(10);
            //  polylineOptions.color(Color.GREEN);
            if(position!=null)
                mMap.addMarker(new MarkerOptions().position(position)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icono_ambulancia));
            Log.d("onPostExecute","onPostExecute lineoptions decoded");
        }

        if(polylineOptions != null) {
            //  mMap.addPolyline(polylineOptions);
        }
        else {
            Log.d("onPostExecute","without Polylines drawn");
        }
    }
}
