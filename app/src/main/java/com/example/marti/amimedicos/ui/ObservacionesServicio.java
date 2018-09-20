package com.example.marti.amimedicos.ui;


import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.marti.amimedicos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObservacionesServicio extends Fragment {

    Button enviar;
    ImageButton sibtn, nobtn;
    TextInputEditText textInputEditTextObs;
    Boolean concordancia = null;

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
                   Toast.makeText(getActivity(), "Información Enviada",Toast.LENGTH_SHORT).show();
                   Fragment fg = ServiciosAsignadosUI.newInstance();
                   getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();

               }
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

}
