package com.example.marti.amimedicos.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marti.amimedicos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiciosAsignadosUI extends Fragment {

    RelativeLayout relativeLayoutservicios;
    TextView textViewnoservice;

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

        if(LogInUI.noservice==1){
          textViewnoservice.setVisibility(View.VISIBLE);
          relativeLayoutservicios.setVisibility(View.GONE);
        }else{
            textViewnoservice.setVisibility(View.GONE);
            relativeLayoutservicios.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
