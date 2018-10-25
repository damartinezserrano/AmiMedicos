package com.example.marti.amimedicos.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.marti.amimedicos.R;
import com.example.marti.amimedicos.estructura.EstructuraLogin;
import com.example.marti.amimedicos.estructura.Identificacion;
import com.example.marti.amimedicos.settings.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInUI extends Fragment {

    TextInputLayout inputLayoutIden;
    TextInputEditText idenTView,contrasenaTView;
    Button loginBtn;
    String campoIden, campoContra;

    static int noservice=0;

    GsonBuilder gsonBuilder;
    Gson gson;

    RequestQueue requestQueue;

    Identificacion identificacion;

    SharedPreferences sharedPref;

    public LogInUI() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        LogInUI logInUI = new LogInUI();
        return logInUI;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in_ui, container, false);
        sharedPref = getActivity().getSharedPreferences(Constant.PREFERENCE_LOGIN, Context.MODE_PRIVATE);
        idenTView = view.findViewById(R.id.identificacion);
        contrasenaTView = view.findViewById(R.id.contrasena);
        Drawable drIden = getResources().getDrawable(R.drawable.icono_usuario2);
        drIden.setBounds(new Rect(0, 0, 20, 20));
        idenTView.setCompoundDrawables(drIden,null,null,null);

        Drawable drContra = getResources().getDrawable(R.drawable.candado);
        drContra.setBounds(new Rect(0, 0, 20, 20));
        contrasenaTView.setCompoundDrawables(drContra,null,null,null);

        /*if(!sharedPref.getString(Constant.TOKEN_FIREBASE_PREF, "").equals("")){
            campoIden = sharedPref.getString(Constant.USER_PREF,"");
            campoContra = sharedPref.getString(Constant.PASS_PREF,"");
            postLogin(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_GENERAL + Constant.ENDPOINT_LOGIN, campoIden, campoContra);
        }*/

        loginBtn=view.findViewById(R.id.ingresar);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campoIden=idenTView.getText().toString();
                campoContra=contrasenaTView.getText().toString();
                if(!campoIden.equals("")&&!campoContra.equals("")){
                    Log.i("campos","Escritos");

                    //if(campoIden.equals("no")){noservice=1;}else{noservice=0;}

                    Date currentTime = Calendar.getInstance().getTime();
                    String successMessage = "Ingreso Exitoso.\n"+currentTime.toString();
                    Toast.makeText(getActivity(),successMessage,Toast.LENGTH_SHORT).show();
                    postLogin(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_GENERAL + Constant.ENDPOINT_LOGIN, campoIden, campoContra);
                }else{
                    if(campoIden.equals("")) {
                        idenTView.requestFocus();
                        Drawable drError = getResources().getDrawable(R.drawable.cancel);
                        drError.setBounds(new Rect(0, 0, 20, 20));
                        idenTView.setError("Este campo no puede estar vacío", drError);
                    }
                    if(campoContra.equals("")) {
                        contrasenaTView.requestFocus();
                        Drawable drError = getResources().getDrawable(R.drawable.cancel);
                        drError.setBounds(new Rect(0, 0, 20, 20));
                        contrasenaTView.setError("Este campo no puede estar vacío", drError);
                    }
                }
            }
        });

        return view;
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        return requestQueue;
    }


    public void postLogin(String URL, String id, String contra) {



        requestQueue = getRequestQueue();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, loginBodyJSON(id,contra), //hacemos la peticion post
                response -> {

                    Log.i("LogInFragment", "Se ha realizado el user post con exito");
                   parseLogInResponse(response);

                }, error -> {

            parseLogInError(error);
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError { //autorizamos basic
                Map<String, String> headers = new HashMap<>();
                String auth = getResources().getString(R.string.auth);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                headers.put("token_movil", "123");
                return headers;
            }

        };

        requestQueue.add(request);
    }

    public JSONObject loginBodyJSON(String id, String contra) { //construimos el json
        //primero json device
        String loginBody="";
        JSONObject jsonObject=null;

        identificacion = new Identificacion();
        identificacion.setUsuario(id); //72000325  22540125
        identificacion.setContrasena(contra); //isabella  jannyscaicedoa
        identificacion.setToken_movil(Constant.TOKEN_FB);

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        loginBody = gson.toJson(identificacion);
        Log.i("loginRbody",loginBody);

        try {
            jsonObject = new JSONObject(loginBody);
            Log.i("jsonObject",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public void parseLogInResponse(JSONObject response) {

        Gson gson3 = new Gson();
        EstructuraLogin estructuraLogin = gson3.fromJson(response.toString(),EstructuraLogin.class);


        Constant.ID = idenTView.getText().toString();
        Constant.TOKEN = estructuraLogin.getToken();

        sharedPref.edit().putString(Constant.TOKEN_FIREBASE_PREF,Constant.TOKEN_FB).apply();
        sharedPref.edit().putString(Constant.USER_PREF,campoIden).apply();
        sharedPref.edit().putString(Constant.PASS_PREF,campoContra).apply();
      //  Constant.slistaContratos = estructuraLogin.getLista();

        Fragment fg = ServiciosAsignadosUI.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();


    }

    public void parseLogInError(VolleyError error) {

        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            boolean estado = data.getBoolean("estado");
            String mensaje = data.getString("mensaje");
            idenTView.requestFocus();
            Drawable drError = getResources().getDrawable(R.drawable.error);
            drError.setBounds(new Rect(0, 0, idenTView.getHeight() / 2, idenTView.getHeight() / 2));
            idenTView.setError(mensaje, drError);
            Log.i("LogInFragment", "Ha ocurrido un error en el Login : "+estado+" , "+mensaje);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

}
