package com.example.marti.amimedicos.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.marti.amimedicos.R;
import com.example.marti.amimedicos.estructura.FinalizarServicioBody;
import com.example.marti.amimedicos.estructura.ReportarLlegadaBody;
import com.example.marti.amimedicos.interfaces.notification.NotificationM;
import com.example.marti.amimedicos.settings.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicioActualUI extends Fragment {

    Button finalizar;

    GsonBuilder gsonBuilder;
    Gson gson;

    RequestQueue requestQueue;

    FinalizarServicioBody finalizarServicioBody;

    public ServicioActualUI() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        ServicioActualUI servicioActualUI = new ServicioActualUI();
        return servicioActualUI;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicio_actual_ui, container, false);

        finalizar = view.findViewById(R.id.finalizar);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((NotificationM)getActivity()).localNotification();


        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                putFinalizarServicio(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_MEDICO + Constant.ENDPOINT_FINALIZAR_SERVICIO,"3282500");
                Fragment fg = ObservacionesServicio.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();
                Toast.makeText(getActivity(),"webs : Medico liberado",Toast.LENGTH_SHORT);

            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        return requestQueue;
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
