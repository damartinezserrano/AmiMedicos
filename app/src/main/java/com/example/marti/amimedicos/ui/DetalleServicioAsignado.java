package com.example.marti.amimedicos.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.marti.amimedicos.MainActivity;
import com.example.marti.amimedicos.R;
import com.example.marti.amimedicos.estructura.DetalleServicio;
import com.example.marti.amimedicos.estructura.FinalizarServicioBody;
import com.example.marti.amimedicos.estructura.Identificacion;
import com.example.marti.amimedicos.estructura.ReportarLlegadaBody;
import com.example.marti.amimedicos.interfaces.notification.NotificationM;
import com.example.marti.amimedicos.settings.Constant;
import com.example.marti.amimedicos.settings.map.DownloadDirectionsMapData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleServicioAsignado extends Fragment implements OnMapReadyCallback {

    GoogleMap map=null;
    Button reportar;
    LinearLayout inicio, avisoLlegada, hamButton;
    Boolean servicioIniciado=false;

    TextView nombreCli, telCli, infoAdicional;

    GsonBuilder gsonBuilder;
    Gson gson;

    RequestQueue requestQueue;

    ReportarLlegadaBody reportarLlegadaBody;
    FinalizarServicioBody finalizarServicioBody;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;



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

        inicio = view.findViewById(R.id.inicio);
        reportar = view.findViewById(R.id.reportar);
        avisoLlegada = view.findViewById(R.id.avisollegada);
        infoAdicional = view.findViewById(R.id.infoadicional);
        nombreCli = view.findViewById(R.id.nombrecli);
        telCli = view.findViewById(R.id.telefono);

        drawerLayout = view.findViewById(R.id.layout);
        navigationView = view.findViewById(R.id.nv);
        hamButton = view.findViewById(R.id.navbutton);

        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportar.setBackgroundColor(getResources().getColor(R.color.darkGreenColor));
                inicio.setVisibility(View.INVISIBLE);
                servicioIniciado=true;
            }
        });

        reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if(servicioIniciado){
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer((int) Gravity.LEFT);
                }
                    Constant.servicioIniciado=false;
                    avisoLlegada.setVisibility(View.VISIBLE);
                    putReporteLlegada(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_MEDICO + Constant.ENDPOINT_REGISTRAR_HORA_LLEGADA, Constant.cod_detalle_serv);
                   // putFinalizarServicio(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_MEDICO + Constant.ENDPOINT_FINALIZAR_SERVICIO,"3282500");
                    Toast.makeText(getActivity(),"webs : el médico ha llegado a su destino",Toast.LENGTH_SHORT);
                //}
            }
        });

        avisoLlegada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fg = ServicioActualUI.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();

            }
        });

        hamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer((int) Gravity.LEFT);
                }else{
                    drawerLayout.openDrawer((int) Gravity.LEFT);
                }

              //  int lockMode = DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
             //   drawerLayout.setDrawerLockMode(lockMode);
            }
        });

        getDetalleServicios(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_MEDICO + Constant.ENDPOINT_DETALLE_SERVICIO + Constant.SLASH + Constant.cod_detalle_serv);

        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer((int) Gravity.LEFT);
        }

        Constant.servicioIniciado=true;
        ((MainActivity)getActivity()).pedirUbicacion();

        super.onViewCreated(view, savedInstanceState);
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

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        return requestQueue;
    }

    public void getDetalleServicios(String UrlQuest) {

        requestQueue = getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlQuest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("PerfilUI :", "success");
                        parseSevicioResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //errores de peticion
                Log.i("PerfilUI :", "error");
                //parseLogInError(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError { //autorizamos basic
                Map<String, String> headers = new HashMap<>();
                String auth = getResources().getString(R.string.auth);
                headers.put("Authorization",auth);
                Log.i("PerfilUIToken ", headers.toString());
                return headers;
            }

        };
        requestQueue.add(stringRequest);
    }

    public void parseSevicioResponse(String response) {

        Gson gson3 = new Gson();
        DetalleServicio detalleServicio = new DetalleServicio();
        detalleServicio = gson3.fromJson(response,DetalleServicio.class);

        if(detalleServicio.getDetalle()!=null){

            String primerNombre = detalleServicio.getDetalle()[0].getPrimer_nombre();
            String primerApellido = detalleServicio.getDetalle()[0].getPrimer_apellido();
            String setNombre = "Cliente : "+primerNombre+" "+primerApellido;
            nombreCli.setText(setNombre);

            String telefCliente = detalleServicio.getDetalle()[0].getTelefono_servicio();
            telCli.setText(telefCliente);

            String adicional = detalleServicio.getDetalle()[0].getDiagnostico();
            infoAdicional.setText(adicional);
        }

    }

    public void putReporteLlegada(String URL, String id) {



        requestQueue = getRequestQueue();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, URL, putLlegadaBodyJSON(id), //hacemos la peticion post
                response -> {

                    Log.i("LogInFragment", "Se ha realizado el user post con exito");
                   // parseLogInResponse(response);

                }, error -> {

           // parseLogInError(error);
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError { //autorizamos basic
                Map<String, String> headers = new HashMap<>();
                String auth = getResources().getString(R.string.auth);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }

        };

        requestQueue.add(request);
    }


    public JSONObject putLlegadaBodyJSON(String id) { //construimos el json
        //primero json device
        String loginBody="";
        JSONObject jsonObject=null;

        reportarLlegadaBody = new ReportarLlegadaBody();
        reportarLlegadaBody.setConsec_movserv(id); //3282500


        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        loginBody = gson.toJson(reportarLlegadaBody);
        Log.i("loginRbody",loginBody);

        try {
            jsonObject = new JSONObject(loginBody);
            Log.i("jsonObject",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void putFinalizarServicio(String URL, String id) {



        requestQueue = getRequestQueue();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, URL, putFinalizarBodyJSON(id), //hacemos la peticion post
                response -> {

                    Log.i("LogInFragment", "Se ha realizado el user post con exito");


                }, error -> {

            // parseLogInError(error);
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError { //autorizamos basic
                Map<String, String> headers = new HashMap<>();
                String auth = getResources().getString(R.string.auth);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }

        };

        requestQueue.add(request);
    }

    public JSONObject putFinalizarBodyJSON(String id) { //construimos el json
        //primero json device
        String loginBody="";
        JSONObject jsonObject=null;

        finalizarServicioBody = new FinalizarServicioBody();
        finalizarServicioBody.setConsec_movserv(id); //3282500
        finalizarServicioBody.setCoordenada_x("1"); //1
        finalizarServicioBody.setCoordenada_y("2"); //2



        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        loginBody = gson.toJson(finalizarServicioBody);
        Log.i("loginRbody",loginBody);

        try {
            jsonObject = new JSONObject(loginBody);
            Log.i("jsonObject",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
