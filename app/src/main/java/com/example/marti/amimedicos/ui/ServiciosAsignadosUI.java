package com.example.marti.amimedicos.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marti.amimedicos.R;
import com.example.marti.amimedicos.interfaces.notification.NotificationM;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiciosAsignadosUI extends Fragment {

    RelativeLayout relativeLayoutservicios;
    TextView textViewnoservice;
    Button cuadradoBtn;
    CardView cardView;

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

        if(LogInUI.noservice==1){
          textViewnoservice.setVisibility(View.VISIBLE);
          relativeLayoutservicios.setVisibility(View.GONE);
        }else{
            textViewnoservice.setVisibility(View.GONE);
            relativeLayoutservicios.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((NotificationM)getActivity()).localNotification();


        cuadradoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NotificationM)getActivity()).popupInternalNotification();
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fg = DetalleServicioAsignado.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

}
