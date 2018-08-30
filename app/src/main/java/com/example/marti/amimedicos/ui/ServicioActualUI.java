package com.example.marti.amimedicos.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.marti.amimedicos.R;
import com.example.marti.amimedicos.interfaces.notification.NotificationM;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicioActualUI extends Fragment {

    Button finalizar;

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

                Fragment fg = ObservacionesServicio.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();
                Toast.makeText(getActivity(),"webs : Medico liberado",Toast.LENGTH_SHORT);

            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

}
