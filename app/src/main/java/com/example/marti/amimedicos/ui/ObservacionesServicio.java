package com.example.marti.amimedicos.ui;


import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.marti.amimedicos.R;
import com.example.marti.amimedicos.estructura.FinalizarServicioBody;
import com.example.marti.amimedicos.estructura.ValidarTriageBody;
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
public class ObservacionesServicio extends Fragment {

    Button enviar;
    ImageButton sibtn, nobtn;
    TextInputEditText textInputEditTextObs;
    Boolean concordancia = null;

    GsonBuilder gsonBuilder;
    Gson gson;

    RequestQueue requestQueue;

    ValidarTriageBody validarTriageBody;

    public ObservacionesServicio() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        ObservacionesServicio observacionesServicio = new ObservacionesServicio();
        return observacionesServicio;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_observaciones_servicio, container, false);

        sibtn = view.findViewById(R.id.sibtn);
        nobtn = view.findViewById(R.id.nobtn);
        enviar = view.findViewById(R.id.enviar);
        textInputEditTextObs = view.findViewById(R.id.observaciones);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        sibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nobtn.setVisibility(View.INVISIBLE);
                concordancia=true;

            }
        });

        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sibtn.setVisibility(View.INVISIBLE);
                concordancia=false;

            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String observaciones = textInputEditTextObs.getText().toString();

               if(concordancia==null || observaciones.equals("")){

                  if(concordancia==null) {
                      LayoutInflater inflater = LayoutInflater.from(getActivity());
                      View dialogView = inflater.inflate(R.layout.concordancia_error_layout, null);
                      new AlertDialog.Builder(getContext())
                              .setTitle("Elegir concordancia")
                              .setView(dialogView)
                              .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialogInterface, int i) {

                                  }
                              })
                              //.setMessage("Debe elgir Si o No para continuar")
                              .show();
                  }

                  if(observaciones.equals("")){
                      textInputEditTextObs.requestFocus();
                      Drawable drError = getResources().getDrawable(R.drawable.cancel);
                      drError.setBounds(new Rect(0, 0, 20, 20));
                      textInputEditTextObs.setError("Debe escribir una observación",drError);
                  }

               }else{

                   if (concordancia){
                       putValidarTriageo(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_MEDICO + Constant.ENDPOINT_VALIDAR_TRIAGE,Constant.cod_detalle_serv,"1",observaciones);
                   }else{
                       putValidarTriageo(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_MEDICO + Constant.ENDPOINT_VALIDAR_TRIAGE,Constant.cod_detalle_serv,"2",observaciones);

                   }

                   Toast.makeText(getActivity(), "Información Enviada",Toast.LENGTH_SHORT).show();
                   Fragment fg = ServiciosAsignadosUI.newInstance();
                   getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();

               }
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

    public void putValidarTriageo(String URL, String id, String valid, String observ) {



        requestQueue = getRequestQueue();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, URL, putTriageBodyJSON(id,valid,observ), //hacemos la peticion post
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

    public JSONObject putTriageBodyJSON(String id, String valid, String observ) { //construimos el json
        //primero json device
        String loginBody="";
        JSONObject jsonObject=null;

        validarTriageBody = new ValidarTriageBody();
        validarTriageBody.setConsec_movserv(id); //3282500
        validarTriageBody.setValidacion_triage(valid); //1 si , 2 no
        validarTriageBody.setObservacion_triage(observ);



        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        loginBody = gson.toJson(validarTriageBody);
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
