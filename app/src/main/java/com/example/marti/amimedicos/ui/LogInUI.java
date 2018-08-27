package com.example.marti.amimedicos.ui;


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

import com.example.marti.amimedicos.R;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInUI extends Fragment {

    TextInputLayout inputLayoutIden;
    TextInputEditText idenTView;
    Button loginBtn;
    String campoIden;

    static int noservice=0;


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

        idenTView = view.findViewById(R.id.identificacion);


        loginBtn=view.findViewById(R.id.ingresar);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campoIden=idenTView.getText().toString();
                if(!campoIden.equals("")){
                    Log.i("campos","Escritos");

                    if(campoIden.equals("no")){noservice=1;}else{noservice=0;}

                    Date currentTime = Calendar.getInstance().getTime();
                    String successMessage = "Ingreso Exitoso.\n"+currentTime.toString();
                    Toast.makeText(getActivity(),successMessage,Toast.LENGTH_SHORT).show();
                    Fragment fg = ServiciosAsignadosUI.newInstance();
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).addToBackStack(null).commit();
                }else{
                    idenTView.requestFocus();
                    Drawable drError = getResources().getDrawable(R.drawable.if_sign_error);
                    drError.setBounds(new Rect(0, 0, 50, 50));
                    idenTView.setError("Ha ocurrido un error en este campo",drError);
                }
            }
        });

        return view;
    }

}
