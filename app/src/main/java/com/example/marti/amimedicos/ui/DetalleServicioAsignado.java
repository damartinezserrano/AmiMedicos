package com.example.marti.amimedicos.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marti.amimedicos.R;
import com.example.marti.amimedicos.settings.map.DownloadDirectionsMapData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleServicioAsignado extends Fragment implements OnMapReadyCallback {

    GoogleMap map=null;


    public DetalleServicioAsignado() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        DetalleServicioAsignado detalleServicioAsignado = new DetalleServicioAsignado();
        return detalleServicioAsignado;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_servicio_asignado, container, false);

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map=googleMap;

        LatLng origen = new LatLng(10.9997609,-74.79688529999999);
        LatLng destino = new LatLng(10.9891167,-74.79982380000001);
        map.moveCamera(CameraUpdateFactory.newLatLng(origen));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(origen,15));
        map.addMarker(new MarkerOptions().position(origen))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icono_corazon_verde));


        String url = getUrl(origen,destino);
        Log.d("URLis", url);

        DownloadDirectionsMapData downloadDirectionsMapData = new DownloadDirectionsMapData(map);
        downloadDirectionsMapData.execute(url);


    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }
}
