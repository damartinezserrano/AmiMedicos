package com.example.marti.amimedicos.ui;


import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.marti.amimedicos.R;
import com.example.marti.amimedicos.estructura.ListaServiciosPendiente;
import com.example.marti.amimedicos.interfaces.notification.NotificationM;
import com.example.marti.amimedicos.settings.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiciosAsignadosUI extends Fragment {

    RelativeLayout relativeLayoutservicios;
    TextView textViewnoservice,nombrecli,telcli;
    Button cuadradoBtn;
    CardView cardView;
    ImageView salir;

    GsonBuilder gsonBuilder;
    Gson gson;

    RequestQueue requestQueue;

    public ServiciosAsignadosUI() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        ServiciosAsignadosUI serviciosAsignadosUI = new ServiciosAsignadosUI();
        return serviciosAsignadosUI;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicios_asignados_ui, container, false);

        relativeLayoutservicios = view.findViewById(R.id.servicios);
        textViewnoservice = view.findViewById(R.id.noservicio);
        cuadradoBtn = view.findViewById(R.id.cuadrado);
        cardView = view.findViewById(R.id.cardservice);
        nombrecli = view.findViewById(R.id.nombrecli);
        telcli = view.findViewById(R.id.telcli);
        salir = view.findViewById(R.id.salir);

       /* if(LogInUI.noservice==1){
          textViewnoservice.setVisibility(View.VISIBLE);
          relativeLayoutservicios.setVisibility(View.GONE);
        }else{
            textViewnoservice.setVisibility(View.GONE);
            relativeLayoutservicios.setVisibility(View.VISIBLE);
        }*/

       getServiciosPendientes(Constant.HTTP_DOMAIN + Constant.APP_PATH + Constant.ENDPOINT_MEDICO + Constant.ENDPOINT_LISTA_SERVICIO_PENDIENTE + Constant.SLASH + Constant.ID);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //((NotificationM)getActivity()).localNotification();


        cuadradoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((NotificationM)getActivity()).popupInternalNotification();
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fg = DetalleServicioAsignado.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment fg2 = LogInUI.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg2).addToBackStack(null).commit();            }
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

    public void getServiciosPendientes(String UrlQuest) {

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
                parseLogInError(error);
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
        ListaServiciosPendiente listaServiciosPendiente = new ListaServiciosPendiente();
        listaServiciosPendiente = gson3.fromJson(response,ListaServiciosPendiente.class);

            if (listaServiciosPendiente.getServicio()!=null) {

                textViewnoservice.setVisibility(View.GONE);
                relativeLayoutservicios.setVisibility(View.VISIBLE);

                String setNombre = listaServiciosPendiente.getServicio().getPrimer_nombre() + " " + listaServiciosPendiente.getServicio().getPrimer_apellido();
                nombrecli.setText(setNombre);
                Constant.NOMBRE_CLIENTE = setNombre;

                String setTel = listaServiciosPendiente.getServicio().getTelefono_servicio();
                telcli.setText(setTel);

                String cColor = listaServiciosPendiente.getServicio().getTipo_servicio_id_tiposerv();
                if(cColor.equals("1")){
                    cuadradoBtn.setBackground(getResources().getDrawable(R.drawable.cuadrado_bordes_redondos));
                    Constant.CLASIFIC_SERV = "Emergencia";
                }else{
                    if(cColor.equals("2")){
                        cuadradoBtn.setBackground(getResources().getDrawable(R.drawable.cuadrado_amarillo));
                        Constant.CLASIFIC_SERV = "Urgencia";
                    } else{
                        if(cColor.equals("3")){
                            cuadradoBtn.setBackground(getResources().getDrawable(R.drawable.cuadrado_verde));
                            Constant.CLASIFIC_SERV = "Baja Complejidad";
                        }
                    }
                }
                Constant.cod_detalle_serv=listaServiciosPendiente.getServicio().getConsec_movserv();
            } else {
                textViewnoservice.setVisibility(View.VISIBLE);
                relativeLayoutservicios.setVisibility(View.GONE);
            }


    }

    public void parseLogInError(VolleyError error) {

        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            boolean estado = data.getBoolean("estado");
            String mensaje = data.getString("mensaje");

            if(mensaje.equals("Sin resultados.")){
                textViewnoservice.setVisibility(View.VISIBLE);
                relativeLayoutservicios.setVisibility(View.GONE);
            }
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
