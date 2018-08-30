package com.example.marti.amimedicos.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marti.amimedicos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObservacionesServicio extends Fragment {


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

        return view;
    }

}
